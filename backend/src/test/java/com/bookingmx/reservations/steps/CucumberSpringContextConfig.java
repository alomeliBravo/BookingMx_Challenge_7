package com.bookingmx.reservations.steps;

import com.bookingmx.reservations.BookingMxApplication;
import com.bookingmx.reservations.CucumberTestRunner;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@AutoConfigureMockMvc
@SpringBootTest(classes = BookingMxApplication.class)
public class CucumberSpringContextConfig {}
