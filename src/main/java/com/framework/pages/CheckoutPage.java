package com.framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

/**
 * Page Object for the SauceDemo Checkout pages.
 * Covers Step One (info), Step Two (overview), and Complete.
 */
public class CheckoutPage extends BasePage {

    // ── Locators ────────────────────────────────────────────────────

    // Step One - Your Information
    private final Locator firstNameInput;
    private final Locator lastNameInput;
    private final Locator postalCodeInput;
    private final Locator continueButton;
    private final Locator cancelButton;
    private final Locator errorMessage;

    // Step Two - Overview
    private final Locator finishButton;
    private final Locator summaryTotal;
    private final Locator summaryItems;

    // Complete
    private final Locator completeHeader;
    private final Locator completeText;
    private final Locator backHomeButton;

    // Shared
    private final Locator title;

    public CheckoutPage(Page page) {
        super(page);
        // Step One
        this.firstNameInput = page.locator("[data-test='firstName']");
        this.lastNameInput = page.locator("[data-test='lastName']");
        this.postalCodeInput = page.locator("[data-test='postalCode']");
        this.continueButton = page.locator("[data-test='continue']");
        this.cancelButton = page.locator("[data-test='cancel']");
        this.errorMessage = page.locator("[data-test='error']");

        // Step Two
        this.finishButton = page.locator("[data-test='finish']");
        this.summaryTotal = page.locator("[data-test='total-label']");
        this.summaryItems = page.locator("[data-test='inventory-item']");

        // Complete
        this.completeHeader = page.locator("[data-test='complete-header']");
        this.completeText = page.locator("[data-test='complete-text']");
        this.backHomeButton = page.locator("[data-test='back-to-products']");

        // Shared
        this.title = page.locator("[data-test='title']");
    }

    // ── Step One Actions ────────────────────────────────────────────

    public CheckoutPage fillFirstName(String firstName) {
        fill(firstNameInput, firstName);
        return this;
    }

    public CheckoutPage fillLastName(String lastName) {
        fill(lastNameInput, lastName);
        return this;
    }

    public CheckoutPage fillPostalCode(String postalCode) {
        fill(postalCodeInput, postalCode);
        return this;
    }

    public CheckoutPage fillInformation(String firstName, String lastName, String postalCode) {
        fillFirstName(firstName);
        fillLastName(lastName);
        fillPostalCode(postalCode);
        return this;
    }

    public void clickContinue() {
        click(continueButton);
    }

    public void clickCancel() {
        click(cancelButton);
    }

    // ── Step Two Actions ────────────────────────────────────────────

    public void clickFinish() {
        click(finishButton);
    }

    // ── Complete Actions ────────────────────────────────────────────

    public void clickBackHome() {
        click(backHomeButton);
    }

    // ── State ───────────────────────────────────────────────────────

    public String getTitle() {
        return title.textContent();
    }

    public String getErrorMessage() {
        return errorMessage.textContent();
    }

    public boolean isErrorVisible() {
        return errorMessage.isVisible();
    }

    public String getSummaryTotal() {
        return summaryTotal.textContent();
    }

    public int getSummaryItemCount() {
        return summaryItems.count();
    }

    public String getCompleteHeader() {
        return completeHeader.textContent();
    }

    public String getCompleteText() {
        return completeText.textContent();
    }

    public boolean isOrderComplete() {
        return completeHeader.isVisible();
    }
}
