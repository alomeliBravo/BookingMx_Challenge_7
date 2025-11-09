package com.bookingmx.reservations.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a requested resource cannot be found.
 * <p>
 * This exception is mapped to the HTTP {@code 404 Not Found} status code.
 * It should be used when a client requests a reservation or any other resource
 * that does not exist in the system.
 * </p>
 *
 * <p>
 * When thrown, Spring automatically sets the HTTP response status to
 * {@code 404 Not Found} due to the {@link ResponseStatus} annotation.
 * </p>
 *
 * <p><b>Example usage:</b></p>
 * <pre>
 * Reservation reservation = repository.findById(id)
 *     .orElseThrow(() -> new NotFoundException("Reservation with ID " + id + " not found"));
 * </pre>
 *
 * @see org.springframework.web.bind.annotation.ResponseStatus
 * @see com.bookingmx.reservations.exception.ApiExceptionHandler
 * @see com.bookingmx.reservations.exception.BadRequestException
 *
 * @version 1.0
 * @author
 *     BookingMX Team
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code NotFoundException} with the specified detail message.
     *
     * @param m the error message explaining which resource could not be found
     */
    public NotFoundException(String m) {
        super(m);
    }
}