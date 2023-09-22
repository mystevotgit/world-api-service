package com.olatunji.world.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.olatunji.world.api.dtos.ConversionDTO;
import com.olatunji.world.api.models.*;
import com.olatunji.world.api.services.WorldApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApiController.class)
class ApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    WorldApiService worldApiService;

    @Test
    void testGetMostPopulatedCities() throws Exception {
        // Mock the service method to return a list of cities
        final List<City> cities = Arrays.asList(
                new City("City1", "Country", List.of(new Population("1993", "5300"))),
                new City("City2", "Country", List.of(new Population("1993", "3400")))
        );
        when(worldApiService.getMostPopulousCity(anyInt())).thenReturn(cities);

        // Perform a GET request to the endpoint
        mockMvc.perform(get("/api/v1/world/most-populated-cities")
                        .param("n", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.msg").value("filtered result"))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].city").value("City1"))
                .andExpect(jsonPath("$.data[1].city").value("City2"));
    }

    @Test
    void testGetCountryDetails() throws Exception {
        // Mock the service method to return country details
        final CountryDetails countryDetails = new CountryDetails();
        countryDetails.setCapitalCity("Abuja");
        when(worldApiService.getCountryDetails(anyString())).thenReturn(
                CountryDetails.builder()
                        .currency("NGN")
                        .capitalCity("Abuja")
                        .ISO2("NG")
                        .ISO3("NGA")
                        .population(195000000)
                        .location(Location.builder()
                                .longitude(8)
                                .latitude(10)
                                .build())
                        .build());

        // Perform a GET request to the endpoint
        mockMvc.perform(get("/api/v1/world/country/details")
                        .param("country", "Nigeria"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.msg").value("Details about Nigeria"))
                .andExpect(jsonPath("$.data.capitalCity").value("Abuja"))
                .andExpect(jsonPath("$.data.iso2").value("NG"))
                .andExpect(jsonPath("$.data.iso3").value("NGA"))
                .andExpect(jsonPath("$.data.population").value(195000000))
                .andExpect(jsonPath("$.data.currency").value("NGN"));
    }

    @Test
    void testGetStatesAndCities() throws Exception {
        // Mock the service method to return a map of states and cities
        final Map<String, List<String>> countryMap = new HashMap<>();
        countryMap.put("State1", Arrays.asList("CityA", "CityB"));
        countryMap.put("State2", Arrays.asList("CityX", "CityY"));
        when(worldApiService.getStatesAndCities(anyString())).thenReturn(countryMap);

        // Perform a GET request to the endpoint
        mockMvc.perform(get("/api/v1/world/states/cities")
                        .param("country", "Germany"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.msg").value("Germany states and their cities retrieved"))
                .andExpect(jsonPath("$.data.State1", hasSize(2)))
                .andExpect(jsonPath("$.data.State2", hasSize(2)))
                .andExpect(jsonPath("$.data.State1[0]").value("CityA"))
                .andExpect(jsonPath("$.data.State2[1]").value("CityY"));
    }

    @Test
    void testConvertCurrency() throws Exception {
        // Create a valid ConversionDTO for testing
        final ConversionDTO conversionDTO = new ConversionDTO("Germany", BigDecimal.valueOf(100.0), "EUR");

        // Mock the service method to return a conversion result
        ConversionData conversionData = new ConversionData("EUR", new BigDecimal("85.0"));
        when(worldApiService.convert(any())).thenReturn(conversionData);

        // Perform a POST request to the endpoint with the valid ConversionDTO as JSON
        mockMvc.perform(post("/api/v1/world/currency/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(conversionDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.msg").value("Germany's currency was converted to EUR"))
                .andExpect(jsonPath("$.data.amount").value(85.0))
                .andExpect(jsonPath("$.data.currency").value("EUR"));
    }

    @Test
    void testConvertCurrencyInvalidConversion() throws Exception {
        // Create a valid ConversionDTO for testing
        final ConversionDTO conversionDTO = new ConversionDTO("Wrong Country", BigDecimal.valueOf(100.0), "EUR");

        // Mock the service method to return null, simulating an invalid conversion
        when(worldApiService.convert(any())).thenReturn(null);

        // Perform a POST request to the endpoint with the valid ConversionDTO as JSON
        mockMvc.perform(post("/api/v1/world/currency/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(conversionDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.msg").value("There is no conversion rate for the currency specified"));
    }

    @Test
    void testConvertInvalidCountryName() throws Exception {
        // Create a ConversionDTO with an invalid country name
        final ConversionDTO conversionDTO = new ConversionDTO("Invalid Country123", BigDecimal.valueOf(100.0), "USD");

        // Perform a POST request to the endpoint with the invalid ConversionDTO as JSON
        mockMvc.perform(post("/api/v1/world/currency/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(conversionDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.msg").value("Error occurred"))
                .andExpect(jsonPath("$.errors.country").value("Invalid country name"));
    }

    @Test
    void testConvertNegativeAmount() throws Exception {
        // Create a ConversionDTO with a negative amount
        final ConversionDTO conversionDTO = new ConversionDTO("ValidCountry", BigDecimal.valueOf(-50.0), "USD");

        // Perform a POST request to the endpoint with the invalid ConversionDTO as JSON
        mockMvc.perform(post("/api/v1/world/currency/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(conversionDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.msg").value("Error occurred"))
                .andExpect(jsonPath("$.errors.amount").value("Value must be non-negative"));
    }

    @Test
    void testConvertInvalidCurrencyCode() throws Exception {
        // Create a ConversionDTO with an invalid currency code format
        final ConversionDTO conversionDTO = new ConversionDTO("ValidCountry", BigDecimal.valueOf(100.0), "Usd");

        // Perform a POST request to the endpoint with the invalid ConversionDTO as JSON
        mockMvc.perform(post("/api/v1/world/currency/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(conversionDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.msg").value("Error occurred"))
                .andExpect(jsonPath("$.errors.targetCurrency").value("Invalid currency code format"));
    }

    @Test
    void testConvertWhenCountryIsNull() throws Exception {
        // Create a ConversionDTO with an invalid currency code format
        final ConversionDTO conversionDTO = new ConversionDTO(null, BigDecimal.valueOf(100.0), "Usd");

        // Perform a POST request to the endpoint with the invalid ConversionDTO as JSON
        mockMvc.perform(post("/api/v1/world/currency/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(conversionDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.msg").value("Error occurred"))
                .andExpect(jsonPath("$.errors.country").value("must not be null"));
    }

    @Test
    void testConvertWhenAmountIsNull() throws Exception {
        // Create a ConversionDTO with an invalid currency code format
        final ConversionDTO conversionDTO = new ConversionDTO("ValidCountry", null, "Usd");

        // Perform a POST request to the endpoint with the invalid ConversionDTO as JSON
        mockMvc.perform(post("/api/v1/world/currency/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(conversionDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.msg").value("Error occurred"))
                .andExpect(jsonPath("$.errors.amount").value("must not be null"));
    }

    @Test
    void testConvertWhenTargetCurrencyIsNull() throws Exception {
        // Create a ConversionDTO with an invalid currency code format
        final ConversionDTO conversionDTO = new ConversionDTO("ValidCountry", BigDecimal.valueOf(100.0), null);

        // Perform a POST request to the endpoint with the invalid ConversionDTO as JSON
        mockMvc.perform(post("/api/v1/world/currency/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(conversionDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.msg").value("Error occurred"))
                .andExpect(jsonPath("$.errors.targetCurrency").value("must not be null"));
    }

    // Helper method to convert an object to JSON string
    private static String asJsonString(Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
