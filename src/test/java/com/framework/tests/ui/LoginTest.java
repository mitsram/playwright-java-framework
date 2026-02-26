package com.framework.tests.ui;

import com.framework.base.BaseUITest;
import com.framework.pages.InventoryPage;
import com.framework.pages.LoginPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * UI tests for the SauceDemo Login page.
 */
public class LoginTest extends BaseUITest {

    private LoginPage loginPage;

    @BeforeMethod(alwaysRun = true, dependsOnMethods = "setupTest")
    public void initPages() {
        loginPage = new LoginPage(page);
    }

    @Test(description = "Verify login page loads with logo")
    public void testLoginPageLoads() {
        loginPage.open();

        assertThat(loginPage.isLogoVisible()).isTrue();
        assertThat(loginPage.getLogoText()).isEqualTo("Swag Labs");
    }

    @Test(description = "Verify successful login with standard_user")
    public void testValidLogin() {
        loginPage.open();
        loginPage.loginAs("standard_user", "secret_sauce");

        InventoryPage inventoryPage = new InventoryPage(page);
        assertThat(inventoryPage.isLoaded()).isTrue();
        assertThat(inventoryPage.getTitle()).isEqualTo("Products");
        assertThat(page.url()).contains("/inventory.html");
    }

    @Test(description = "Verify login fails with locked_out_user")
    public void testLockedOutUser() {
        loginPage.open();
        loginPage.loginAs("locked_out_user", "secret_sauce");

        assertThat(loginPage.isErrorVisible()).isTrue();
        assertThat(loginPage.getErrorMessage()).contains("locked out");
    }

    @Test(description = "Verify login fails with invalid credentials")
    public void testInvalidCredentials() {
        loginPage.open();
        loginPage.loginAs("invalid_user", "wrong_password");

        assertThat(loginPage.isErrorVisible()).isTrue();
        assertThat(loginPage.getErrorMessage()).contains("do not match");
    }

    @Test(description = "Verify login fails with empty username")
    public void testEmptyUsername() {
        loginPage.open();
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();

        assertThat(loginPage.isErrorVisible()).isTrue();
        assertThat(loginPage.getErrorMessage()).contains("Username is required");
    }

    @Test(description = "Verify login fails with empty password")
    public void testEmptyPassword() {
        loginPage.open();
        loginPage.enterUsername("standard_user");
        loginPage.clickLogin();

        assertThat(loginPage.isErrorVisible()).isTrue();
        assertThat(loginPage.getErrorMessage()).contains("Password is required");
    }
}
