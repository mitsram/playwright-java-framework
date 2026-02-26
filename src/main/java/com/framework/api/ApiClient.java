package com.framework.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.config.ConfigManager;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Lightweight API client built on top of Playwright's APIRequestContext.
 * Provides fluent methods for REST calls with automatic base URL resolution.
 */
public class ApiClient {

    private static final Logger log = LoggerFactory.getLogger(ApiClient.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final APIRequestContext requestContext;
    private final String baseUrl;

    public ApiClient(APIRequestContext requestContext) {
        this.requestContext = requestContext;
        this.baseUrl = ConfigManager.getInstance().getApiBaseUrl();
    }

    public ApiClient(APIRequestContext requestContext, String baseUrl) {
        this.requestContext = requestContext;
        this.baseUrl = baseUrl;
    }

    // ── GET ─────────────────────────────────────────────────────────

    public ApiResponseWrapper get(String endpoint) {
        return get(endpoint, null);
    }

    public ApiResponseWrapper get(String endpoint, Map<String, String> queryParams) {
        String url = resolveUrl(endpoint);
        log.info("GET {}", url);

        RequestOptions options = RequestOptions.create();
        if (queryParams != null) {
            queryParams.forEach(options::setQueryParam);
        }

        APIResponse response = requestContext.get(url, options);
        return new ApiResponseWrapper(response);
    }

    // ── POST ────────────────────────────────────────────────────────

    public ApiResponseWrapper post(String endpoint, Object body) {
        String url = resolveUrl(endpoint);
        log.info("POST {}", url);

        RequestOptions options = RequestOptions.create();
        if (body != null) {
            options.setData(serialize(body));
        }

        APIResponse response = requestContext.post(url, options);
        return new ApiResponseWrapper(response);
    }

    public ApiResponseWrapper post(String endpoint) {
        return post(endpoint, null);
    }

    // ── PUT ─────────────────────────────────────────────────────────

    public ApiResponseWrapper put(String endpoint, Object body) {
        String url = resolveUrl(endpoint);
        log.info("PUT {}", url);

        RequestOptions options = RequestOptions.create();
        if (body != null) {
            options.setData(serialize(body));
        }

        APIResponse response = requestContext.put(url, options);
        return new ApiResponseWrapper(response);
    }

    // ── PATCH ───────────────────────────────────────────────────────

    public ApiResponseWrapper patch(String endpoint, Object body) {
        String url = resolveUrl(endpoint);
        log.info("PATCH {}", url);

        RequestOptions options = RequestOptions.create();
        if (body != null) {
            options.setData(serialize(body));
        }

        APIResponse response = requestContext.patch(url, options);
        return new ApiResponseWrapper(response);
    }

    // ── DELETE ───────────────────────────────────────────────────────

    public ApiResponseWrapper delete(String endpoint) {
        String url = resolveUrl(endpoint);
        log.info("DELETE {}", url);

        APIResponse response = requestContext.delete(url);
        return new ApiResponseWrapper(response);
    }

    // ── Internal ────────────────────────────────────────────────────

    private String resolveUrl(String endpoint) {
        if (endpoint.startsWith("http")) {
            return endpoint;
        }
        return baseUrl + (endpoint.startsWith("/") ? endpoint : "/" + endpoint);
    }

    private String serialize(Object body) {
        if (body instanceof String) {
            return (String) body;
        }
        try {
            return objectMapper.writeValueAsString(body);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize request body", e);
        }
    }
}
