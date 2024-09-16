@active
@allure.label.layer:web
@allure.label.owner:jgbuitragoc
@allure.label.page:/{org}/{repo}/labels
Feature: Client testing CRUD

  @clientsPost
  Scenario: Change the phone number of the first Client named Laura
    Given there are registered 10 clients
    And there is a client named "Laura" created
    When i update the phone number of the first client with the name "Laura" to "+57 222-222-22-22"
    Then the client response should have a status code of 200
    And validates the response with client JSON schema
    And the new phone number is different than the old phone number

  @clientsPost
  Scenario: Update and delete a New Client
    Given I have a client with the following details:
      | Name | LastName | Country  | City     | Email          | Phone      | Id |
      | Juan | Calle    | colombia | medellin | juan@calle.com | 1111111111 | 1  |
    When I send a PUT request to update the client with ID "1"
    """
    {
      "name": "Juan Guillermo",
      "lastName": "Buitrago Calle",
      "country": "Colombia",
      "city": "Medellín",
      "email": "juancalle@gmail.com",
      "phone": "3053295454",
      "id": "1"
    }
    """
    And I send a DELETE request to delete the client with ID "1"
    Then the client response should have a status code of 200
    And validates the response with client JSON schema
    And the client response should have the following details:
      | Name           | LastName       | Country  | City     | Email               | Phone      | Id |
      | Juan Guillermo | Buitrago Calle | Colombia | Medellín | juancalle@gmail.com | 3053295454 | 1  |