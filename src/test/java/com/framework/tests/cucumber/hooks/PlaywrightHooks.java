package com.framework.tests.cucumber.hooks;

import com.framework.config.ConfigManager;
import com.framework.factory.BrowserFactory;
import com.microsoft.playwright.*;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;

/**
 * Cucumber hooks for Playwright lifecycle management.
 * Provides the same browser lifecycle as BaseUITest but for Cucumber scenarios.
 */
public class PlaywrightHooks {

    private static final Logger log = LoggerFactory.getLogger(PlaywrightHooks.class);

    // Shared across scenarios (thread-safe for parallel execution considerations)
    private static Playwright playwright;
    private static Browser browser;
    private static final BrowserFactory browserFactory = new BrowserFactory();

    // Per-scenario
    private BrowserContext context;
    private Page page;

    static {
        playwright = browserFactory.createPlaywright();
        browser = browserFactory.createBrowser(playwright);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (browser != null) browser.close();
            if (playwright != null) playwright.close();
        }));
    }

    @Before
    public void setUp(Scenario scenario) {
        context = browserFactory.createContext(browser);
        page = browserFactory.createPage(context);
        log.info("Cucumber scenario started: {}", scenario.getName());
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed() && page != null) {
            byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
            scenario.attach(screenshot, "image/png", scenario.getName());
            log.info("Screenshot attached for failed scenario: {}", scenario.getName());
        }

        browserFactory.stopTracing(context, scenario.getName().replaceAll("\\s+", "_"));

        if (context != null) context.close();
        log.info("Cucumber scenario ended: {}", scenario.getName());
    }

    public Page getPage() {
        return page;
    }

    public BrowserContext getContext() {
        return context;
    }
}
