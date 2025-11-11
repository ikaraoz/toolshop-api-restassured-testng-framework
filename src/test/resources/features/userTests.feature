Feature: User account flows

  Scenario Outline: Successful login returns an access token
    Given I am an existing user with email "<username>" and password "<password>"
    When I submit the login request
    Then the login is successful and an access token is returned
    Examples:
      | username                             | password  |
      | customer@practicesoftwaretesting.com | welcome011 |
      | customer@practicesoftwaretesting.com | welcome01 |
      | customer@practicesoftwaretesting.com | welcome01 |


  Scenario: Successful registration creates a user
    Given I provide new user details
    When I submit the registration request
    Then the user is created successfully