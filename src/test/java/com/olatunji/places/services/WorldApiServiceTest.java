package com.olatunji.places.services;

import com.olatunji.places.dtos.*;
import com.olatunji.places.models.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorldApiServiceTest {

    @Mock
    private CountriesAndCitiesApiClient countriesAndCitiesApiClient;

    @InjectMocks
    private WorldApiService worldApiService;

    @Test
    public void testGetMostPopulousCity() {
        // Mock the getCitiesPopulation method
        when(countriesAndCitiesApiClient.getCitiesPopulation("Italy")).thenReturn(stubPopulationApiResponse());
        when(countriesAndCitiesApiClient.getCitiesPopulation("New Zealand")).thenReturn(stubPopulationApiResponse());
        when(countriesAndCitiesApiClient.getCitiesPopulation("Ghana")).thenReturn(stubPopulationApiResponse());

        // Test the getMostPopulousCity method
        List<City> mostPopulousCities = worldApiService.getMostPopulousCity(2);

        // Verify Interactions with getCitiesPopulation method in countriesAndCitiesApiClient
        verify(countriesAndCitiesApiClient, times(3)).getCitiesPopulation(anyString());

        // Verify that the method returns the expected number of cities
        assertEquals(2, mostPopulousCities.size());

        // Verify the order of cities (descending order by population)
        final int first = Integer.parseInt(mostPopulousCities.get(0).getPopulationCounts().get(0).getValue());
        final int second = Integer.parseInt(mostPopulousCities.get(1).getPopulationCounts().get(0).getValue());
        assertThat(first).isGreaterThan(second);
    }

    @Test
    public void testGetStatesAndCities() {
        // Mock the getCountry method to return a country with states
        final ResponseEntity<CountryApiResponse> response = stubCountryApiResponse();
        when(countriesAndCitiesApiClient.getCountry(anyString())).thenReturn(response);

        // Mock the getStateCities method for each state
        when(countriesAndCitiesApiClient.getStateCities(anyString(), eq("State1"))).thenReturn(
                ResponseEntity.ok(CitiesApiResponse.builder()
                        .error(false)
                        .data(Arrays.asList("CityA", "CityB"))
                        .build()));
        when(countriesAndCitiesApiClient.getStateCities(anyString(), eq("State2"))).thenReturn(
                ResponseEntity.ok(CitiesApiResponse.builder()
                        .error(false)
                        .data(Arrays.asList("CityX", "CityY"))
                        .build()));

        // Test the getStatesAndCities method
        Map<String, List<String>> stateCitiesMap = worldApiService.getStatesAndCities("CountryName");

        // Verify Interactions with getStateCities method in countriesAndCitiesApiClient
        verify(countriesAndCitiesApiClient, times(2)).getStateCities(anyString(), anyString());

        // Verify that the map contains the expected states and cities
        assertTrue(stateCitiesMap.containsKey("State1"));
        assertTrue(stateCitiesMap.containsKey("State2"));
        assertEquals(2, stateCitiesMap.get("State1").size());
        assertEquals(2, stateCitiesMap.get("State2").size());
        assertEquals("CityA", stateCitiesMap.get("State1").get(0));
        assertEquals("CityX", stateCitiesMap.get("State2").get(0));
    }

    @Test
    public void testConvertSameCurrency() {
        // Create a ConversionDTO with the same target currency as the country's currency
        ConversionDTO conversionDTO = new ConversionDTO("United States", BigDecimal.valueOf(100.0), "USD");

        // Mock the getCountryCurrency method to set the same currency as the target
        when(countriesAndCitiesApiClient.getCountryCurrency(anyString())).thenReturn(
                ResponseEntity.ok(CountryDetailsApiResponse.builder()
                                .error(false)
                                .data(CountryDetails.builder()
                                        .currency("USD")
                                        .build())
                                .build()));

        // Test the convert method
        ConversionData conversionData = worldApiService.convert(conversionDTO);

        // Verify Interactions with getCountryCurrency method in countriesAndCitiesApiClient
        verify(countriesAndCitiesApiClient, times(1)).getCountryCurrency(anyString());

        // Verify that the converted amount is the same as the input amount
        assertNotNull(conversionData);
        assertEquals(BigDecimal.valueOf(100.0), conversionData.getAmount());
        assertEquals("USD", conversionData.getCurrency());
    }

    @Test
    public void testConvertDifferentCurrency() {
        // Create a ConversionDTO with the same target currency
        ConversionDTO conversionDTO = new ConversionDTO("United States", BigDecimal.valueOf(100.0), "NGN");

        // Mock the getCountryCurrency method
        when(countriesAndCitiesApiClient.getCountryCurrency(anyString())).thenReturn(
                ResponseEntity.ok(CountryDetailsApiResponse.builder()
                        .error(false)
                        .data(CountryDetails.builder()
                                .currency("USD")
                                .build())
                        .build()));

        // Test the convert method
        ConversionData conversionData = worldApiService.convert(conversionDTO);

        // Verify Interactions with getCountryCurrency method in countriesAndCitiesApiClient
        verify(countriesAndCitiesApiClient, times(1)).getCountryCurrency(anyString());

        // Verify that the converted amount is the same as the input amount
        assertNotNull(conversionData);
        assertEquals(new BigDecimal("46072.000"), conversionData.getAmount());
        assertEquals("NGN", conversionData.getCurrency());
    }

    @Test
    public void testGetCountryDetails() {
        // Mock the response for getting the country's capital
        when(countriesAndCitiesApiClient.getCountryCapital("Nigeria"))
                .thenReturn(ResponseEntity.ok(CountryPopulationApiResponse.builder()
                                .error(false)
                                .data(CountryDetail.builder()
                                        .capital("Abuja")
                                        .build())
                                .build()));

        // Mock the response for getting the country's capital
        when(countriesAndCitiesApiClient.getCountryPopulation("Nigeria"))
                .thenReturn(ResponseEntity.ok(CountryPopulationApiResponse.builder()
                                .error(false)
                                .data(CountryDetail.builder()
                                        .populationCounts(List.of(Population.builder()
                                                .value("195000000")
                                                .build()))
                                        .build())
                                .build()));

        // Mock the response for getting the country's location
        String locationResponseJson = "{\"data\":{\"long\":8,\"lat\":10},\"error\":false}";
        when(countriesAndCitiesApiClient.getLocation("Nigeria"))
                .thenReturn(ResponseEntity.ok(locationResponseJson));

        // Mock the response for getting the country's currency details
        when(countriesAndCitiesApiClient.getCountryCurrency("Nigeria"))
                .thenReturn(ResponseEntity.ok(CountryDetailsApiResponse.builder()
                        .error(false)
                        .data(CountryDetails.builder()
                                .ISO2("NG")
                                .ISO3("NGA")
                                .currency("NGN")
                                .build())
                        .build()));

        // Call the getCountryDetails method
        CountryDetails countryDetails = worldApiService.getCountryDetails("Nigeria");

        // Verify Interactions
        verify(countriesAndCitiesApiClient, times(1)).getCountryCapital(anyString());
        verify(countriesAndCitiesApiClient, times(1)).getCountryPopulation(anyString());
        verify(countriesAndCitiesApiClient, times(1)).getLocation(anyString());
        verify(countriesAndCitiesApiClient, times(1)).getCountryCurrency(anyString());

        // Verify that the CountryDetails object is correctly enriched
        assertNotNull(countryDetails);
        assertEquals("Abuja", countryDetails.getCapitalCity());
        assertEquals(8, countryDetails.getLocation().getLongitude());
        assertEquals(10, countryDetails.getLocation().getLatitude());
        assertEquals("NGN", countryDetails.getCurrency());
        assertEquals("NG", countryDetails.getISO2());
        assertEquals("NGA", countryDetails.getISO3());
    }

    private static ResponseEntity<CountryApiResponse> stubCountryApiResponse() {
        final Country country = new Country();
        final List<State> states = Arrays.asList(new State("State1", "S1"), new State("State2","S2"));
        country.setStates(states);
        return ResponseEntity.ok(CountryApiResponse.builder()
                .error(false)
                .data(country)
                .build());
    }

    private ResponseEntity<PopulationApiResponse> stubPopulationApiResponse() {
        return ResponseEntity.ok(PopulationApiResponse.builder()
                .error(false)
                .msg("filtered result")
                .data(Arrays.asList(
                        new City("City1", "Country", List.of(new Population("1993", String.valueOf((int) (Math.random() * 8000) + 1000)))),
                        new City("City2", "Country", List.of(new Population("1993", String.valueOf((int) (Math.random() * 8000) + 1000)))),
                        new City("City3", "Country", List.of(new Population("1993", String.valueOf((int) (Math.random() * 8000) + 1000))))
                ))
                .build());
    }

}