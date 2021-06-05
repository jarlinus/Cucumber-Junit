Feature: Google translate

  Scenario: Translate French to English
    Given user is in google home page
    When user translates "Bonjour" a French word to English
    Then the translated word "Hello" should be displayed