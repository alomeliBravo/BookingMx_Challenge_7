package com.bookingmx.reservations.model;

/**
 * Enumeration representing the possible statuses of a hotel reservation.
 * <p>
 * A reservation can either be {@link #ACTIVE} (currently valid and not canceled)
 * or {@link #CANCELED} (explicitly canceled by the user or system).
 * </p>
 *
 * <p>
 * This enum is used throughout the application to track and communicate
 * the lifecycle state of a {@link com.bookingmx.reservations.model.Reservation}.
 * </p>
 *
 * <p><b>Example usage:</b></p>
 * <pre>
 * Reservation reservation = new Reservation(...);
 * reservation.setStatus(ReservationStatus.CANCELED);
 * </pre>
 *
 * @see com.bookingmx.reservations.model.Reservation
 * @see com.bookingmx.reservations.dto.ReservationResponse
 *
 * @version 1.0
 * author
 *     BookingMX Team
 */
public enum ReservationStatus {

    /**
     * Indicates that the reservation is currently active and valid.
     */
    ACTIVE,

    /**
     * Indicates that the reservation has been canceled and is no longer valid.
     */
    CANCELED
}