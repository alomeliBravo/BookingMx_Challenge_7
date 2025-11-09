package com.bookingmx.reservations.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) representing the payload for creating or updating a reservation.
 * <p>
 * This class is used to receive and validate user input from HTTP requests. 
 * It contains essential reservation details such as the guest name, hotel name, 
 * and check-in/check-out dates.
 * </p>
 *
 * <p>
 * Validation annotations ensure that:
 * <ul>
 *   <li>Guest and hotel names are not blank.</li>
 *   <li>Check-in and check-out dates are not null and must represent future dates.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Example usage:
 * </p>
 * <pre>
 * {
 *   "guestName": "John Doe",
 *   "hotelName": "Ocean View Resort",
 *   "checkIn": "2025-12-10",
 *   "checkOut": "2025-12-15"
 * }
 * </pre>
 *
 * @author
 *     BookingMX Team
 * @version 1.0
 */
public class ReservationRequest {

    /**
     * The name of the guest making the reservation.
     * <p>
     * This field cannot be blank.
     * </p>
     */
    @NotBlank
    private String guestName;

    /**
     * The name of the hotel where the reservation is made.
     * <p>
     * This field cannot be blank.
     * </p>
     */
    @NotBlank
    private String hotelName;

    /**
     * The check-in date for the reservation.
     * <p>
     * Must not be null and must be a future date.
     * </p>
     */
    @NotNull
    @Future
    private LocalDate checkIn;

    /**
     * The check-out date for the reservation.
     * <p>
     * Must not be null and must be a future date.
     * </p>
     */
    @NotNull
    @Future
    private LocalDate checkOut;

    /**
     * Returns the guest name associated with this reservation request.
     *
     * @return the guest's name
     */
    public String getGuestName() {
        return guestName;
    }

    /**
     * Sets the guest name for this reservation request.
     *
     * @param guestName the name of the guest
     */
    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    /**
     * Returns the hotel name associated with this reservation request.
     *
     * @return the name of the hotel
     */
    public String getHotelName() {
        return hotelName;
    }

    /**
     * Sets the hotel name for this reservation request.
     *
     * @param hotelName the name of the hotel
     */
    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    /**
     * Returns the check-in date for the reservation.
     *
     * @return the check-in date
     */
    public LocalDate getCheckIn() {
        return checkIn;
    }

    /**
     * Sets the check-in date for the reservation.
     *
     * @param checkIn the check-in date (must be a future date)
     */
    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    /**
     * Returns the check-out date for the reservation.
     *
     * @return the check-out date
     */
    public LocalDate getCheckOut() {
        return checkOut;
    }

    /**
     * Sets the check-out date for the reservation.
     *
     * @param checkOut the check-out date (must be a future date)
     */
    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }
}
