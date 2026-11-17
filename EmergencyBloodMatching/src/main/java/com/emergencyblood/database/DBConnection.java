
package com.emergencyblood.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DBConnection {

    private static String url = System.getenv("DB_URL");
    private static String user = System.getenv("DB_USER");
    private static String password = System.getenv("DB_PASSWORD");

    static {
        loadEnvIfMissing();
    }

    private static void loadEnvIfMissing() {
        if (url != null && user != null && password != null) {
            return;
        }

        File[] candidateFiles = new File[] {
            new File(".env"),
            new File("EmergencyBloodMatching/.env"),
            new File("../.env")
        };

        for (File envFile : candidateFiles) {
            if (envFile.exists() && envFile.isFile()) {
                Map<String, String> envMap = parseEnvFile(envFile);
                if (url == null || url.trim().isEmpty()) {
                    url = envMap.get("DB_URL");
                }
                if (user == null || user.trim().isEmpty()) {
                    user = envMap.get("DB_USER");
                }
                if (password == null || password.trim().isEmpty()) {
                    password = envMap.get("DB_PASSWORD");
                }
                if (url != null && user != null && password != null) {
                    break;
                }
            }
        }
    }

    private static Map<String, String> parseEnvFile(File file) {
        Map<String, String> env = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                int eqIdx = line.indexOf('=');
                if (eqIdx != -1) {
                    String key = line.substring(0, eqIdx).trim();
                    String val = line.substring(eqIdx + 1).trim();
                    if ((val.startsWith("\"") && val.endsWith("\"")) || (val.startsWith("'") && val.endsWith("'"))) {
                        val = val.substring(1, val.length() - 1);
                    }
                    env.put(key, val);
                }
            }
        } catch (IOException ignored) {
        }
        return env;
    }

    public static Connection getConnection() throws SQLException {
        if (url == null || user == null || password == null) {
            loadEnvIfMissing();
        }

        if (url == null || user == null || password == null) {
            throw new IllegalStateException(
                "Database environment variables are missing (DB_URL, DB_USER, DB_PASSWORD)."
            );
        }

        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Cleans up MySQL Connector/J background cleanup thread and deregisters JDBC drivers.
     * Useful for standalone tests, CLI runners, or JVM shutdown hooks to prevent thread leaks.
     */
    public static void cleanup() {
        try {
            com.mysql.cj.jdbc.AbandonedConnectionCleanupThread.checkedShutdown();
        } catch (Throwable t) {
            try {
                com.mysql.cj.jdbc.AbandonedConnectionCleanupThread.uncheckedShutdown();
            } catch (Throwable ignored) {
            }
        }

        try {
            java.util.Enumeration<java.sql.Driver> drivers = java.sql.DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                java.sql.Driver driver = drivers.nextElement();
                try {
                    java.sql.DriverManager.deregisterDriver(driver);
                } catch (java.sql.SQLException ignored) {
                }
            }
        } catch (Throwable ignored) {
        }
    }
}
