package com.framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.List;

/**
 * Page Object for the SauceDemo Cart page.
 * URL: /cart.html
 */
public class CartPage extends BasePage {

    // ── Locators ────────────────────────────────────────────────────

    private final Locator title;
    private final Locator cartItems;
    private final Locator checkoutButton;
    private final Locator continueShoppingButton;

    public CartPage(Page page) {
        super(page);
        this.title = page.locator("[data-test='title']");
        this.cartItems = page.locator("[data-test='inventory-item']");
        this.checkoutButton = page.locator("[data-test='checkout']");
        this.continueShoppingButton = page.locator("[data-test='continue-shopping']");
    }

    // ── Actions ─────────────────────────────────────────────────────

    public void clickCheckout() {
        click(checkoutButton);
    }

    public void clickContinueShopping() {
        click(continueShoppingButton);
    }

    public CartPage removeItemByName(String itemName) {
        String buttonId = itemName.toLowerCase().replace(" ", "-");
        page.locator("[data-test='remove-" + buttonId + "']").click();
        return this;
    }

    // ── State ───────────────────────────────────────────────────────

    public String getTitle() {
        return title.textContent();
    }

    public int getCartItemCount() {
        return cartItems.count();
    }

    public List<String> getCartItemNames() {
        return page.locator("[data-test='inventory-item-name']").allTextContents();
    }

    public List<String> getCartItemPrices() {
        return page.locator("[data-test='inventory-item-price']").allTextContents();
    }

    public boolean isLoaded() {
        return title.isVisible() && title.textContent().contains("Your Cart");
    }
}
