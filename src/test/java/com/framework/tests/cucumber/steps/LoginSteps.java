package com.framework.tests.cucumber.steps;

import com.framework.pages.InventoryPage;
import com.framework.pages.LoginPage;
import com.framework.tests.cucumber.hooks.PlaywrightHooks;
import io.cucumber.java.en.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Step definitions for the SauceDemo Login feature.
 */
public class LoginSteps {

    private final PlaywrightHooks hooks;
    private LoginPage loginPage;

    public LoginSteps(PlaywrightHooks hooks) {
        this.hooks = hooks;
    }

    @Given("I am on the SauceDemo login page")
    public void iAmOnTheSauceDemoLoginPage() {
        loginPage = new LoginPage(hooks.getPage());
        loginPage.open();
    }

    @When("I login with username {string} and password {string}")
    public void iLoginWithCredentials(String username, String password) {
        loginPage.loginAs(username, password);
    }

    @When("I enter username {string}")
    public void iEnterUsername(String username) {
        loginPage.enterUsername(username);
    }

    @When("I enter password {string}")
    public void iEnterPassword(String password) {
        loginPage.enterPassword(password);
    }

    @When("I click the login button")
    public void iClickTheLoginButton() {
        loginPage.clickLogin();
    }

    @Then("I should see the inventory page")
    public void iShouldSeeTheInventoryPage() {
        InventoryPage inventoryPage = new InventoryPage(hooks.getPage());
        assertThat(inventoryPage.isLoaded()).isTrue();
    }

    @Then("the page title should be {string}")
    public void thePageTitleShouldBe(String expectedTitle) {
        InventoryPage inventoryPage = new InventoryPage(hooks.getPage());
        assertThat(inventoryPage.getTitle()).isEqualTo(expectedTitle);
    }

    @Then("I should see a login error containing {string}")
    public void iShouldSeeALoginErrorContaining(String expectedText) {
        assertThat(loginPage.isErrorVisible()).isTrue();
        assertThat(loginPage.getErrorMessage()).contains(expectedText);
    }
}
