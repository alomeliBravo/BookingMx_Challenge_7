package com.bookingmx.reservations.service;

import com.bookingmx.reservations.dto.ReservationRequest;
import com.bookingmx.reservations.exception.BadRequestException;
import com.bookingmx.reservations.exception.NotFoundException;
import com.bookingmx.reservations.model.Reservation;
import com.bookingmx.reservations.model.ReservationStatus;
import com.bookingmx.reservations.repo.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Service layer responsible for managing {@link Reservation} entities and
 * implementing business rules such as validation and status transitions.
 * <p>
 * This class interacts with the {@link ReservationRepository} to perform CRUD
 * operations and enforces business constraints such as date validation and
 * immutability of canceled reservations.
 * </p>
 *
 * <h3>Responsibilities</h3>
 * <ul>
 *   <li>Validating reservation requests</li>
 *   <li>Creating, updating, and canceling reservations</li>
 *   <li>Ensuring check-in/check-out dates are valid and in the future</li>
 * </ul>
 *
 * <p><strong>Note:</strong> This implementation uses an in-memory repository
 * ({@link ReservationRepository}), making it suitable for testing or
 * demonstration purposes. For production, it should be replaced by a
 * persistence-backed implementation.</p>
 *
 * @see com.bookingmx.reservations.model.Reservation
 * @see com.bookingmx.reservations.dto.ReservationRequest
 * @see com.bookingmx.reservations.repo.ReservationRepository
 * @since 1.0
 */
@Service
public class ReservationService {

    /** Repository managing in-memory reservation data. */
    private final ReservationRepository repo = new ReservationRepository();

    /**
     * Retrieves a list of all reservations.
     *
     * @return a list of all stored {@link Reservation} entities
     */
    public List<Reservation> list() {
        return repo.findAll();
    }

    /**
     * Creates a new reservation after validating input data.
     *
     * @param req the reservation creation request
     * @return the newly created {@link Reservation}
     * @throws BadRequestException if the dates are invalid or in the past
     */
    public Reservation create(ReservationRequest req) {
        validateDates(req.getCheckIn(), req.getCheckOut());
        Reservation r = new Reservation(
                null,
                req.getGuestName(),
                req.getHotelName(),
                req.getCheckIn(),
                req.getCheckOut()
        );
        return repo.save(r);
    }

    /**
     * Retrieves a reservation by its unique ID.
     *
     * @param id the ID of the reservation
     * @return the found {@link Reservation}
     * @throws NotFoundException if no reservation exists with the given ID
     */
    public Reservation findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Reservation not found"));
    }

    /**
     * Updates an existing reservation with new information.
     * <p>
     * A reservation can only be updated if it is currently active.
     * </p>
     *
     * @param id  the ID of the reservation to update
     * @param req the updated reservation details
     * @return the updated {@link Reservation}
     * @throws NotFoundException if no reservation exists with the given ID
     * @throws BadRequestException if the reservation is canceled or the dates are invalid
     */
    public Reservation update(Long id, ReservationRequest req) {
        Reservation existing = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Reservation not found"));

        if (!existing.isActive()) {
            throw new BadRequestException("Cannot update a canceled reservation");
        }

        validateDates(req.getCheckIn(), req.getCheckOut());
        existing.setGuestName(req.getGuestName());
        existing.setHotelName(req.getHotelName());
        existing.setCheckIn(req.getCheckIn());
        existing.setCheckOut(req.getCheckOut());
        return repo.save(existing);
    }

    /**
     * Cancels a reservation by setting its status to {@link ReservationStatus#CANCELED}.
     *
     * @param id the ID of the reservation to cancel
     * @return the canceled {@link Reservation}
     * @throws NotFoundException if the reservation is not found
     */
    public Reservation cancel(Long id) {
        Reservation existing = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Reservation not found"));

        existing.setStatus(ReservationStatus.CANCELED);
        return repo.save(existing);
    }

    /**
     * Validates the check-in and check-out dates for a reservation.
     * <p>
     * This method ensures that:
     * <ul>
     *   <li>Both dates are provided</li>
     *   <li>Check-out is after check-in</li>
     *   <li>Check-in is in the future</li>
     * </ul>
     * </p>
     *
     * @param in  the check-in date
     * @param out the check-out date
     * @throws BadRequestException if any validation rule is violated
     */
    public void validateDates(LocalDate in, LocalDate out) {
        if (in == null || out == null) {
            throw new BadRequestException("Dates cannot be null");
        }
        if (!out.isAfter(in)) {
            throw new BadRequestException("Check-out must be after check-in");
        }
        if (in.isBefore(LocalDate.now())) {
            throw new BadRequestException("Check-in must be in the future");
        }
    }
}