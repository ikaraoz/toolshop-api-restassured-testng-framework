Feature: Cart management
  As a shopper
  I want to manage items in my cart
  So that I can complete purchases

  Scenario: Create a new cart
    When I create a new cart
    Then the cart response status is 200
    And a cart id is generated

  Scenario: Add product to cart
    Given I have an existing cart
    And a product is available
    When I add 10 units of the product to the cart
    Then the cart item is added successfully

  Scenario: Retrieve cart by id
    Given I have an existing cart
    When I retrieve the cart by id
    Then the retrieved cart matches the stored cart

  Scenario: Delete cart by id
    Given I have an existing cart
    When I delete the cart
    Then the cart deletion response status is 204

  Scenario: Update quantity of an item in the cart
    Given I have a cart containing 10 units of a product
    When I update the cart item quantity to 20
    Then the cart item quantity is 20

  Scenario: Remove product from cart
    Given I have a cart containing 10 units of a product
    When I remove the product from the cart
    Then the cart has no items