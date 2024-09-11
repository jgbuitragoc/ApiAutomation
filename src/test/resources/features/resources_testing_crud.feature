@active
Feature: Resources testing CRUD

  @resourcesPost
  @resourcesActive
  Scenario: Get the list of active resources
    Given there are 5 active resources
    When i retrieve all the resources
    Then the resource response should have a status code of 200
    And validates the response with resource list JSON schema
    And at least 5 resources should be active

  @resourcesPost
  Scenario: Update the last created resource
    Given there are 15 active resources
    When i retrieve all the resources
    And i update the parameters of the last resource found
    """
    {
      "name": "Wine",
      "trademark": "Gato Negro",
      "stock": 100,
      "price": 27000.00,
      "description": "Finest wine AA quality",
      "tags": "drink",
      "active": true,
      "id": "2"
    }
    """
    Then the resource response should have a status code of 200
    And validates the response with resource JSON schema
    And the resources response should have the following details:
      | Name | Trademark  | Stock | Price    | Description            | Tags  | Active |
      | Wine | Gato Negro | 100   | 27000.00 | Finest wine AA quality | drink | true   |

