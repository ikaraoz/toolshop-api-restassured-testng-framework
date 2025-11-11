Feature: Product catalogue
  As a store visitor
  I want to browse the product catalogue
  So that I can discover items to purchase

  Scenario: List products with default parameters
    When I request the default product list
    Then the product response status is 200
    And the product list is not empty

  Scenario: Validate product list structure
    When I request the default product list
    Then the product response status is 200
    And the product list contains products with required fields
    And the product list metadata is present

  Scenario: Get product details by id
    Given I have the first product from the catalogue
    When I request the product details by id
    Then the product details response status is 200
    And the product id matches the requested id