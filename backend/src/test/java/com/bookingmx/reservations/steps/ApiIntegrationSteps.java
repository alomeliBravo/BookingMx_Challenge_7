package com.bookingmx.reservations.steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
public class ApiIntegrationSteps extends CucumberSpringContextConfig{
    @Autowired
    private MockMvc mockMvc;
    private MvcResult result;
    private final CommonSteps commonSteps;
    @Autowired
    private ObjectMapper objectMapper;

    public ApiIntegrationSteps(CommonSteps commonSteps) {
        this.commonSteps = commonSteps;
    }

    @Given("The API is running")
    public void theApiIsRunning(){
        Assert.assertNotNull(mockMvc);
    }

    @When("I create a new reservation")
    public void iCreateANewReservation() throws Exception {
        String json = """
            {
              "guestName": "John Doe",
              "hotelName": "Grand Sunset Resort",
              "checkIn": "2025-12-20",
              "checkOut": "2025-12-25"
            }
        """;
        result = mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andReturn();
        commonSteps.setResult(result);
    }

    @When("I call GET reservations endpoint")
    public void iCallGetApiReservations() throws Exception{
        result = mockMvc.perform(get("/api/reservations"))
                .andReturn();
        commonSteps.setResult(result);
    }

    @Then("the response must be an array with {int} reservations")
    public void theResponseMustBeAnArrayWithOneReservation(int numberOfReservations) throws Exception{
        String content = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(content);

        if (!node.isArray()) {
            throw new AssertionError("Expected an array but got " + node);
        }

        if (node.size() <  numberOfReservations || node.size() > numberOfReservations) {
            throw new AssertionError("Expected at least " + numberOfReservations + " reservations but got " + node);
        }
    }

    @When("I call DELETE reservation endpoint with id {int}")
    public void iCallDeleteReservation(int id) throws Exception{
        result = mockMvc.perform(delete("/api/reservations/" + id))
                .andReturn();
        commonSteps.setResult(result);
    }

    @Then("the Reservation with id {int} must be CANCELED")
    public void theReservationMustBeCanceled(int id) throws Exception{
        result = mockMvc.perform(get("/api/reservations/" + id))
                .andExpect(jsonPath("$.status").value("CANCELED"))
                .andReturn();
        commonSteps.setResult(result);
    }

    @When("I call UPDATE reservation endpoint with id {int}")
    public void iCallUpdateReservation(int id) throws Exception{
        String json = """
            {
              "guestName": "Doe John ",
              "hotelName": "Grand Moon Resort",
              "checkIn": "2025-12-25",
              "checkOut": "2025-12-31"
            }
        """;
        result = mockMvc.perform(put("/api/reservations/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andReturn();
        commonSteps.setResult(result);
    }

    @When("I call GET reservation by ID endpoint with id {int}")
    public void iCallGetReservationById(int id) throws Exception{
        result = mockMvc.perform(get("/api/reservations/" + id))
                .andReturn();
        commonSteps.setResult(result);
    }

    @When("I create a new Reservation with guestName {string}, hotelName {string}, checkIn {string}, checkOut {string}")
    public void createNewReservation(String guestName, String hotelName, String checkIn, String checkOut) throws Exception{
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("guestName", guestName);
        reservation.put("hotelName", hotelName);
        reservation.put("checkIn", checkIn);
        reservation.put("checkOut", checkOut);

        String json = objectMapper.writeValueAsString(reservation);

        result = mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andReturn();
        commonSteps.setResult(result);
    }

}
