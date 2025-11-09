package com.bookingmx.reservations.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

/**
 * Global exception handler for the Reservation API.
 * <p>
 * This class centralizes the handling of exceptions thrown by controllers
 * within the application. It ensures consistent and user-friendly JSON error
 * responses across all endpoints.
 * </p>
 *
 * <p>
 * Each method corresponds to a specific exception type and returns a
 * {@link ResponseEntity} with the appropriate HTTP status code and
 * structured error body.
 * </p>
 *
 * <p>
 * The default error format returned by this handler is:
 * </p>
 *
 * <pre>
 * {
 *   "timestamp": "2025-11-08T18:30:12.456Z",
 *   "status": 404,
 *   "message": "Reservation not found"
 * }
 * </pre>
 *
 * @see BadRequestException
 * @see NotFoundException
 * @see org.springframework.web.bind.annotation.RestControllerAdvice
 *
 * @author
 *     BookingMX Team
 * @version 1.0
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    /**
     * Handles {@link BadRequestException} errors and returns a 400 Bad Request response.
     *
     * @param ex the {@code BadRequestException} thrown by the application
     * @return a {@link ResponseEntity} with HTTP 400 and a structured JSON error body
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> badRequest(BadRequestException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorBody(ex.getMessage(), 400));
    }

    /**
     * Handles {@link NotFoundException} errors and returns a 404 Not Found response.
     *
     * @param ex the {@code NotFoundException} thrown by the application
     * @return a {@link ResponseEntity} with HTTP 404 and a structured JSON error body
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> notFound(NotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorBody(ex.getMessage(), 404));
    }

    /**
     * Handles all other uncaught exceptions and returns a 500 Internal Server Error response.
     * <p>
     * This acts as a fallback to prevent unhandled exceptions from leaking
     * implementation details to the client.
     * </p>
     *
     * @param ex the unexpected exception that occurred
     * @return a {@link ResponseEntity} with HTTP 500 and a generic error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> generic(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorBody("Unexpected error", 500));
    }

    /**
     * Builds a structured JSON response body for API errors.
     * <p>
     * The response includes:
     * <ul>
     *     <li>{@code timestamp}: when the error occurred</li>
     *     <li>{@code status}: HTTP status code</li>
     *     <li>{@code message}: error message or description</li>
     * </ul>
     * </p>
     *
     * @param message the error message to include
     * @param status  the HTTP status code
     * @return a {@link Map} containing error details
     */
    private Map<String, Object> errorBody(String message, int status) {
        return Map.of(
                "timestamp", Instant.now().toString(),
                "status", status,
                "message", message
        );
    }
}