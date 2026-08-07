package com.spms.config;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Loads key=value pairs from a local .env file into system properties
 * so Spring can resolve ${EMAIL_USER}, ${JWT_SECRET}, etc.
 * Does not override variables that are already set in the OS/IDE.
 * Later lines in .env override earlier lines (last value wins).
 */
public final class DotEnvLoader {

    private DotEnvLoader() {
    }

    public static void load() {
        Path envFile = Paths.get(".env");
        if (!Files.exists(envFile)) {
            envFile = Paths.get(System.getProperty("user.dir"), ".env");
        }
        if (!Files.exists(envFile)) {
            System.out.println("No .env file found — using OS/IDE environment variables only.");
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(envFile)) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                int eq = line.indexOf('=');
                if (eq <= 0) {
                    continue;
                }
                String key = line.substring(0, eq).trim();
                String value = line.substring(eq + 1).trim();

                // OS / IDE env vars always win
                if (System.getenv(key) != null) {
                    continue;
                }

                // Last value in .env wins (so real values after placeholders work)
                System.setProperty(key, value);
            }
            System.out.println("Loaded environment from .env");
        } catch (Exception ex) {
            System.err.println("Failed to load .env: " + ex.getMessage());
        }
    }
}
