package com.bookingmx.reservations.steps;

import com.bookingmx.reservations.dto.ReservationRequest;
import com.bookingmx.reservations.exception.BadRequestException;
import com.bookingmx.reservations.model.Reservation;
import static org.junit.jupiter.api.Assertions.*;

import com.bookingmx.reservations.model.ReservationStatus;
import com.bookingmx.reservations.repo.ReservationRepository;
import com.bookingmx.reservations.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

public class ReservationTest {

    private ReservationService service;
    private ReservationRepository repo;

    @BeforeEach
    void setup() {
        repo = mock(ReservationRepository.class);
        service = new ReservationService();
        try {
            java.lang.reflect.Field repoField = ReservationService.class.getDeclaredField("repo");
            repoField.setAccessible(true);
            repoField.set(service, repo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testEqualsAndHashCode() {
        Reservation r1 = new Reservation(1L, "Guest", "Hotel", LocalDate.now(), LocalDate.now().plusDays(1));
        Reservation r2 = new Reservation(1L, "Guest2", "Hotel2", LocalDate.now(), LocalDate.now().plusDays(2));
        Reservation r3 = new Reservation(2L, "Guest", "Hotel", LocalDate.now(), LocalDate.now().plusDays(1));

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());

        assertNotEquals(r1, r3);
        assertNotEquals(r1.hashCode(), r3.hashCode());

        assertNotEquals(r1, null);
        assertNotEquals(r1, "some string");
    }

    @Test
    void create_withValidDates_succeeds() {
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(3);

        ReservationRequest req = new ReservationRequest();
        req.setGuestName("John");
        req.setHotelName("Hotel A");
        req.setCheckIn(checkIn);
        req.setCheckOut(checkOut);

        when(repo.save(any(Reservation.class))).thenAnswer(i -> i.getArguments()[0]);

        Reservation r = service.create(req);

        assertEquals("John", r.getGuestName());
        assertEquals("Hotel A", r.getHotelName());
        assertEquals(checkIn, r.getCheckIn());
        assertEquals(checkOut, r.getCheckOut());
    }

    @Test
    void create_withCheckOutBeforeCheckIn_throwsException() {
        ReservationRequest req = new ReservationRequest();
        req.setGuestName("John");
        req.setHotelName("Hotel A");
        req.setCheckIn(LocalDate.now().plusDays(5));
        req.setCheckOut(LocalDate.now().plusDays(3));

        BadRequestException ex = assertThrows(BadRequestException.class, () -> service.create(req));
        assertEquals("Check-out must be after check-in", ex.getMessage());
    }

    @Test
    void create_withCheckInInPast_throwsException() {
        ReservationRequest req = new ReservationRequest();
        req.setGuestName("John");
        req.setHotelName("Hotel A");
        req.setCheckIn(LocalDate.now().minusDays(1));
        req.setCheckOut(LocalDate.now().plusDays(1));

        BadRequestException ex = assertThrows(BadRequestException.class, () -> service.create(req));
        assertEquals("Check-in must be in the future", ex.getMessage());
    }

    @Test
    void create_withCheckIn_null_throwsException() {
        assertThrows(BadRequestException.class, () -> service.validateDates(null, LocalDate.now().plusDays(1)));
    }

    @Test
    void create_withCheckOut_null_throwsException() {
        assertThrows(BadRequestException.class, () -> service.validateDates(LocalDate.now().plusDays(1), null));
    }

    @Test
    void create_withBothDates_null_throwsException() {
        assertThrows(BadRequestException.class, () -> service.validateDates(null, null));
    }

    @Test
    void create_withCheckOutInPast_throwsException() {
        ReservationRequest req = new ReservationRequest();
        req.setGuestName("John");
        req.setHotelName("Hotel A");
        req.setCheckIn(LocalDate.now().plusDays(1));
        req.setCheckOut(LocalDate.now().minusDays(1));

        BadRequestException ex = assertThrows(BadRequestException.class, () -> service.create(req));
        assertEquals("Check-out must be after check-in", ex.getMessage());
    }

    @Test
    void isActive_returnsTrue_whenStatusIsActive() {
        Reservation r = new Reservation(1L, "John", "Hotel A", LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));
        r.setStatus(ReservationStatus.ACTIVE);

        assertTrue(r.isActive());
    }

    @Test
    void isActive_returnsFalse_whenStatusIsCanceled() {
        Reservation r = new Reservation(1L, "John", "Hotel A", LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));
        r.setStatus(ReservationStatus.CANCELED);

        assertFalse(r.isActive());
    }

    @Test
    void equals_returnsTrue_whenComparingWithSelf() {
        Reservation r = new Reservation(1L, "John", "Hotel A", LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));

        assertTrue(r.equals(r));
    }

    @Test
    void gettersAndSetters_workCorrectly() {
        Reservation r = new Reservation(null, null, null, null, null);

        r.setId(1L);
        r.setGuestName("John");
        r.setHotelName("Hotel A");
        r.setCheckIn(LocalDate.now().plusDays(1));
        r.setCheckOut(LocalDate.now().plusDays(2));

        assertEquals(1L, r.getId());
        assertEquals("John", r.getGuestName());
        assertEquals("Hotel A", r.getHotelName());
        assertEquals(LocalDate.now().plusDays(1), r.getCheckIn());
        assertEquals(LocalDate.now().plusDays(2), r.getCheckOut());
    }
}
