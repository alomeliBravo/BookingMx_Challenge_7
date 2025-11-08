Feature: Reservation Module

    Background: The server is running
        Given The API is running

    Scenario: Get Empty List of Reservations
      When I call GET reservations endpoint
      Then the response status should be 200 and Content-Type JSON
      Then the response must be an empty array

    Scenario: Create a new Reservation correctly and check if Exists
      When I create a new reservation
      Then the response status should be 200
      When I call GET reservations endpoint
      Then the response must be an array with 1 reservations

    Scenario: Delete a Reservation correctly
      When I create a new reservation
      Then the response status should be 200
      When I call DELETE reservation endpoint with id 1
      Then the response status should be 200
      Then the Reservation with id 1 must be CANCELED
      Then the response status should be 200

    Scenario: Delete a Reservation which doesn't exist
      When I call DELETE reservation endpoint with id 5
      Then the response status should be 404

    Scenario: Update a Reservation correctly
      When I create a new reservation
      Then the response status should be 200
      When I call UPDATE reservation endpoint with id 2
      Then the response status should be 200

    Scenario: Update a Reservation which doesn't exist
      When I call UPDATE reservation endpoint with id 4
      Then the response status should be 404

    Scenario: Find Reservation which doesn't exist
      When I call GET reservation by ID endpoint with id 10
      Then the response status should be 404