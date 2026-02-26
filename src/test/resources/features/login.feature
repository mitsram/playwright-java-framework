Feature: SauceDemo Login
  As a user of the SauceDemo application
  I want to log in with my credentials
  So that I can access the product inventory

  Background:
    Given I am on the SauceDemo login page

  Scenario: Successful login with standard user
    When I login with username "standard_user" and password "secret_sauce"
    Then I should see the inventory page
    And the page title should be "Products"

  Scenario: Login fails with locked out user
    When I login with username "locked_out_user" and password "secret_sauce"
    Then I should see a login error containing "locked out"

  Scenario: Login fails with invalid credentials
    When I login with username "invalid_user" and password "bad_password"
    Then I should see a login error containing "do not match"

  Scenario: Login fails with empty username
    When I enter password "secret_sauce"
    And I click the login button
    Then I should see a login error containing "Username is required"

  Scenario: Login fails with empty password
    When I enter username "standard_user"
    And I click the login button
    Then I should see a login error containing "Password is required"
