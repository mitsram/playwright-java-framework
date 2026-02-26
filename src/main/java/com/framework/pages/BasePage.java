package com.framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for all Page Objects.
 * Provides common UI interaction helpers and enforces Page Object pattern.
 */
public abstract class BasePage {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected final Page page;

    protected BasePage(Page page) {
        this.page = page;
    }

    // ── Navigation ──────────────────────────────────────────────────

    protected void navigateTo(String url) {
        log.info("Navigating to: {}", url);
        page.navigate(url);
    }

    protected String getCurrentUrl() {
        return page.url();
    }

    protected String getTitle() {
        return page.title();
    }

    // ── Element Interactions ────────────────────────────────────────

    protected void click(String selector) {
        log.debug("Click: {}", selector);
        page.locator(selector).click();
    }

    protected void fill(String selector, String text) {
        log.debug("Fill '{}' into: {}", text, selector);
        page.locator(selector).fill(text);
    }

    protected String getText(String selector) {
        return page.locator(selector).textContent();
    }

    protected String getInputValue(String selector) {
        return page.locator(selector).inputValue();
    }

    protected boolean isVisible(String selector) {
        return page.locator(selector).isVisible();
    }

    protected boolean isEnabled(String selector) {
        return page.locator(selector).isEnabled();
    }

    // ── Locator-based Interactions (preferred) ──────────────────────

    protected void click(Locator locator) {
        log.debug("Click: {}", locator);
        locator.click();
    }

    protected void fill(Locator locator, String text) {
        log.debug("Fill: {}", locator);
        locator.fill(text);
    }

    // ── Waits ───────────────────────────────────────────────────────

    protected void waitForSelector(String selector) {
        page.waitForSelector(selector);
    }

    protected void waitForSelectorVisible(String selector) {
        page.waitForSelector(selector, new Page.WaitForSelectorOptions()
                .setState(WaitForSelectorState.VISIBLE));
    }

    protected void waitForSelectorHidden(String selector) {
        page.waitForSelector(selector, new Page.WaitForSelectorOptions()
                .setState(WaitForSelectorState.HIDDEN));
    }

    protected void waitForNavigation(Runnable action) {
        action.run();
        page.waitForLoadState();
    }

    protected void waitForUrl(String urlPattern) {
        page.waitForURL(urlPattern);
    }

    // ── Screenshots ─────────────────────────────────────────────────

    protected byte[] takeScreenshot() {
        return page.screenshot();
    }

    protected byte[] takeScreenshot(String selector) {
        return page.locator(selector).screenshot();
    }
}
