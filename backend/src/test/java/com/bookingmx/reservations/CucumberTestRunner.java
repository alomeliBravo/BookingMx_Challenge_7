package com.bookingmx.reservations;

import io.cucumber.core.options.Constants;
import org.junit.platform.suite.api.*;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME, value = "com.bookingmx.reservations.steps")
public class CucumberTestRunner {
}
