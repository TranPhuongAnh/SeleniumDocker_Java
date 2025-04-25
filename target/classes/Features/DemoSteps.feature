# Author: Phuong Anh
# Date: 23/04/2025
# Description: Demo open Docker

@SmokeScenario
Feature: Demo open Docker

  @View
  Scenario: Check open Docker
    Given open browser and url
    When Check title page
    Then close browser