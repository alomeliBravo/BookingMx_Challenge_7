import { jest } from '@jest/globals';
import {
    listReservations,
    createReservation,
    updateReservation,
    cancelReservation
} from '../js/api.js';

const BASE_URL = "http://localhost:8080/api/reservations";

describe('API module', () => {
    beforeEach(() => {
        global.fetch = jest.fn();
    });

    afterEach(() => {
        jest.resetAllMocks();
    });

    test('listReservations: returns reservations when fetch succeeds', async () => {
        const mockData = [{ id: 1, guestName: 'John Doe' }];
        fetch.mockResolvedValueOnce({
            ok: true,
            json: async () => mockData
        });

        const result = await listReservations();
        expect(fetch).toHaveBeenCalledWith(BASE_URL);
        expect(result).toEqual(mockData);
    });

    test('listReservations: throws error when fetch fails', async () => {
        fetch.mockResolvedValueOnce({ ok: false });
        await expect(listReservations()).rejects.toThrow('Failed to fetch reservations');
    });

    test('createReservation: posts data and returns response', async () => {
        const payload = { guestName: 'Jane Doe' };
        const mockResponse = { id: 2, ...payload };

        fetch.mockResolvedValueOnce({
            ok: true,
            json: async () => mockResponse
        });

        const result = await createReservation(payload);
        expect(fetch).toHaveBeenCalledWith(BASE_URL, expect.objectContaining({
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        }));
        expect(result).toEqual(mockResponse);
    });

    test('createReservation: throws error when API returns error', async () => {
        fetch.mockResolvedValueOnce({
            ok: false,
            json: async () => ({ message: 'Create failed due to conflict' })
        });

        await expect(createReservation({})).rejects.toThrow('Create failed due to conflict');
    });

    test('updateReservation: updates reservation successfully', async () => {
        const payload = { guestName: 'Updated' };
        const mockResponse = { id: 3, ...payload };
        fetch.mockResolvedValueOnce({
            ok: true,
            json: async () => mockResponse
        });

        const result = await updateReservation(3, payload);
        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/3`, expect.objectContaining({
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        }));
        expect(result).toEqual(mockResponse);
    });

    test('updateReservation: throws error when API returns error', async () => {
        fetch.mockResolvedValueOnce({
            ok: false,
            json: async () => ({ message: 'Update failed due to missing field' })
        });

        await expect(updateReservation(3, {})).rejects.toThrow('Update failed due to missing field');
    });

    test('cancelReservation: deletes reservation successfully', async () => {
        const mockResponse = { message: 'Deleted successfully' };
        fetch.mockResolvedValueOnce({
            ok: true,
            json: async () => mockResponse
        });

        const result = await cancelReservation(4);
        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/4`, { method: 'DELETE' });
        expect(result).toEqual(mockResponse);
    });

    test('cancelReservation: throws error when API fails', async () => {
        fetch.mockResolvedValueOnce({
            ok: false,
            json: async () => ({ message: 'Cancel failed: not found' })
        });

        await expect(cancelReservation(4)).rejects.toThrow('Cancel failed: not found');
    });
});
