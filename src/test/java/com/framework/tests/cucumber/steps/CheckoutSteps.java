package com.framework.tests.cucumber.steps;

import com.framework.pages.CheckoutPage;
import com.framework.pages.InventoryPage;
import com.framework.tests.cucumber.hooks.PlaywrightHooks;
import io.cucumber.java.en.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Step definitions for the SauceDemo Checkout feature.
 */
public class CheckoutSteps {

    private final PlaywrightHooks hooks;
    private CheckoutPage checkoutPage;

    public CheckoutSteps(PlaywrightHooks hooks) {
        this.hooks = hooks;
    }

    @When("I proceed to checkout")
    public void iProceedToCheckout() {
        new com.framework.pages.CartPage(hooks.getPage()).clickCheckout();
        checkoutPage = new CheckoutPage(hooks.getPage());
    }

    @When("I fill in checkout information with first name {string}, last name {string}, and postal code {string}")
    public void iFillInCheckoutInformation(String firstName, String lastName, String postalCode) {
        checkoutPage = new CheckoutPage(hooks.getPage());
        checkoutPage.fillInformation(firstName, lastName, postalCode);
    }

    @When("I continue to the overview")
    public void iContinueToTheOverview() {
        checkoutPage = new CheckoutPage(hooks.getPage());
        checkoutPage.clickContinue();
    }

    @When("I finish the order")
    public void iFinishTheOrder() {
        checkoutPage = new CheckoutPage(hooks.getPage());
        checkoutPage.clickFinish();
    }

    @When("I click back to products")
    public void iClickBackToProducts() {
        checkoutPage = new CheckoutPage(hooks.getPage());
        checkoutPage.clickBackHome();
    }

    @Then("I should see the checkout overview with {int} item(s)")
    public void iShouldSeeTheCheckoutOverview(int expectedItemCount) {
        checkoutPage = new CheckoutPage(hooks.getPage());
        assertThat(checkoutPage.getTitle()).isEqualTo("Checkout: Overview");
        assertThat(checkoutPage.getSummaryItemCount()).isEqualTo(expectedItemCount);
    }

    @Then("I should see the order confirmation with {string}")
    public void iShouldSeeTheOrderConfirmation(String expectedMessage) {
        checkoutPage = new CheckoutPage(hooks.getPage());
        assertThat(checkoutPage.isOrderComplete()).isTrue();
        assertThat(checkoutPage.getCompleteHeader()).isEqualTo(expectedMessage);
    }

    @Then("I should see a checkout error containing {string}")
    public void iShouldSeeACheckoutErrorContaining(String expectedText) {
        checkoutPage = new CheckoutPage(hooks.getPage());
        assertThat(checkoutPage.isErrorVisible()).isTrue();
        assertThat(checkoutPage.getErrorMessage()).contains(expectedText);
    }
}
