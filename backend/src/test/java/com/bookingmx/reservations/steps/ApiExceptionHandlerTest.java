package com.bookingmx.reservations.steps;

import com.bookingmx.reservations.dto.ReservationRequest;
import com.bookingmx.reservations.exception.ApiExceptionHandler;
import com.bookingmx.reservations.exception.BadRequestException;
import com.bookingmx.reservations.exception.NotFoundException;
import com.bookingmx.reservations.service.ReservationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class ApiExceptionHandlerTest {
    private MockMvc mockMvc;
    private ReservationService service;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        service = mock(ReservationService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new com.bookingmx.reservations.controller.ReservationController(service))
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void getReservation_notFoundException_returns404() throws Exception {
        when(service.findById(1L)).thenThrow(new NotFoundException("Reservation not found"));

        mockMvc.perform(get("/api/reservations/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Reservation not found"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void createReservation_badRequestException_returns400() throws Exception {
        when(service.create(any())).thenThrow(new BadRequestException("Invalid dates"));

        ReservationRequest req = new ReservationRequest();
        req.setGuestName("John");
        req.setHotelName("Hotel A");
        req.setCheckIn(LocalDate.now().plusDays(1));
        req.setCheckOut(LocalDate.now().plusDays(1));

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Invalid dates"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void updateReservation_genericException_returns500() throws Exception {
        when(service.update(any(), any())).thenThrow(new RuntimeException("Unexpected error"));

        ReservationRequest req = new ReservationRequest();
        req.setGuestName("Alice");
        req.setHotelName("Hotel B");
        req.setCheckIn(LocalDate.now().plusDays(2));
        req.setCheckOut(LocalDate.now().plusDays(4));

        mockMvc.perform(put("/api/reservations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("Unexpected error"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void cancelReservation_notFoundException_returns404() throws Exception {
        when(service.cancel(1L)).thenThrow(new NotFoundException("Reservation not found"));

        mockMvc.perform(delete("/api/reservations/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Reservation not found"))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
