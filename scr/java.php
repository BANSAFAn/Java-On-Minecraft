<?php

function getJavaVersion($url, $provider) {
    $ch = curl_init();

    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true);
    
    $html = curl_exec($ch);
    curl_close($ch);

    if ($html === false) {
        return "Error fetching data from $provider.";
    }
    switch ($provider) {
        case "Adoptium":
            preg_match('/<div class="release-versions">.*?<a.*?>(.*?)<\/a>/s', $html, $matches);
            return $matches[1] ?? "Version not found";
        case "Oracle":
            preg_match('/<a.*?jdk-17.*?>(.*?)<\/a>/s', $html, $matches);
            return $matches[1] ?? "Version not found";
        case "Amazon Corretto":
            preg_match('/<td>(.*?)<\/td>/s', $html, $matches);
            return $matches[1] ?? "Version not found";
        case "Azul Zulu":
            preg_match('/<span class="version-string">(.*?)<\/span>/s', $html, $matches);
            return $matches[1] ?? "Version not found";
        case "Red Hat OpenJDK":
        case "Microsoft OpenJDK":
            return "Version info not available, please check manually";
        default:
            return "Unknown provider";
    }
}

$sites = [
    "Adoptium" => "https://adoptium.net/temurin/releases/?version=17",
    "Oracle" => "https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html",
    "Amazon Corretto" => "https://docs.aws.amazon.com/corretto/latest/corretto-17-ug/downloads-list.html",
    "Azul Zulu" => "https://www.azul.com/downloads/?version=java-17-lts",
    "Red Hat OpenJDK" => "https://developers.redhat.com/products/openjdk/download",
    "Microsoft OpenJDK" => "https://learn.microsoft.com/en-us/java/openjdk/download"
];

foreach ($sites as $provider => $url) {
    $version = getJavaVersion($url, $provider);
    echo "$provider: $version\n";
}
?>