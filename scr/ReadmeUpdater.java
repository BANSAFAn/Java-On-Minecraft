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
    private static final Map<String, String> TIMESTAMP_TEXTS = new HashMap<>();
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
        TIMESTAMP_TEXTS.put("en", "\n\n*Java download status last updated: " + currentDate + "*");
        TIMESTAMP_TEXTS.put("ru", "\n\n*Статус загрузки Java последний раз обновлялся: " + currentDate + "*");
        TIMESTAMP_TEXTS.put("ua", "\n\n*Статус завантаження Java останнє оновлення: " + currentDate + "*");
        TIMESTAMP_TEXTS.put("de", "\n\n*Java-Download-Status zuletzt aktualisiert: " + currentDate + "*");
        TIMESTAMP_TEXTS.put("zh", "\n\n*Java下载状态最后更新: " + currentDate + "*");
        TIMESTAMP_TEXTS.put("pl", "\n\n*Status pobierania Java ostatnio zaktualizowany: " + currentDate + "*");
    }

    public static void main(String[] args) {
        try {
            // Load Java status data
            String jsonContent = new String(Files.readAllBytes(Paths.get("java_status.json")), StandardCharsets.UTF_8);
            javaStatus = new JSONObject(jsonContent);

            // Update main README
            updateReadme("README.md", "en");

            // Update translated READMEs
            Map<String, String> readmeFiles = new HashMap<>();
            readmeFiles.put("README/README.ru.md", "ru");
            readmeFiles.put("README/README.ua.md", "ua");
            readmeFiles.put("README/README.de.md", "de");
            readmeFiles.put("README/README.zh.md", "zh");
            readmeFiles.put("README/README.pl.md", "pl");

            for (Map.Entry<String, String> entry : readmeFiles.entrySet()) {
                String filePath = entry.getKey();
                String lang = entry.getValue();
                File file = new File(filePath);
                if (file.exists()) {
                    updateReadme(filePath, lang);
                } else {
                    System.out.println("Warning: " + filePath + " not found");
                }
            }

            System.out.println("README files updated successfully");
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
                
                // Replace the status badge in the table
                Pattern pattern = Pattern.compile("\\| " + provider + " \\| .*? \\|");
                Matcher matcher = pattern.matcher(contentStr);
                if (matcher.find()) {
                    contentStr = matcher.replaceAll("| " + provider + " | " + statusBadge + " |");
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
                
                // Pattern to find the provider section and add status information
                Pattern pattern = Pattern.compile("(.*" + providerName + ".*\n.*Website:.*\n.*Description:.*)\n");
                Matcher matcher = pattern.matcher(contentStr);
                if (matcher.find()) {
                    contentStr = matcher.replaceAll("$1\n   - Status: " + statusBadge + " " + versionInfo + "\n");
                }
            }
        }

        // Add timestamp at the end
        contentStr += TIMESTAMP_TEXTS.getOrDefault(lang, TIMESTAMP_TEXTS.get("en"));

        // Write the updated content back to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, StandardCharsets.UTF_8))) {
            writer.write(contentStr);
        }
    }

    private static String getStatusBadge(String provider) {
        if (javaStatus.has(provider)) {
            JSONObject providerInfo = javaStatus.getJSONObject(provider);
            String status = providerInfo.optString("status", "unknown");
            
            if ("available".equals(status)) {
                return "![Available](https://img.shields.io/badge/status-available-brightgreen)";
            } else if ("unavailable".equals(status)) {
                return "![Unavailable](https://img.shields.io/badge/status-unavailable-red)";
            }
        }
        return "![Unknown](https://img.shields.io/badge/status-unknown-yellow)";
    }

    private static String getVersionInfo(String provider) {
        if (javaStatus.has(provider)) {
            JSONObject providerInfo = javaStatus.getJSONObject(provider);
            if (providerInfo.has("version")) {
                return "Current version: " + providerInfo.getString("version");
            }
        }
        return "";
    }
}