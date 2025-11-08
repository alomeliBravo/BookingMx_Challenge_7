package com.bookingmx.reservations.steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


public class CommonSteps extends CucumberSpringContextConfig {
    @Autowired
    private MockMvc mockMvc;
    private MvcResult result;

    public void setResult(MvcResult result) {
        this.result = result;
    }

    @Then("the response status should be {int} and Content-Type JSON")
    public void verifyStatusAndContentType(int expectedStatus) throws Exception {
        if (result == null) throw new IllegalStateException("No MvcResult available");

        int actualStatus = result.getResponse().getStatus();
        if (actualStatus != expectedStatus) {
            throw new AssertionError("Expected status " + expectedStatus + " but got " + actualStatus);
        }

        String contentType = result.getResponse().getContentType();
        if (!MediaType.APPLICATION_JSON_VALUE.equals(contentType)) {
            throw new AssertionError("Expected Content-Type application/json but got " + contentType);
        }
    }

    @Then("the response status should be {int}")
    public void theResponseStatusShouldBe(int expectedStatus) throws Exception{
        int actualStatus = result.getResponse().getStatus();
        if (actualStatus != expectedStatus) {
            throw new AssertionError("Expected status " + expectedStatus + " but got " + actualStatus);
        }
    }

    @Then("the response must be an empty array")
    public void theResponseMustBeAnEmptyArray() throws Exception{
        String content = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(content);

        if (!node.isArray()) {
            throw new AssertionError("Expected an array but got " + node);
        }

        if (!node.isEmpty()) {
            throw new AssertionError("Expected an empty array but got " + node);
        }
    }

}
