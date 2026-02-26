Feature: SauceDemo Checkout
  As a user of the SauceDemo application
  I want to complete the checkout process
  So that I can purchase my selected items

  Background:
    Given I am on the SauceDemo login page
    When I login with username "standard_user" and password "secret_sauce"
    Then I should see the inventory page

  Scenario: Complete end-to-end checkout
    When I add "sauce-labs-backpack" to the cart
    And I navigate to the cart page
    And I proceed to checkout
    And I fill in checkout information with first name "John", last name "Doe", and postal code "12345"
    And I continue to the overview
    Then I should see the checkout overview with 1 item
    When I finish the order
    Then I should see the order confirmation with "Thank you for your order!"

  Scenario: Checkout requires first name
    When I add "sauce-labs-backpack" to the cart
    And I navigate to the cart page
    And I proceed to checkout
    And I continue to the overview
    Then I should see a checkout error containing "First Name is required"

  Scenario: Return to products after completing checkout
    When I add "sauce-labs-bike-light" to the cart
    And I navigate to the cart page
    And I proceed to checkout
    And I fill in checkout information with first name "Jane", last name "Doe", and postal code "54321"
    And I continue to the overview
    And I finish the order
    And I click back to products
    Then I should see the inventory page
