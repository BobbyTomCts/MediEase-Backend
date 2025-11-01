@api_register_and_login
Feature: API
  @api_register_and_login
  Feature: API

  Scenario: Testing the register and login of the user
    Given user is registering
      | fieldName | value               |
      | name      | vineesha            |
      | phone     | 8778878787          |
      | email     | vineesha3@gmail.com |
      | password  | selenium            |
    When the user is successfully registered
    Then the user can login
    And token is generated successfully

  Scenario: Testing login
    Given the user can login with "vineesha@gmail.com" and "selenium"
    Then token is generated successfully
      # Test /validate
    When the user calls the secure validate endpoint
    Then the response status code is 200
    And the response user details match the logged-in user

      # Test /{id}
    When the user calls the get user by ID endpoint
    Then the response status code is 200

      # Test /isAdmin/{userId}
    When the user calls the isAdmin check endpoint
    Then the response confirms the user is "EMPLOYEE"

    Given the user can login with "admin@gmail.com" and "admin123"
    Then token is generated successfully

    When the user calls the isAdmin check endpoint
    Then the response confirms the user is "ADMIN"






