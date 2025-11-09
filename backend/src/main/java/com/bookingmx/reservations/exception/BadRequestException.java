package com.bookingmx.reservations.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a client sends an invalid or malformed request.
 * <p>
 * This exception is mapped to the HTTP {@code 400 Bad Request} status code.
 * It should be used to indicate that the request could not be processed due
 * to client-side errors such as invalid parameters, missing fields, or
 * business rule violations.
 * </p>
 *
 * <p>
 * When this exception is thrown, Spring automatically sets the HTTP status
 * to {@code 400 Bad Request} using the {@link ResponseStatus} annotation.
 * </p>
 *
 * <p><b>Example usage:</b></p>
 * <pre>
 * if (checkInDate.isAfter(checkOutDate)) {
 *     throw new BadRequestException("Check-in date cannot be after check-out date");
 * }
 * </pre>
 *
 * @see org.springframework.web.bind.annotation.ResponseStatus
 * @see com.bookingmx.reservations.exception.ApiExceptionHandler
 * @see com.bookingmx.reservations.exception.NotFoundException
 *
 * @author
 *     BookingMX Team
 * @version 1.0
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    /**
     * Constructs a new {@code BadRequestException} with the specified detail message.
     *
     * @param m the error message describing why the request was invalid
     */
    public BadRequestException(String m) {
        super(m);
    }
}
