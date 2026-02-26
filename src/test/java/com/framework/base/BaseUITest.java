package com.framework.base;

import com.framework.config.ConfigManager;
import com.framework.factory.BrowserFactory;
import com.microsoft.playwright.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.nio.file.Paths;

/**
 * Base class for all UI tests.
 * Manages Playwright browser lifecycle and provides page instances.
 * <p>
 * Playwright is NOT thread-safe — each test class gets its own
 * Playwright + Browser instance so parallel execution works safely.
 * <p>
 * Lifecycle:
 * - @BeforeClass  → Playwright + Browser (one per class / thread)
 * - @BeforeMethod → BrowserContext + Page (clean state per test)
 * - @AfterMethod  → Cleanup context, save traces/screenshots on failure
 * - @AfterClass   → Close browser + Playwright
 */
public abstract class BaseUITest {

    protected static final Logger log = LoggerFactory.getLogger(BaseUITest.class);
    protected ConfigManager config;

    private Playwright playwright;
    private Browser browser;
    private BrowserFactory browserFactory;

    protected BrowserContext context;
    protected Page page;

    @BeforeClass(alwaysRun = true)
    public void setupBrowser() {
        config = ConfigManager.getInstance();
        browserFactory = new BrowserFactory();
        playwright = browserFactory.createPlaywright();
        browser = browserFactory.createBrowser(playwright);
        log.info("Browser setup complete — {} launched", config.getBrowser());
    }

    @BeforeMethod(alwaysRun = true)
    public void setupTest() {
        context = browserFactory.createContext(browser);
        page = browserFactory.createPage(context);
        log.info("Test setup complete — new context and page created");
    }

    @AfterMethod(alwaysRun = true)
    public void teardownTest(ITestResult result) {
        String testName = result.getMethod().getMethodName();

        // Screenshot on failure
        if (result.getStatus() == ITestResult.FAILURE && page != null) {
            try {
                page.screenshot(new Page.ScreenshotOptions()
                        .setPath(Paths.get("screenshots/" + testName + ".png"))
                        .setFullPage(true));
                log.info("Screenshot saved for failed test: {}", testName);
            } catch (Exception e) {
                log.warn("Failed to capture screenshot for test: {}", testName, e);
            }
        }

        // Save tracing
        if (context != null) {
            try {
                browserFactory.stopTracing(context, testName);
            } catch (Exception e) {
                log.warn("Failed to stop tracing for test: {}", testName, e);
            }
        }

        // Cleanup
        if (context != null) {
            try {
                context.close();
            } catch (Exception e) {
                log.warn("Failed to close context for test: {}", testName, e);
            }
        }
        log.info("Test teardown complete — context closed");
    }

    @AfterClass(alwaysRun = true)
    public void teardownBrowser() {
        if (browser != null) {
            try { browser.close(); } catch (Exception e) { log.warn("Error closing browser", e); }
        }
        if (playwright != null) {
            try { playwright.close(); } catch (Exception e) { log.warn("Error closing playwright", e); }
        }
        log.info("Browser teardown complete — browser and playwright closed");
    }
}
