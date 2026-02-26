package com.framework.pages;

import com.framework.config.ConfigManager;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

/**
 * Page Object for the SauceDemo Login page (https://www.saucedemo.com).
 */
public class LoginPage extends BasePage {

    // ── Locators ────────────────────────────────────────────────────

    private final Locator usernameInput;
    private final Locator passwordInput;
    private final Locator loginButton;
    private final Locator errorMessage;
    private final Locator loginLogo;

    public LoginPage(Page page) {
        super(page);
        this.usernameInput = page.locator("[data-test='username']");
        this.passwordInput = page.locator("[data-test='password']");
        this.loginButton = page.locator("[data-test='login-button']");
        this.errorMessage = page.locator("[data-test='error']");
        this.loginLogo = page.locator(".login_logo");
    }

    // ── Actions ─────────────────────────────────────────────────────

    public LoginPage open() {
        navigateTo(ConfigManager.getInstance().getBaseUrl());
        return this;
    }

    public LoginPage enterUsername(String username) {
        fill(usernameInput, username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        fill(passwordInput, password);
        return this;
    }

    public void clickLogin() {
        click(loginButton);
    }

    /** Fluent login combining all steps. */
    public void loginAs(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    // ── State ───────────────────────────────────────────────────────

    public String getErrorMessage() {
        return errorMessage.textContent();
    }

    public boolean isErrorVisible() {
        return errorMessage.isVisible();
    }

    public boolean isLogoVisible() {
        return loginLogo.isVisible();
    }

    public String getLogoText() {
        return loginLogo.textContent();
    }
}
