import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadmeUpdater {
    private static final Map<String, String> PROVIDERS = new HashMap<>();
    private static final Map<String, String> TIMESTAMP_TEXT = new HashMap<>();
    private static JSONObject javaStatus;
    private static final String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

    static {
        // Map provider keys to their names in README files
        PROVIDERS.put("Adoptium", "AdoptOpenJDK \\(Adoptium\\)");
        PROVIDERS.put("Oracle", "Oracle Java");
        PROVIDERS.put("Amazon Corretto", "Amazon Corretto");
        PROVIDERS.put("Azul Zulu", "Azul Zulu");
        PROVIDERS.put("Red Hat OpenJDK", "Red Hat OpenJDK");
        PROVIDERS.put("Microsoft OpenJDK", "Microsoft Build of OpenJDK");

        // Timestamp texts for different languages
        TIMESTAMP_TEXT.put("en", "\n\n*Java download status last updated: " + currentDate + "*");
        TIMESTAMP_TEXT.put("ru", "\n\n*Статус загрузки Java последний раз обновлялся: " + currentDate + "*");
        TIMESTAMP_TEXT.put("ua", "\n\n*Статус завантаження Java останнє оновлення: " + currentDate + "*");
        TIMESTAMP_TEXT.put("de", "\n\n*Java-Download-Status zuletzt aktualisiert: " + currentDate + "*");
        TIMESTAMP_TEXT.put("zh", "\n\n*Java下载状态最后更新: " + currentDate + "*");
        TIMESTAMP_TEXT.put("pl", "\n\n*Status pobierania Java ostatnio zaktualizowany: " + currentDate + "*");
        // Additional languages
        TIMESTAMP_TEXT.put("es", "\n\n*Estado de descarga de Java actualizado por última vez: " + currentDate + "*");
        TIMESTAMP_TEXT.put("fr", "\n\n*Statut de téléchargement Java dernière mise à jour: " + currentDate + "*");
        TIMESTAMP_TEXT.put("it", "\n\n*Stato di download di Java ultimo aggiornamento: " + currentDate + "*");
        TIMESTAMP_TEXT.put("ja", "\n\n*Javaダウンロードステータス最終更新: " + currentDate + "*");
        TIMESTAMP_TEXT.put("ko", "\n\n*Java 다운로드 상태 마지막 업데이트: " + currentDate + "*");
    }

    public static void main(String[] args) {
        try {
            // Load the status data from the JSON file
            String jsonContent = new String(Files.readAllBytes(Paths.get("java_status.json")), StandardCharsets.UTF_8);
            javaStatus = new JSONObject(jsonContent);

            // Update the main README.md file
            System.out.println("Updating README.md...");
            updateReadme("README.md", "en");

            // Check if README directory exists
            boolean readmeDirExists = new File("README").exists() && new File("README").isDirectory();
            
            // Update translated README files if they exist
             for (Map.Entry<String, String> entry : TIMESTAMP_TEXT.entrySet()) {
                String lang = entry.getKey();
                if (!lang.equals("en")) {
                    // Try both in README directory and in root directory
                    String[] possiblePaths = {
                        "README/README." + lang + ".md",
                        "README." + lang + ".md"
                    };
                    
                    boolean fileUpdated = false;
                    for (String readmePath : possiblePaths) {
                        File readmeFile = new File(readmePath);
                        if (readmeFile.exists()) {
                            System.out.println("Updating " + readmePath + "...");
                            updateReadme(readmePath, lang);
                            fileUpdated = true;
                            break;
                        }
                    }
                    
                    if (!fileUpdated) {
                        System.out.println("Warning: Could not find README file for language: " + lang);
                    }
                }
            }

            System.out.println("README files updated successfully.");

        } catch (Exception e) {
            System.err.println("Error updating README files: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void updateReadme(String filePath, String lang) throws IOException {
        // Read the content of the README file
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }

        String contentStr = content.toString();

        // Remove existing timestamp if present
        contentStr = contentStr.replaceAll("\n\n\\*Java[^\n]*\\*$", "");

        // Update status badges in the table
        if (contentStr.contains("| Provider | Status | Website |")) {
            for (Map.Entry<String, String> entry : PROVIDERS.entrySet()) {
                String provider = entry.getKey();
                String statusBadge = getStatusBadge(provider);
                String versionInfo = getVersionInfo(provider);
                
                // Replace the status badge in the table
                Pattern pattern = Pattern.compile("\\| " + provider + " \\| .*? \\|");
                Matcher matcher = pattern.matcher(contentStr);
                if (matcher.find()) {
                    contentStr = matcher.replaceAll("| " + provider + " | " + statusBadge + " |");
                }
                
                // Also try to update version info if it exists in a separate row
                Pattern versionPattern = Pattern.compile("(\\| " + provider + " \\| .*? \\|[^\n]*\n\\|[^\n]*\\| )Current version:.*?(\\|)");
                Matcher versionMatcher = versionPattern.matcher(contentStr);
                if (versionMatcher.find()) {
                    contentStr = versionMatcher.replaceAll("$1" + versionInfo + "$2");
                }
            }
        } 
        // Update status in the detailed information section
        else {
            for (Map.Entry<String, String> entry : PROVIDERS.entrySet()) {
                String providerKey = entry.getKey();
                String providerName = entry.getValue();
                String statusBadge = getStatusBadge(providerKey);
                String versionInfo = getVersionInfo(providerKey);
                
                // Try different patterns to find the provider section
                String[] patterns = {
                    "(.*" + providerName + ".*\n.*Website:.*\n.*Description:.*)",
                    "(.*" + providerName + ".*\n.*Website:.*)",
                    "(.*" + providerName + ".*)"
                };
                
                boolean found = false;
                for (String patternStr : patterns) {
                    if (found) break;
                    
                    Pattern pattern = Pattern.compile(patternStr);
                    Matcher matcher = pattern.matcher(contentStr);
                    if (matcher.find()) {
                        // Check if Status line already exists
                        String matchedText = matcher.group(1);
                        if (matchedText.contains("Status:")) {
                            // Replace existing status line
                            contentStr = contentStr.replaceAll(
                                "(" + Pattern.quote(matchedText) + "[^\n]*Status:).*\n",
                                "$1 " + statusBadge + " " + versionInfo + "\n"
                            );
                        } else {
                            // Add new status line
                            contentStr = matcher.replaceAll("$1\n   - Status: " + statusBadge + " " + versionInfo);
                        }
                        found = true;
                    }
                }
            }
        }

        // Add timestamp at the end
        contentStr += TIMESTAMP_TEXT.getOrDefault(lang, TIMESTAMP_TEXT.get("en"));
        
        // Make sure the content ends with a newline
        if (!contentStr.endsWith("\n")) {
            contentStr += "\n";
        }

        // Write the updated content back to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, StandardCharsets.UTF_8))) {
            writer.write(contentStr);
        }
    }

    private static String getStatusBadge(String provider) {
        if (javaStatus.has(provider)) {
            JSONObject providerInfo = javaStatus.getJSONObject(provider);
            
            // Check if site is available
            if (providerInfo.has("available")) {
                boolean isAvailable = providerInfo.getBoolean("available");
                
                // Check if there's an error message
                String errorMessage = "";
                if (providerInfo.has("error")) {
                    errorMessage = providerInfo.getString("error");
                }
                
                if (isAvailable) {
                    return "![Status](https://img.shields.io/badge/Status-Available-brightgreen)";
                } else if (!errorMessage.isEmpty()) {
                    // If there's a specific error, show it in the badge
                    String errorType = errorMessage.contains("timeout") ? "Timeout" : 
                                      errorMessage.contains("404") ? "Not Found" : "Error";
                    return "![Status](https://img.shields.io/badge/Status-" + errorType + "-red)";
                } else {
                    return "![Status](https://img.shields.io/badge/Status-Unavailable-red)";
                }
            }
        }
        return "![Status](https://img.shields.io/badge/Status-Unknown-yellow)";
    }

    private static String getVersionInfo(String provider) {
        if (javaStatus.has(provider)) {
            JSONObject providerInfo = javaStatus.getJSONObject(provider);
            if (providerInfo.has("version")) {
                String version = providerInfo.getString("version");
                if (!version.isEmpty()) {
                    // Format version string based on provider
                    if (version.startsWith("Java") || version.startsWith("OpenJDK") || 
                        version.startsWith("JDK") || version.startsWith("Oracle")) {
                        return "Current version: " + version;
                    } else {
                        // Add Java prefix if not present
                        return "Current version: Java " + version;
                    }
                }
            }
        }
        return "";
    }
}