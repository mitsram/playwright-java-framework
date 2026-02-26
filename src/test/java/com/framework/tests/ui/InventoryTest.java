package com.framework.tests.ui;

import com.framework.base.BaseUITest;
import com.framework.pages.CartPage;
import com.framework.pages.InventoryPage;
import com.framework.pages.LoginPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * UI tests for the SauceDemo Inventory (Products) page.
 */
public class InventoryTest extends BaseUITest {

    private InventoryPage inventoryPage;

    @BeforeMethod(alwaysRun = true, dependsOnMethods = "setupTest")
    public void loginAndInit() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.open();
        loginPage.loginAs("standard_user", "secret_sauce");
        inventoryPage = new InventoryPage(page);
    }

    @Test(description = "Verify inventory page displays 6 products")
    public void testInventoryDisplaysProducts() {
        assertThat(inventoryPage.isLoaded()).isTrue();
        assertThat(inventoryPage.getInventoryItemCount()).isEqualTo(6);
    }

    @Test(description = "Verify adding a single item to cart")
    public void testAddSingleItemToCart() {
        inventoryPage.addItemToCartByName("sauce-labs-backpack");

        assertThat(inventoryPage.getCartBadgeCount()).isEqualTo(1);
    }

    @Test(description = "Verify adding multiple items to cart")
    public void testAddMultipleItemsToCart() {
        inventoryPage
                .addItemToCartByName("sauce-labs-backpack")
                .addItemToCartByName("sauce-labs-bike-light")
                .addItemToCartByName("sauce-labs-bolt-t-shirt");

        assertThat(inventoryPage.getCartBadgeCount()).isEqualTo(3);
    }

    @Test(description = "Verify removing an item from cart on inventory page")
    public void testRemoveItemFromCart() {
        inventoryPage.addItemToCartByName("sauce-labs-backpack");
        assertThat(inventoryPage.getCartBadgeCount()).isEqualTo(1);

        inventoryPage.removeItemFromCartByName("sauce-labs-backpack");
        assertThat(inventoryPage.getCartBadgeCount()).isEqualTo(0);
    }

    @Test(description = "Verify sorting products by name A-Z")
    public void testSortByNameAZ() {
        inventoryPage.sortBy("az");

        List<String> names = inventoryPage.getItemNames();
        assertThat(names).isSorted();
    }

    @Test(description = "Verify sorting products by name Z-A")
    public void testSortByNameZA() {
        inventoryPage.sortBy("za");

        List<String> names = inventoryPage.getItemNames();
        assertThat(names).isSortedAccordingTo((a, b) -> b.compareTo(a));
    }

    @Test(description = "Verify sorting products by price low to high")
    public void testSortByPriceLowToHigh() {
        inventoryPage.sortBy("lohi");

        List<String> prices = inventoryPage.getItemPrices();
        List<Double> numericPrices = prices.stream()
                .map(p -> Double.parseDouble(p.replace("$", "")))
                .toList();
        assertThat(numericPrices).isSorted();
    }

    @Test(description = "Verify navigating to cart from inventory")
    public void testNavigateToCart() {
        inventoryPage.addItemToCartByName("sauce-labs-backpack");
        inventoryPage.clickShoppingCart();

        CartPage cartPage = new CartPage(page);
        assertThat(cartPage.isLoaded()).isTrue();
        assertThat(cartPage.getCartItemCount()).isEqualTo(1);
    }

    @Test(description = "Verify logout from inventory page")
    public void testLogout() {
        inventoryPage.logout();

        assertThat(page.url()).isEqualTo(config.getBaseUrl() + "/");
    }
}
