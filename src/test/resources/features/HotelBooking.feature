Feature: Booking feature

  Scenario: A room cannot be booked on an un-existing hotel
    Given the employee <123> from the company "Acme"
    And the hotel "Taj Mahal"
    And the room type "Standard"
    And the dates "2020-04-15" to "2020-04-16"
    When the employee books
    Then the result is empty