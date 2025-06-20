import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.json.JSONObject;

public class JavaVersionChecker {
    private static final Map<String, String> SITES = new HashMap<>();
    
    static {
        SITES.put("Adoptium", "https://adoptium.net/temurin/releases/?version=17");
        SITES.put("Oracle", "https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html");
        SITES.put("Amazon Corretto", "https://docs.aws.amazon.com/corretto/latest/corretto-17-ug/downloads-list.html");
        SITES.put("Azul Zulu", "https://www.azul.com/downloads/?version=java-17-lts");
        SITES.put("Red Hat OpenJDK", "https://developers.redhat.com/products/openjdk/download");
        SITES.put("Microsoft OpenJDK", "https://learn.microsoft.com/en-us/java/openjdk/download");
    }

    public static void main(String[] args) {
        JSONObject results = new JSONObject();
        
        for (Map.Entry<String, String> entry : SITES.entrySet()) {
            String provider = entry.getKey();
            String url = entry.getValue();
            JSONObject providerInfo = new JSONObject();
            
            try {
                // Check if site is available
                boolean isAvailable = isSiteAvailable(url);
                String status = isAvailable ? "available" : "unavailable";
                providerInfo.put("status", status);
                
                // Get version if site is available
                if (isAvailable) {
                    String version = checkVersion(provider, url);
                    providerInfo.put("version", version);
                    providerInfo.put("last_checked", System.currentTimeMillis());
                }
                
                results.put(provider, providerInfo);
                System.out.println(provider + ": " + (isAvailable ? "Available" : "Unavailable") + 
                                  (isAvailable ? ", Version: " + providerInfo.optString("version", "Unknown") : ""));
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                providerInfo.put("status", "error");
                providerInfo.put("error", e.getMessage());
                results.put(provider, providerInfo);
                System.err.println("Error checking " + provider + ": " + e.getMessage());
            }
            
            // Add a small delay between requests to avoid overloading servers
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        // Output JSON result
        System.out.println(results.toString(2));
    }

    private static String checkVersion(String provider, String url) throws IOException {
        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                .timeout(10000)
                .get();
        String version = "";

        try {
            switch (provider) {
                case "Adoptium":
                    // Adoptium changed their HTML structure, try different selectors
                    Elements adoptiumVersions = doc.select(".release-versions, .temurin-version, h2:contains(Temurin), .version-info");
                    if (!adoptiumVersions.isEmpty()) {
                        version = adoptiumVersions.first().text();
                        if (version.contains("Temurin")) {
                            version = version.replaceAll(".*?(\\d+\\.\\d+\\.\\d+).*", "$1");
                        }
                    } else {
                        // Fallback to title which often contains version
                        version = doc.title();
                        if (version.contains("17")) {
                            version = "17.x";
                        }
                    }
                    break;
                case "Oracle":
                    Elements oracleLinks = doc.select("a:contains(jdk-17), h1:contains(Java 17), title:contains(Java 17)");
                    if (!oracleLinks.isEmpty()) {
                        String text = oracleLinks.first().text();
                        // Extract version pattern like 17.0.x
                        if (text.matches(".*\\d+\\.\\d+\\.\\d+.*")) {
                            version = text.replaceAll(".*?(\\d+\\.\\d+\\.\\d+).*", "$1");
                        } else {
                            version = "17.x";
                        }
                    }
                    break;
                case "Amazon Corretto":
                    Elements correttoRows = doc.select("table tr, h1:contains(Corretto 17), title:contains(Corretto 17)");
                    if (!correttoRows.isEmpty()) {
                        if (correttoRows.first().select("td").size() > 0) {
                            version = correttoRows.get(1).select("td").get(0).text();
                        } else {
                            String text = correttoRows.first().text();
                            if (text.matches(".*\\d+\\.\\d+\\.\\d+.*")) {
                                version = text.replaceAll(".*?(\\d+\\.\\d+\\.\\d+).*", "$1");
                            } else {
                                version = "17.x";
                            }
                        }
                    }
                    break;
                case "Azul Zulu":
                    Elements zuluVersions = doc.select(".version-string, h1:contains(Zulu), title:contains(Java 17)");
                    if (!zuluVersions.isEmpty()) {
                        String text = zuluVersions.first().text();
                        if (text.matches(".*\\d+\\.\\d+\\.\\d+.*")) {
                            version = text.replaceAll(".*?(\\d+\\.\\d+\\.\\d+).*", "$1");
                        } else {
                            version = "17.x";
                        }
                    }
                break;
                case "Red Hat OpenJDK":
                    Elements redhatVersions = doc.select("h1:contains(OpenJDK), title:contains(OpenJDK), .pf-c-title:contains(OpenJDK)");
                    if (!redhatVersions.isEmpty()) {
                        String text = redhatVersions.first().text();
                        if (text.matches(".*\\d+\\.\\d+\\.\\d+.*")) {
                            version = text.replaceAll(".*?(\\d+\\.\\d+\\.\\d+).*", "$1");
                        } else {
                            version = "17.x";
                        }
                    } else {
                        version = "17.x (Latest)";
                    }
                    break;
                case "Microsoft OpenJDK":
                    Elements msVersions = doc.select("h1:contains(OpenJDK), title:contains(OpenJDK), .content-title:contains(OpenJDK)");
                    if (!msVersions.isEmpty()) {
                        String text = msVersions.first().text();
                        if (text.matches(".*\\d+\\.\\d+\\.\\d+.*")) {
                            version = text.replaceAll(".*?(\\d+\\.\\d+\\.\\d+).*", "$1");
                        } else {
                            version = "17.x";
                        }
                    } else {
                        version = "17.x (Latest)";
                    }
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error extracting version for " + provider + ": " + e.getMessage());
            version = "17.x (Error extracting version)";
        }

        return version.isEmpty() ? "17.x (Latest)" : version;
    }

    private static boolean isUrlUpdated(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("HEAD");
        long lastModified = connection.getLastModified();
        connection.disconnect();

        System.out.println("Last modified: " + new java.util.Date(lastModified));

        return true; 
    }
    
    private static boolean isSiteAvailable(String urlString) {
        int maxRetries = 3;
        int retryCount = 0;
        int retryDelayMs = 2000; // 2 seconds
        
        while (retryCount < maxRetries) {
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(10000); // 10 seconds
                connection.setReadTimeout(10000); // 10 seconds
                connection.setInstanceFollowRedirects(true);
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
                connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                
                int responseCode = connection.getResponseCode();
                connection.disconnect();
                
                // Consider 2xx and 3xx as success
                if (200 <= responseCode && responseCode <= 399) {
                    return true;
                }
                
                // If we get a 4xx or 5xx, retry
                System.out.println("Attempt " + (retryCount + 1) + " for " + urlString + " failed with response code: " + responseCode);
                
            } catch (Exception e) {
                System.err.println("Attempt " + (retryCount + 1) + " for " + urlString + " failed with error: " + e.getMessage());
            }
            
            retryCount++;
            
            // Wait before retrying
            if (retryCount < maxRetries) {
                try {
                    Thread.sleep(retryDelayMs);
                    // Increase delay for next retry
                    retryDelayMs *= 2;
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        
        return false; // All attempts failed
    }
}