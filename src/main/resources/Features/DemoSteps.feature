# Author: Phuong Anh
# Date: 12/11/2024
# Description: Demo open Docker

@SmokeScenario
Feature: Demo open Docker

  @View
  Scenario: Check open Docker
    Given open browser and url
    When Check title page
    Then close browser