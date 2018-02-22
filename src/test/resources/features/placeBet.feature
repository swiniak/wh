@betting @javascript
Feature: Placing bets

  # I would probably skip logging in for multiple scenarios and pass a cookie or something to speed up testing
  Background: Joe Bloggs is logged in
    Given Joe Bloggs has an account
    And he is on betting page
    When he logs in
    Then bob's your uncle, he's logged in

  Scenario Outline: Joe Bloggs bets on English Premier League
    Given Joe Bloggs goes to betting page
    And he finds English Premier League football event
    And he has at least <quids> on his account
    When he bets <quids> on <team> to win
    Then he nitpicks odds and returns

  Examples:
  | quids | team |
  |  0.05 | home |