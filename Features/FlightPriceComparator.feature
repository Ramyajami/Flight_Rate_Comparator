Feature: Flight Price Comparator
  Scenario Outline: Extract the flight prices from two different Travel websites (ClearTrip, Paytm)
    Given User launch chrome browser
    When user opens first portal
    And Enter valid search criteria "<from>" "<to>" locations
    And Enter valid journey "<date>"
    Then click on search button
    And Select non stop flights
    Then User able to view flights data like Flight Operator,Flight Number,Price on Cleartrip
    When user opens second portal
    Then Enter valid From "<from>" and To "<to>" locations
    And Enter valid travel "<date>"
    Then click on search flights button
    And Search non stop flights
    Then User able to view flights data like Flight Operator,Flight Number,Price on Paytm
    Then User should able to view price comparison csv report with "Flight Operator","Flight Number","Price on Cleartrip","Price on Paytm"
    Then close browser
  Examples:
  |from      |to   |date      |
  |Bengaluru |Delhi|2023/11/15|