Feature: CucumberJava

  Scenario Outline: Add a New Computer in Database
    Given I have open the "<Browser>"browser
    When I open Computer database website
    Then Verify Landing page with Add a new Computer button is getting displayed
    Then Add a new computer
    |Name | Brand |
    |<Computer name> | <Company> |
    And Search a computer
      |Name |
      |<Computer name> |
    Examples:
      | Browser | Computer name | Company |
    |Chrome  | ComputerName  |IBM      |


  Scenario Outline: Update an existing Computer in Database
    Given I have open the "<Browser>"browser
    When I open Computer database website
    Then Verify Landing page with Add a new Computer button is getting displayed
    And Search a computer
      |Name |
      |<Computer name> |
    Then Update a Computer
      |Name |
      |<Computer name> |

    Examples:
      | Browser | Computer name |
      |Chrome  | ACE  |