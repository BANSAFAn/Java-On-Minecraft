import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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
        for (Map.Entry<String, String> entry : SITES.entrySet()) {
            String provider = entry.getKey();
            String url = entry.getValue();
            try {
                String version = checkVersion(provider, url);
                System.out.println(provider + ": " + version);
            } catch (IOException e) {
                System.err.println("Error checking " + provider + ": " + e.getMessage());
            }
        }
    }

    private static String checkVersion(String provider, String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        String version = "";

        switch (provider) {
            case "Adoptium":
                Elements releases = doc.select(".release-versions");
                if (!releases.isEmpty()) {
                    version = releases.first().text();
                }
                break;
            case "Oracle":
                Elements links = doc.select("a:contains(jdk-17)");
                if (!links.isEmpty()) {
                    version = links.first().text().split(" ")[0];
                }
                break;
            case "Amazon Corretto":
                Elements rows = doc.select("table tr");
                if (rows.size() > 1) {
                    version = rows.get(1).select("td").get(0).text();
                }
                break;
            case "Azul Zulu":
                Elements versions = doc.select(".version-string");
                if (!versions.isEmpty()) {
                    version = versions.first().text();
                }
                break;
            case "Red Hat OpenJDK":
            case "Microsoft OpenJDK":
                version = "Version info not available, please check manually";
                break;
        }

        return version;
    }

    private static boolean isUrlUpdated(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("HEAD");
        long lastModified = connection.getLastModified();
        connection.disconnect();

        System.out.println("Last modified: " + new java.util.Date(lastModified));

        return true; 
    }
}