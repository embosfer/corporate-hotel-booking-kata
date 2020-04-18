Feature: Booking feature

  Scenario: A room cannot be booked on an un-existing hotel
    Given the employee <123> from the company "Acme"
    When the employee books the room type "Standard" in the hotel "Taj Mahal" on the dates "2020-04-15" to "2020-04-16"
    Then the result is empty