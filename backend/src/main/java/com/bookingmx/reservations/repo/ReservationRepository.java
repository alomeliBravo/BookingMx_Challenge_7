package com.bookingmx.reservations.repo;

import com.bookingmx.reservations.model.Reservation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory repository for managing {@link Reservation} entities.
 * <p>
 * This repository acts as a simple data access layer, simulating database behavior
 * using a thread-safe {@link ConcurrentHashMap}. It is primarily intended for
 * testing, development, or demo environments where a persistent database is not required.
 * </p>
 *
 * <p>
 * Each saved {@link Reservation} is assigned a unique, auto-incrementing ID
 * managed by an {@link AtomicLong} sequence generator.
 * </p>
 *
 * <h3>Example Usage</h3>
 * <pre>
 * ReservationRepository repo = new ReservationRepository();
 * Reservation res = new Reservation(null, "John Doe", "Hotel X",
 *         LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
 * repo.save(res);
 * List&lt;Reservation&gt; all = repo.findAll();
 * </pre>
 *
 * @see com.bookingmx.reservations.model.Reservation
 * @since 1.0
 */
public class ReservationRepository {

    /** Thread-safe in-memory store of reservations indexed by ID. */
    private final Map<Long, Reservation> store = new ConcurrentHashMap<>();

    /** Sequence generator for auto-incrementing reservation IDs. */
    private final AtomicLong seq = new AtomicLong(1L);

    /**
     * Retrieves all stored reservations.
     *
     * @return a list containing all reservations currently in the repository
     */
    public List<Reservation> findAll() {
        return new ArrayList<>(store.values());
    }

    /**
     * Retrieves a reservation by its unique ID.
     *
     * @param id the reservation ID to search for
     * @return an {@link Optional} containing the reservation if found, or empty otherwise
     */
    public Optional<Reservation> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    /**
     * Saves or updates a reservation.
     * <p>
     * If the reservation does not yet have an ID, a new one is generated automatically.
     * </p>
     *
     * @param r the reservation to save
     * @return the saved reservation instance (with an assigned ID if newly created)
     */
    public Reservation save(Reservation r) {
        if (r.getId() == null) {
            r.setId(seq.getAndIncrement());
        }
        store.put(r.getId(), r);
        return r;
    }
}