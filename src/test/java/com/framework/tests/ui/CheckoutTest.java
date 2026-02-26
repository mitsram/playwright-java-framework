package com.framework.tests.ui;

import com.framework.base.BaseUITest;
import com.framework.pages.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * UI tests for the SauceDemo end-to-end checkout flow.
 */
public class CheckoutTest extends BaseUITest {

    private InventoryPage inventoryPage;

    @BeforeMethod(alwaysRun = true, dependsOnMethods = "setupTest")
    public void loginAndInit() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.open();
        loginPage.loginAs("standard_user", "secret_sauce");
        inventoryPage = new InventoryPage(page);
    }

    @Test(description = "Verify complete checkout flow end-to-end")
    public void testCompleteCheckoutFlow() {
        // Add items to cart
        inventoryPage
                .addItemToCartByName("sauce-labs-backpack")
                .addItemToCartByName("sauce-labs-bike-light");
        inventoryPage.clickShoppingCart();

        // Verify cart
        CartPage cartPage = new CartPage(page);
        assertThat(cartPage.getCartItemCount()).isEqualTo(2);
        assertThat(cartPage.getCartItemNames()).containsExactlyInAnyOrder(
                "Sauce Labs Backpack", "Sauce Labs Bike Light"
        );

        // Proceed to checkout
        cartPage.clickCheckout();

        // Fill checkout information
        CheckoutPage checkoutPage = new CheckoutPage(page);
        assertThat(checkoutPage.getTitle()).isEqualTo("Checkout: Your Information");

        checkoutPage.fillInformation("John", "Doe", "12345");
        checkoutPage.clickContinue();

        // Verify overview
        assertThat(checkoutPage.getTitle()).isEqualTo("Checkout: Overview");
        assertThat(checkoutPage.getSummaryItemCount()).isEqualTo(2);
        assertThat(checkoutPage.getSummaryTotal()).contains("Total:");

        // Finish order
        checkoutPage.clickFinish();

        // Verify completion
        assertThat(checkoutPage.getTitle()).isEqualTo("Checkout: Complete!");
        assertThat(checkoutPage.isOrderComplete()).isTrue();
        assertThat(checkoutPage.getCompleteHeader()).isEqualTo("Thank you for your order!");
    }

    @Test(description = "Verify checkout fails without filling required fields")
    public void testCheckoutRequiresInformation() {
        inventoryPage.addItemToCartByName("sauce-labs-backpack");
        inventoryPage.clickShoppingCart();

        CartPage cartPage = new CartPage(page);
        cartPage.clickCheckout();

        // Try to continue without filling info
        CheckoutPage checkoutPage = new CheckoutPage(page);
        checkoutPage.clickContinue();

        assertThat(checkoutPage.isErrorVisible()).isTrue();
        assertThat(checkoutPage.getErrorMessage()).contains("First Name is required");
    }

    @Test(description = "Verify cancel from checkout returns to cart")
    public void testCancelCheckout() {
        inventoryPage.addItemToCartByName("sauce-labs-backpack");
        inventoryPage.clickShoppingCart();

        CartPage cartPage = new CartPage(page);
        cartPage.clickCheckout();

        CheckoutPage checkoutPage = new CheckoutPage(page);
        checkoutPage.clickCancel();

        assertThat(page.url()).contains("/cart.html");
    }

    @Test(description = "Verify removing item from cart during checkout")
    public void testRemoveItemFromCart() {
        inventoryPage
                .addItemToCartByName("sauce-labs-backpack")
                .addItemToCartByName("sauce-labs-bike-light");
        inventoryPage.clickShoppingCart();

        CartPage cartPage = new CartPage(page);
        assertThat(cartPage.getCartItemCount()).isEqualTo(2);

        cartPage.removeItemByName("sauce-labs-backpack");
        assertThat(cartPage.getCartItemCount()).isEqualTo(1);
        assertThat(cartPage.getCartItemNames()).containsExactly("Sauce Labs Bike Light");
    }

    @Test(description = "Verify back to products from checkout complete")
    public void testBackToProductsAfterCheckout() {
        inventoryPage.addItemToCartByName("sauce-labs-backpack");
        inventoryPage.clickShoppingCart();

        new CartPage(page).clickCheckout();

        CheckoutPage checkoutPage = new CheckoutPage(page);
        checkoutPage.fillInformation("Jane", "Doe", "54321");
        checkoutPage.clickContinue();
        checkoutPage.clickFinish();
        checkoutPage.clickBackHome();

        assertThat(page.url()).contains("/inventory.html");
        assertThat(new InventoryPage(page).isLoaded()).isTrue();
    }
}
