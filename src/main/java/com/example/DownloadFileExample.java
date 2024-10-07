package com.example;

import javax.net.ssl.*;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class DownloadFileExample {
    public static void trustAllCertificates() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[] {
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() { return null; }
                public void checkClientTrusted(X509Certificate[] certs, String authType) { }
                public void checkServerTrusted(X509Certificate[] certs, String authType) { }
            }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) { return true; }
        });
    }

    public static void download(String downloadURL) throws Exception {
        trustAllCertificates(); // chiamata alla funzione per fidarsi di tutti i certificati
        URL website = new URL(downloadURL);
        String fileName = "downloaded.zip";
        try (InputStream inputStream = website.openStream()) {
            Files.copy(inputStream, Paths.get(fileName), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public static void main(String[] arguments) throws Exception {
        String downloadURL = "https://mh-nexus.de/downloads/HxDSetupEN.zip";
        download(downloadURL);
    }
}
