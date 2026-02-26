Feature: SauceDemo Shopping Cart
  As a user of the SauceDemo application
  I want to manage items in my shopping cart
  So that I can purchase the products I need

  Background:
    Given I am on the SauceDemo login page
    When I login with username "standard_user" and password "secret_sauce"
    Then I should see the inventory page

  Scenario: Add a single item to cart
    When I add "sauce-labs-backpack" to the cart
    Then the cart badge should show 1 item

  Scenario: Add multiple items to cart
    When I add "sauce-labs-backpack" to the cart
    And I add "sauce-labs-bike-light" to the cart
    And I add "sauce-labs-bolt-t-shirt" to the cart
    Then the cart badge should show 3 items

  Scenario: Remove an item from cart on inventory page
    When I add "sauce-labs-backpack" to the cart
    And I remove "sauce-labs-backpack" from the cart
    Then the cart badge should show 0 items

  Scenario: Verify cart contents
    When I add "sauce-labs-backpack" to the cart
    And I add "sauce-labs-bike-light" to the cart
    And I navigate to the cart page
    Then the cart should contain 2 items
    And the cart should contain "Sauce Labs Backpack"
    And the cart should contain "Sauce Labs Bike Light"
