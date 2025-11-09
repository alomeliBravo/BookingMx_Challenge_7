package com.bookingmx.reservations.dto;

import com.bookingmx.reservations.model.ReservationStatus;
import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) representing the data returned to clients
 * when retrieving or modifying reservations.
 * <p>
 * This class provides a read-only representation of a reservation,
 * including its identifier, guest and hotel details, date range,
 * and current status.
 * </p>
 *
 * <p>
 * Typical usage:
 * </p>
 * <pre>
 * {
 *   "id": 1,
 *   "guestName": "John Doe",
 *   "hotelName": "Ocean View Resort",
 *   "checkIn": "2025-12-10",
 *   "checkOut": "2025-12-15",
 *   "status": "CONFIRMED"
 * }
 * </pre>
 *
 * @see com.bookingmx.reservations.model.Reservation
 * @see com.bookingmx.reservations.model.ReservationStatus
 *
 * @author
 *     BookingMX Team
 * @version 1.0
 */
public class ReservationResponse {

    /**
     * Unique identifier of the reservation.
     */
    private Long id;

    /**
     * Name of the guest who made the reservation.
     */
    private String guestName;

    /**
     * Name of the hotel where the reservation is booked.
     */
    private String hotelName;

    /**
     * Date when the guest will check into the hotel.
     */
    private LocalDate checkIn;

    /**
     * Date when the guest will check out of the hotel.
     */
    private LocalDate checkOut;

    /**
     * Current status of the reservation (e.g., CONFIRMED, CANCELED).
     */
    private ReservationStatus status;

    /**
     * Constructs a new {@code ReservationResponse} with all required fields.
     *
     * @param id         the unique identifier of the reservation
     * @param guestName  the name of the guest
     * @param hotelName  the name of the hotel
     * @param checkIn    the check-in date
     * @param checkOut   the check-out date
     * @param status     the current reservation status
     */
    public ReservationResponse(Long id,
                               String guestName,
                               String hotelName,
                               LocalDate checkIn,
                               LocalDate checkOut,
                               ReservationStatus status) {
        this.id = id;
        this.guestName = guestName;
        this.hotelName = hotelName;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.status = status;
    }

    /**
     * Returns the reservation's unique identifier.
     *
     * @return the reservation ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the name of the guest who made the reservation.
     *
     * @return the guest's name
     */
    public String getGuestName() {
        return guestName;
    }

    /**
     * Returns the name of the hotel where the reservation is made.
     *
     * @return the hotel name
     */
    public String getHotelName() {
        return hotelName;
    }

    /**
     * Returns the check-in date of the reservation.
     *
     * @return the check-in date
     */
    public LocalDate getCheckIn() {
        return checkIn;
    }

    /**
     * Returns the check-out date of the reservation.
     *
     * @return the check-out date
     */
    public LocalDate getCheckOut() {
        return checkOut;
    }

    /**
     * Returns the current status of the reservation.
     *
     * @return the reservation status
     */
    public ReservationStatus getStatus() {
        return status;
    }
}
