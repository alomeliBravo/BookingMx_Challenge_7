/**
 * @fileoverview Minimal API client for the backend reservations module.
 * @description
 * Provides simple, modular functions to interact with the Reservation API,
 * including listing, creating, updating, and cancelling reservations.
 * 
 * Designed for easy mocking and testing (e.g., in Jest).
 */

const BASE_URL = "http://localhost:8080/api/reservations";

/**
 * Fetches all reservations from the backend API.
 * 
 * @async
 * @function listReservations
 * @returns {Promise<Object[]>} A promise that resolves to an array of reservation objects.
 * @throws {Error} If the request fails or the response is not OK.
 * 
 * @example
 * const reservations = await listReservations();
 * console.log(reservations);
 */
export async function listReservations() {
  const res = await fetch(BASE_URL);
  if (!res.ok) throw new Error("Failed to fetch reservations");
  return res.json();
}

/**
 * Creates a new reservation in the backend.
 * 
 * @async
 * @function createReservation
 * @param {Object} payload - The reservation data to create.
 * @param {string} payload.guestName - Name of the guest.
 * @param {string} payload.hotelName - Name of the hotel.
 * @param {string} payload.checkIn - Check-in date (ISO 8601 format).
 * @param {string} payload.checkOut - Check-out date (ISO 8601 format).
 * @returns {Promise<Object>} A promise that resolves to the created reservation object.
 * @throws {Error} If the creation fails or the response is not OK.
 * 
 * @example
 * const newReservation = await createReservation({
 *   guestName: "John Doe",
 *   hotelName: "Hotel Example",
 *   checkIn: "2025-11-10",
 *   checkOut: "2025-11-15"
 * });
 */
export async function createReservation(payload) {
  const res = await fetch(BASE_URL, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload)
  });
  if (!res.ok) throw new Error((await res.json()).message || "Create failed");
  return res.json();
}

/**
 * Updates an existing reservation.
 * 
 * @async
 * @function updateReservation
 * @param {string|number} id - The unique identifier of the reservation to update.
 * @param {Object} payload - The updated reservation data.
 * @param {string} [payload.guestName] - Updated guest name.
 * @param {string} [payload.hotelName] - Updated hotel name.
 * @param {string} [payload.checkIn] - Updated check-in date (ISO 8601 format).
 * @param {string} [payload.checkOut] - Updated check-out date (ISO 8601 format).
 * @returns {Promise<Object>} A promise that resolves to the updated reservation object.
 * @throws {Error} If the update fails or the response is not OK.
 * 
 * @example
 * const updated = await updateReservation(123, { guestName: "Jane Doe" });
 */
export async function updateReservation(id, payload) {
  const res = await fetch(`${BASE_URL}/${encodeURIComponent(id)}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload)
  });
  if (!res.ok) throw new Error((await res.json()).message || "Update failed");
  return res.json();
}

/**
 * Cancels (deletes) a reservation by its ID.
 * 
 * @async
 * @function cancelReservation
 * @param {string|number} id - The unique identifier of the reservation to cancel.
 * @returns {Promise<Object>} A promise that resolves to the cancellation confirmation or deleted record.
 * @throws {Error} If the cancellation fails or the response is not OK.
 * 
 * @example
 * await cancelReservation(123);
 */
export async function cancelReservation(id) {
  const res = await fetch(`${BASE_URL}/${encodeURIComponent(id)}`, { method: "DELETE" });
  if (!res.ok) throw new Error((await res.json()).message || "Cancel failed");
  return res.json();
}