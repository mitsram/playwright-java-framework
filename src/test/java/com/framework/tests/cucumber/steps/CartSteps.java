package com.framework.tests.cucumber.steps;

import com.framework.pages.CartPage;
import com.framework.pages.InventoryPage;
import com.framework.tests.cucumber.hooks.PlaywrightHooks;
import io.cucumber.java.en.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Step definitions for the SauceDemo Cart feature.
 */
public class CartSteps {

    private final PlaywrightHooks hooks;
    private InventoryPage inventoryPage;
    private CartPage cartPage;

    public CartSteps(PlaywrightHooks hooks) {
        this.hooks = hooks;
    }

    @When("I add {string} to the cart")
    public void iAddItemToTheCart(String itemName) {
        inventoryPage = new InventoryPage(hooks.getPage());
        inventoryPage.addItemToCartByName(itemName);
    }

    @When("I remove {string} from the cart")
    public void iRemoveItemFromTheCart(String itemName) {
        inventoryPage = new InventoryPage(hooks.getPage());
        inventoryPage.removeItemFromCartByName(itemName);
    }

    @When("I navigate to the cart page")
    public void iNavigateToTheCartPage() {
        inventoryPage = new InventoryPage(hooks.getPage());
        inventoryPage.clickShoppingCart();
        cartPage = new CartPage(hooks.getPage());
    }

    @Then("the cart badge should show {int} item(s)")
    public void theCartBadgeShouldShow(int expectedCount) {
        inventoryPage = new InventoryPage(hooks.getPage());
        assertThat(inventoryPage.getCartBadgeCount()).isEqualTo(expectedCount);
    }

    @Then("the cart should contain {int} item(s)")
    public void theCartShouldContainItems(int expectedCount) {
        cartPage = new CartPage(hooks.getPage());
        assertThat(cartPage.getCartItemCount()).isEqualTo(expectedCount);
    }

    @Then("the cart should contain {string}")
    public void theCartShouldContainItem(String itemName) {
        cartPage = new CartPage(hooks.getPage());
        assertThat(cartPage.getCartItemNames()).contains(itemName);
    }
}
