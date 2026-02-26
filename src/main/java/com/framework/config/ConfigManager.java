package com.framework.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Centralized configuration manager that loads environment-specific properties.
 * <p>
 * Resolution order (highest priority first):
 * 1. System properties (-Dkey=value)
 * 2. Environment variables
 * 3. Environment-specific properties file (e.g., dev.properties)
 * 4. Default values
 */
public final class ConfigManager {

    private static final Logger log = LoggerFactory.getLogger(ConfigManager.class);
    private static final String DEFAULT_ENV = "dev";
    private static ConfigManager instance;
    private final Properties properties;
    private final String environment;

    private ConfigManager() {
        this.environment = resolveEnvironment();
        this.properties = new Properties();
        loadProperties();
        log.info("Configuration loaded for environment: {}", environment);
    }

    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    /** Reset for testing purposes. */
    public static synchronized void reset() {
        instance = null;
    }

    // ── Property Accessors ──────────────────────────────────────────

    public String get(String key) {
        // System property > env var > properties file
        String value = System.getProperty(key);
        if (value != null) return value;

        value = System.getenv(key);
        if (value != null) return value;

        return properties.getProperty(key);
    }

    public String get(String key, String defaultValue) {
        String value = get(key);
        return value != null ? value : defaultValue;
    }

    public int getInt(String key, int defaultValue) {
        String value = get(key);
        if (value == null) return defaultValue;
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            log.warn("Invalid integer for key '{}': '{}'. Using default: {}", key, value, defaultValue);
            return defaultValue;
        }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String value = get(key);
        if (value == null) return defaultValue;
        return Boolean.parseBoolean(value.trim());
    }

    public String getEnvironment() {
        return environment;
    }

    // ── Convenience Accessors ───────────────────────────────────────

    public String getBaseUrl() {
        return get("base.url", "http://localhost");
    }

    public String getApiBaseUrl() {
        return get("api.base.url", "http://localhost/api");
    }

    public String getBrowser() {
        return get("browser", "chromium");
    }

    public boolean isHeadless() {
        return getBoolean("headless", true);
    }

    public boolean isTracingEnabled() {
        return getBoolean("tracing.enabled", false);
    }

    public int getDefaultTimeout() {
        return getInt("default.timeout", 30000);
    }

    public double getSlowMo() {
        return Double.parseDouble(get("slow.mo", "0"));
    }

    // ── Internal ────────────────────────────────────────────────────

    private String resolveEnvironment() {
        String env = System.getProperty("env");
        if (env == null) env = System.getenv("ENV");
        if (env == null) env = DEFAULT_ENV;
        return env.toLowerCase().trim();
    }

    private void loadProperties() {
        String path = "config/" + environment + ".properties";
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
            if (is != null) {
                properties.load(is);
                log.debug("Loaded properties from {}", path);
            } else {
                log.warn("Properties file not found: {}. Using defaults and system properties.", path);
            }
        } catch (IOException e) {
            log.error("Error loading properties from {}", path, e);
        }
    }
}
