package com.bookingmx.reservations.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Domain model representing a hotel reservation within the system.
 * <p>
 * A {@code Reservation} contains identifying information, guest and hotel details,
 * check-in/check-out dates, and its current {@link ReservationStatus}.
 * </p>
 *
 * <p>
 * This class is typically used by the service and repository layers to manage
 * reservation data, and may be mapped to a database entity in future extensions.
 * </p>
 *
 * <p><b>Example usage:</b></p>
 * <pre>
 * Reservation reservation = new Reservation(
 *     1L,
 *     "John Doe",
 *     "Ocean View Resort",
 *     LocalDate.of(2025, 12, 10),
 *     LocalDate.of(2025, 12, 15)
 * );
 * </pre>
 *
 * @see com.bookingmx.reservations.model.ReservationStatus
 * @see com.bookingmx.reservations.dto.ReservationRequest
 * @see com.bookingmx.reservations.dto.ReservationResponse
 *
 * @version 1.0
 * @author
 *     BookingMX Team
 */
public class Reservation {

    /**
     * Unique identifier for the reservation.
     */
    private Long id;

    /**
     * The name of the guest who made the reservation.
     */
    private String guestName;

    /**
     * The name of the hotel where the reservation is booked.
     */
    private String hotelName;

    /**
     * The date when the guest will check into the hotel.
     */
    private LocalDate checkIn;

    /**
     * The date when the guest will check out of the hotel.
     */
    private LocalDate checkOut;

    /**
     * The current status of the reservation.
     * <p>
     * Defaults to {@link ReservationStatus#ACTIVE}.
     * </p>
     */
    private ReservationStatus status = ReservationStatus.ACTIVE;

    /**
     * Constructs a new {@code Reservation} with the specified details.
     * <p>
     * The status is automatically set to {@link ReservationStatus#ACTIVE}.
     * </p>
     *
     * @param id         the unique identifier of the reservation
     * @param guestName  the name of the guest
     * @param hotelName  the name of the hotel
     * @param checkIn    the check-in date
     * @param checkOut   the check-out date
     */
    public Reservation(Long id, String guestName, String hotelName, LocalDate checkIn, LocalDate checkOut) {
        this.id = id;
        this.guestName = guestName;
        this.hotelName = hotelName;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.status = ReservationStatus.ACTIVE;
    }

    /** @return the unique reservation ID */
    public Long getId() { return id; }

    /** @param id sets the unique reservation ID */
    public void setId(Long id) { this.id = id; }

    /** @return the name of the guest */
    public String getGuestName() { return guestName; }

    /** @param guestName sets the guest name */
    public void setGuestName(String guestName) { this.guestName = guestName; }

    /** @return the name of the hotel */
    public String getHotelName() { return hotelName; }

    /** @param hotelName sets the hotel name */
    public void setHotelName(String hotelName) { this.hotelName = hotelName; }

    /** @return the check-in date */
    public LocalDate getCheckIn() { return checkIn; }

    /** @param checkIn sets the check-in date */
    public void setCheckIn(LocalDate checkIn) { this.checkIn = checkIn; }

    /** @return the check-out date */
    public LocalDate getCheckOut() { return checkOut; }

    /** @param checkOut sets the check-out date */
    public void setCheckOut(LocalDate checkOut) { this.checkOut = checkOut; }

    /** @return the current reservation status */
    public ReservationStatus getStatus() { return status; }

    /** @param status sets the reservation status */
    public void setStatus(ReservationStatus status) { this.status = status; }

    /**
     * Determines whether this reservation is currently active.
     *
     * @return {@code true} if the status is {@link ReservationStatus#ACTIVE}, {@code false} otherwise
     */
    public boolean isActive() { return this.status == ReservationStatus.ACTIVE; }

    /**
     * Indicates whether another object is equal to this reservation.
     * <p>
     * Two reservations are considered equal if they share the same {@code id}.
     * </p>
     *
     * @param o the object to compare
     * @return {@code true} if both objects represent the same reservation; {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reservation)) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    /**
     * Returns a hash code value for the reservation, based on its ID.
     *
     * @return the hash code for this reservation
     */
    @Override
    public int hashCode() { return Objects.hash(id); }
}