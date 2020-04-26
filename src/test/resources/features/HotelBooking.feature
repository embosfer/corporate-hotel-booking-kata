Feature: Booking feature

  Scenario: A room cannot be booked on an un-existing hotel
    Given the employee <123> from the company "Acme"
    When the employee books the room type "Single" in the hotel "Unknown" on the dates "2020-04-15" to "2020-04-16"
    Then booking failure of type "UNKNOWN_HOTEL"

  Scenario: A room of a certain type cannot be booked on a hotel that does not provide it
    Given the employee <123> from the company "Acme"
    And the hotel "Taj Mahal" with available rooms
    | Single | 0 |
    | Double | 1 |
    When the employee books the room type "Single" in the hotel "Taj Mahal" on the dates "2020-04-15" to "2020-04-16"
    Then booking failure of type "UNAVAILABLE_ROOM"