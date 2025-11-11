Feature: Invoice management
  As an authenticated user
  I want to review invoices
  So that I can track my orders

  Scenario: List invoices first page
    When I request invoices for page 1
    Then the invoice response status is 200