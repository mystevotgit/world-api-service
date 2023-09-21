package com.olatunji.places.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.olatunji.places.dtos.*;
import com.olatunji.places.models.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class WorldApiService {

    private static final List<String> SPECIFIED_COUNTRIES = Arrays.asList("Italy", "New Zealand", "Ghana");

    private final CountriesAndCitiesApiClient countriesAndCitiesApiClient;

    public WorldApiService(CountriesAndCitiesApiClient countriesAndCitiesApiClient) {
        this.countriesAndCitiesApiClient = countriesAndCitiesApiClient;
    }

    public List<City> getMostPopulousCity(final int n) {
        final List<City> cities = getCitiesInSpecifiedCountries();
        cities.sort(Comparator.comparing(city -> Integer.parseInt(city.getPopulationCounts().get(0).getValue())));
        Collections.reverse(cities);
        if (cities.size() > n) {
            return cities.subList(0, n);
        }
        return cities;
    }

    /* Consideration
     * If there will be a frequent call to this method, Caching can be considered since the countries whose cities
     * are being fetched are limited to 3 and the quantity of their cities rarely change.
     */
    public List<City> getCitiesInSpecifiedCountries() {
        final List<City> allCities = new ArrayList<>();

        for (final String country : SPECIFIED_COUNTRIES) {
            final List<City> cities = getCities(country);
            allCities.addAll(cities);
        }
        return allCities;
    }

    private List<City> getCities(final String country) {
        final ResponseEntity<PopulationApiResponse> response = countriesAndCitiesApiClient.getCitiesPopulation(country);

        if (response.getStatusCode() == HttpStatus.OK) {
            final PopulationApiResponse populationApiResponse = response.getBody();
            if (populationApiResponse != null && !populationApiResponse.isError()) {
                return populationApiResponse.getData();
            }
        }
        return List.of();
    }

    public Map<String, List<String>> getStatesAndCities(String countryName) {
        final Map<String, List<String>> countryMap = new HashMap<>();
        final Country country = getCountry(countryName);
        assert country != null;
        final List<State> states = country.getStates();
        for (final State state : states) {
            final List<String> cities = getStateCities(countryName, state.getName());
            countryMap.putIfAbsent(state.getName(), cities);
        }
        return countryMap;
    }

    private List<String> getStateCities(final String country, final String state) {
        final ResponseEntity<CitiesApiResponse> response = countriesAndCitiesApiClient.getStateCities(country, state);

        if (response.getStatusCode() == HttpStatus.OK) {
            final CitiesApiResponse citiesApiResponse = response.getBody();
            if (citiesApiResponse != null && !citiesApiResponse.isError()) {
                return citiesApiResponse.getData();
            }
        }
        return List.of();
    }

    private Country getCountry(String countryName) {
        final ResponseEntity<CountryApiResponse> response = countriesAndCitiesApiClient.getCountry(countryName);

        if (response.getStatusCode() == HttpStatus.OK) {
            final CountryApiResponse countryApiResponse = response.getBody();
            if (countryApiResponse != null && !countryApiResponse.isError()) {
                return countryApiResponse.getData();
            }
        }
        return null;
    }

    public CountryDetails getCountryDetails(String country) {
        CountryDetails countryDetails = CountryDetails.builder().build();
        enrichCountryDetailsWithPopulation(country, countryDetails);
        enrichCountryDetailsWithCurrencyIso2AndIso3(country, countryDetails);
        enrichCountryDetailsWithCapitalAndLocation(country, countryDetails);
        return countryDetails;
    }

    private void enrichCountryDetailsWithCapitalAndLocation(final String country, final CountryDetails countryDetails) {
        final ResponseEntity<CountryPopulationApiResponse> response = countriesAndCitiesApiClient.getCountryCapital(country);

        if (response.getStatusCode() == HttpStatus.OK) {
            final CountryPopulationApiResponse countryPopulationApiResponse = response.getBody();
            if (countryPopulationApiResponse != null && !countryPopulationApiResponse.isError()) {
                final CountryDetail countryDetail = countryPopulationApiResponse.getData();
                countryDetails.setCapitalCity(countryDetail.getCapital());
            }
        }

        final ResponseEntity<String> locationResponse = countriesAndCitiesApiClient.getLocation(country);

        if (locationResponse.getStatusCode() == HttpStatus.OK) {
            final String locationResponseBody = locationResponse.getBody();
            if (locationResponseBody != null && locationResponseBody.contains("false")) {
                final ObjectMapper objectMapper = new ObjectMapper();
                try {
                    final JsonNode root = objectMapper.readTree(locationResponseBody);
                    final JsonNode data = root.get("data");
                    final int longitude = data.get("long").asInt();
                    final int latitude = data.get("lat").asInt();
                    countryDetails.setLocation(Location.builder()
                            .longitude(longitude)
                            .latitude(latitude)
                            .build());
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void enrichCountryDetailsWithCurrencyIso2AndIso3(final String country, final CountryDetails countryDetails) {
        final ResponseEntity<CountryDetailsApiResponse> response = countriesAndCitiesApiClient.getCountryCurrency(country);

        if (response.getStatusCode() == HttpStatus.OK) {
            final CountryDetailsApiResponse countryDetailsApiResponse = response.getBody();
            if (countryDetailsApiResponse != null && !countryDetailsApiResponse.isError()) {
                countryDetails.setCurrency(countryDetailsApiResponse.getData().getCurrency());
                countryDetails.setISO2(countryDetailsApiResponse.getData().getISO2());
                countryDetails.setISO3(countryDetailsApiResponse.getData().getISO3());
            }
        }
    }

    private void enrichCountryDetailsWithPopulation(final String country, final CountryDetails countryDetails) {
        final ResponseEntity<CountryPopulationApiResponse> response = countriesAndCitiesApiClient.getCountryPopulation(country);

        if (response.getStatusCode() == HttpStatus.OK) {
            final CountryPopulationApiResponse countryPopulationApiResponse = response.getBody();
            if (countryPopulationApiResponse != null && !countryPopulationApiResponse.isError()) {
                final CountryDetail countryDetail = countryPopulationApiResponse.getData();
                final Population population = countryDetail.getPopulationCounts().get(countryDetail.getPopulationCounts().size()-1);
                countryDetails.setPopulation(Integer.parseInt(population.getValue()));
            }
        }
    }

    public ConversionData convert(ConversionDTO conversionDTO) {
        final CountryDetails countryDetails = CountryDetails.builder().build();
        enrichCountryDetailsWithCurrencyIso2AndIso3(conversionDTO.getCountry(), countryDetails);
        if (countryDetails.getCurrency().equals(conversionDTO.getTargetCurrency())) {
            return ConversionData.builder()
                    .amount(conversionDTO.getAmount())
                    .currency(conversionDTO.getTargetCurrency())
                    .build();
        }
        final CurrencyPair currencyPair = CurrencyPair.getCurrencyPair(countryDetails.getCurrency(), conversionDTO.getTargetCurrency());
        if (currencyPair != null) {
            final BigDecimal convertedAmount = conversionDTO.getAmount().multiply(currencyPair.getRate());
            return ConversionData.builder()
                    .amount(convertedAmount)
                    .currency(conversionDTO.getTargetCurrency())
                    .build();
        }
        return null;
    }
}
