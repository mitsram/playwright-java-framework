package com.framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.List;

/**
 * Page Object for the SauceDemo Inventory (Products) page.
 * URL: /inventory.html
 */
public class InventoryPage extends BasePage {

    // ── Locators ────────────────────────────────────────────────────

    private final Locator title;
    private final Locator inventoryItems;
    private final Locator shoppingCartBadge;
    private final Locator shoppingCartLink;
    private final Locator sortDropdown;
    private final Locator burgerMenuButton;
    private final Locator logoutLink;

    public InventoryPage(Page page) {
        super(page);
        this.title = page.locator("[data-test='title']");
        this.inventoryItems = page.locator("[data-test='inventory-item']");
        this.shoppingCartBadge = page.locator("[data-test='shopping-cart-badge']");
        this.shoppingCartLink = page.locator("[data-test='shopping-cart-link']");
        this.sortDropdown = page.locator("[data-test='product-sort-container']");
        this.burgerMenuButton = page.locator("#react-burger-menu-btn");
        this.logoutLink = page.locator("[data-test='logout-sidebar-link']");
    }

    // ── Actions ─────────────────────────────────────────────────────

    public InventoryPage addItemToCartByName(String itemName) {
        String buttonId = itemName.toLowerCase().replace(" ", "-");
        page.locator("[data-test='add-to-cart-" + buttonId + "']").click();
        return this;
    }

    public InventoryPage removeItemFromCartByName(String itemName) {
        String buttonId = itemName.toLowerCase().replace(" ", "-");
        page.locator("[data-test='remove-" + buttonId + "']").click();
        return this;
    }

    public void clickShoppingCart() {
        click(shoppingCartLink);
    }

    public InventoryPage sortBy(String option) {
        sortDropdown.selectOption(option);
        return this;
    }

    public void logout() {
        click(burgerMenuButton);
        click(logoutLink);
    }

    // ── State ───────────────────────────────────────────────────────

    public String getTitle() {
        return title.textContent();
    }

    public int getInventoryItemCount() {
        return inventoryItems.count();
    }

    public int getCartBadgeCount() {
        if (shoppingCartBadge.isVisible()) {
            return Integer.parseInt(shoppingCartBadge.textContent().trim());
        }
        return 0;
    }

    public boolean isLoaded() {
        return title.isVisible();
    }

    public List<String> getItemNames() {
        return page.locator("[data-test='inventory-item-name']").allTextContents();
    }

    public List<String> getItemPrices() {
        return page.locator("[data-test='inventory-item-price']").allTextContents();
    }

    public void clickItemByName(String itemName) {
        page.locator("[data-test='inventory-item-name']").getByText(itemName).click();
    }
}
