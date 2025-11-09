package com.bookingmx.reservations.controller;

import com.bookingmx.reservations.dto.ReservationRequest;
import com.bookingmx.reservations.dto.ReservationResponse;
import com.bookingmx.reservations.model.Reservation;
import com.bookingmx.reservations.service.ReservationService;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller that manages reservation-related operations.
 * <p>
 * This controller exposes endpoints for creating, retrieving, updating,
 * listing, and canceling hotel reservations. It delegates all business logic
 * to the {@link ReservationService}.
 * </p>
 *
 * <p>
 * Cross-origin requests are allowed from local frontend development origins:
 * {@code http://localhost:5173}, {@code http://127.0.0.1:5173}, and any other origin.
 * </p>
 *
 * <p>
 * All endpoints produce responses in JSON format.
 * </p>
 *
 * @author
 *     BookingMX Team
 * @version 1.0
 */
@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173", "*"})
@RequestMapping(value = "/api/reservations", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReservationController {

    private final ReservationService service;

    /**
     * Constructs a new {@code ReservationController} with the specified service dependency.
     *
     * @param service the {@link ReservationService} used to handle reservation operations
     */
    public ReservationController(ReservationService service) {
        this.service = service;
    }

    /**
     * Retrieves a list of all reservations.
     *
     * @return a list of {@link ReservationResponse} objects representing all reservations
     */
    @GetMapping
    public List<ReservationResponse> list() {
        return service.list().stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Retrieves a reservation by its ID.
     *
     * @param id the unique identifier of the reservation
     * @return a {@link ReservationResponse} object representing the found reservation
     * @throws com.bookingmx.reservations.exception.ReservationNotFoundException
     *         if no reservation with the given ID exists
     */
    @GetMapping("/{id}")
    public ReservationResponse get(@PathVariable("id") Long id) {
        return toResponse(service.findById(id));
    }

    /**
     * Creates a new reservation using the provided request data.
     *
     * @param req the {@link ReservationRequest} containing guest, hotel, and date information
     * @return a {@link ReservationResponse} representing the created reservation
     * @throws jakarta.validation.ConstraintViolationException
     *         if the provided data fails validation
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ReservationResponse create(@Valid @RequestBody ReservationRequest req) {
        return toResponse(service.create(req));
    }

    /**
     * Updates an existing reservation with new data.
     *
     * @param id  the unique identifier of the reservation to update
     * @param req the {@link ReservationRequest} containing updated reservation details
     * @return a {@link ReservationResponse} representing the updated reservation
     * @throws com.bookingmx.reservations.exception.ReservationNotFoundException
     *         if no reservation with the given ID exists
     * @throws jakarta.validation.ConstraintViolationException
     *         if the provided data fails validation
     */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ReservationResponse update(@PathVariable("id") Long id, @Valid @RequestBody ReservationRequest req) {
        return toResponse(service.update(id, req));
    }

    /**
     * Cancels an existing reservation.
     *
     * @param id the unique identifier of the reservation to cancel
     * @return a {@link ReservationResponse} representing the canceled reservation
     * @throws com.bookingmx.reservations.exception.ReservationNotFoundException
     *         if no reservation with the given ID exists
     */
    @DeleteMapping("/{id}")
    public ReservationResponse cancel(@PathVariable("id") Long id) {
        return toResponse(service.cancel(id));
    }

    /**
     * Converts a {@link Reservation} entity into a {@link ReservationResponse} DTO.
     *
     * @param r the reservation entity to convert
     * @return a corresponding {@link ReservationResponse} with mapped fields
     */
    private ReservationResponse toResponse(Reservation r) {
        return new ReservationResponse(
                r.getId(),
                r.getGuestName(),
                r.getHotelName(),
                r.getCheckIn(),
                r.getCheckOut(),
                r.getStatus()
        );
    }
}
