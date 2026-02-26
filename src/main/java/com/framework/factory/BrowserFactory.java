package com.framework.factory;

import com.framework.config.ConfigManager;
import com.microsoft.playwright.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;

/**
 * Factory for creating Playwright browser instances.
 * Supports chromium, firefox, and webkit.
 * Manages Playwright lifecycle (playwright → browser → context → page).
 */
public final class BrowserFactory {

    private static final Logger log = LoggerFactory.getLogger(BrowserFactory.class);

    private final ConfigManager config;

    public BrowserFactory() {
        this.config = ConfigManager.getInstance();
    }

    // ── Playwright Lifecycle ────────────────────────────────────────

    public Playwright createPlaywright() {
        return Playwright.create();
    }

    public Browser createBrowser(Playwright playwright) {
        String browserType = config.getBrowser().toLowerCase();
        boolean headless = config.isHeadless();
        double slowMo = config.getSlowMo();

        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                .setHeadless(headless)
                .setSlowMo(slowMo);

        log.info("Launching browser: {} (headless={}, slowMo={})", browserType, headless, slowMo);

        return switch (browserType) {
            case "firefox" -> playwright.firefox().launch(options);
            case "webkit" -> playwright.webkit().launch(options);
            default -> playwright.chromium().launch(options);
        };
    }

    public BrowserContext createContext(Browser browser) {
        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions()
                .setViewportSize(
                        config.getInt("viewport.width", 1920),
                        config.getInt("viewport.height", 1080)
                );

        // Video recording
        if (config.getBoolean("video.enabled", false)) {
            contextOptions.setRecordVideoDir(Paths.get("videos/"));
        }

        BrowserContext context = browser.newContext(contextOptions);
        context.setDefaultTimeout(config.getDefaultTimeout());

        // Tracing
        if (config.isTracingEnabled()) {
            context.tracing().start(new Tracing.StartOptions()
                    .setScreenshots(true)
                    .setSnapshots(true)
                    .setSources(true));
            log.info("Tracing enabled");
        }

        return context;
    }

    public Page createPage(BrowserContext context) {
        return context.newPage();
    }

    // ── Cleanup ─────────────────────────────────────────────────────

    public void stopTracing(BrowserContext context, String testName) {
        if (config.isTracingEnabled()) {
            context.tracing().stop(new Tracing.StopOptions()
                    .setPath(Paths.get("playwright-traces/" + testName + ".zip")));
            log.info("Trace saved for test: {}", testName);
        }
    }
}
