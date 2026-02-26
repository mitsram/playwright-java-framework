package com.framework.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Wrapper around Playwright's APIResponse with convenience methods
 * for JSON parsing and assertion-friendly accessors.
 */
public class ApiResponseWrapper {

    private static final Logger log = LoggerFactory.getLogger(ApiResponseWrapper.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final APIResponse response;

    public ApiResponseWrapper(APIResponse response) {
        this.response = response;
    }

    // ── Status ──────────────────────────────────────────────────────

    public int statusCode() {
        return response.status();
    }

    public String statusText() {
        return response.statusText();
    }

    public boolean isOk() {
        return response.ok();
    }

    // ── Headers ─────────────────────────────────────────────────────

    public Map<String, String> headers() {
        return response.headers();
    }

    public String header(String name) {
        return response.headers().get(name.toLowerCase());
    }

    // ── Body ────────────────────────────────────────────────────────

    public String bodyAsString() {
        return new String(response.body());
    }

    public byte[] bodyAsBytes() {
        return response.body();
    }

    public JsonNode bodyAsJson() {
        try {
            return objectMapper.readTree(response.body());
        } catch (Exception e) {
            log.error("Failed to parse response body as JSON", e);
            throw new RuntimeException("Failed to parse response body as JSON", e);
        }
    }

    public <T> T bodyAs(Class<T> clazz) {
        try {
            return objectMapper.readValue(response.body(), clazz);
        } catch (Exception e) {
            log.error("Failed to deserialize response body to {}", clazz.getSimpleName(), e);
            throw new RuntimeException("Failed to deserialize response body", e);
        }
    }

    // ── Raw ─────────────────────────────────────────────────────────

    public APIResponse raw() {
        return response;
    }
}
