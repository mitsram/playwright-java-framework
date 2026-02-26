package com.framework.base;

import com.framework.api.ApiClient;
import com.framework.config.ConfigManager;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

import java.util.Map;

/**
 * Base class for all API tests.
 * Manages Playwright API request context lifecycle.
 * <p>
 * Lifecycle:
 * - @BeforeSuite  → Playwright + API context
 * - @AfterSuite   → Cleanup
 */
public abstract class BaseAPITest {

    protected static final Logger log = LoggerFactory.getLogger(BaseAPITest.class);
    protected ConfigManager config;

    private Playwright playwright;
    private APIRequestContext requestContext;
    protected ApiClient apiClient;

    @BeforeClass(alwaysRun = true)
    public void setupApiClient() {
        config = ConfigManager.getInstance();
        playwright = Playwright.create();

        requestContext = playwright.request().newContext(
                new APIRequest.NewContextOptions()
                        .setBaseURL(config.getApiBaseUrl())
                        .setExtraHTTPHeaders(Map.of(
                                "Content-Type", "application/json",
                                "Accept", "application/json"
                        ))
        );

        apiClient = new ApiClient(requestContext);
        log.info("API test setup complete — base URL: {}", config.getApiBaseUrl());
    }

    @AfterClass(alwaysRun = true)
    public void teardownApiClient() {
        if (requestContext != null) {
            try { requestContext.dispose(); } catch (Exception e) { log.warn("Error disposing request context", e); }
        }
        if (playwright != null) {
            try { playwright.close(); } catch (Exception e) { log.warn("Error closing playwright", e); }
        }
        log.info("API test teardown complete");
    }
}
