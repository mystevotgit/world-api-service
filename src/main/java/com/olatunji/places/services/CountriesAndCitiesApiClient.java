package com.olatunji.places.services;

import com.olatunji.places.dtos.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class CountriesAndCitiesApiClient {

    private static final String BASE_URL = "https://countriesnow.space";
    public static final String COUNTRIES_CITIES_POPULATION_FILTER = "api/v0.1/countries/population/cities/filter";
    public static final String COUNTRY_POPULATION_FILTER = "api/v0.1/countries/population";
    public static final String COUNTRY_CAPITAL_FILTER = "api/v0.1/countries/capital";
    public static final String COUNTRY_LOCATION_FILTER = "api/v0.1/countries/positions";
    public static final String SINGLE_COUNTRY_FILTER = "api/v0.1/countries/states";
    public static final String SINGLE_STATE_CITIES_FILTER = "api/v0.1/countries/state/cities";

    public static final String COUNTRY_CURRENCY_FILTER = "api/v0.1/countries/currency";
    public static final String COUNTRY_QUERY_TEMPLATE = "q?country=%s";
    public static final String COUNTRY_STATE_QUERY_TEMPLATE = "q?country=%s&state=%s";

    @Getter
    private final RestTemplate restTemplate;
    private final HttpEntity<HttpHeaders> entity;

    @Autowired
    public CountriesAndCitiesApiClient(final RestTemplate restTemplate) {
        this.entity = createHttpEntity();
        this.restTemplate = restTemplate;
    }

    private HttpEntity<HttpHeaders> createHttpEntity() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(headers);
    }

    public ResponseEntity<PopulationApiResponse> getCitiesPopulation(String country) {
        String query = String.format(COUNTRY_QUERY_TEMPLATE, country);
        final URI uri = URI.create(String.join("/", BASE_URL, COUNTRIES_CITIES_POPULATION_FILTER, query.replace(" ", "%20")));
        return restTemplate.exchange(uri, HttpMethod.GET, entity, PopulationApiResponse.class);
    }

    public ResponseEntity<CitiesApiResponse> getStateCities (String country, String state) {
        String query = String.format(COUNTRY_STATE_QUERY_TEMPLATE, country, state);
        final URI uri = URI.create(String.join("/", BASE_URL, SINGLE_STATE_CITIES_FILTER, query.replace(" ", "%20")));
        return restTemplate.exchange(uri, HttpMethod.GET, entity, CitiesApiResponse.class);
    }

    public ResponseEntity<CountryApiResponse> getCountry (String countryName) {
        String query = String.format(COUNTRY_QUERY_TEMPLATE, countryName);
        final URI url = URI.create(String.join("/", BASE_URL, SINGLE_COUNTRY_FILTER, query.replace(" ", "%20")));
        return restTemplate.exchange(url, HttpMethod.GET, entity, CountryApiResponse.class);
    }

    public ResponseEntity<CountryPopulationApiResponse> getCountryCapital(final String country) {
        String query = String.format(COUNTRY_QUERY_TEMPLATE, country);
        final URI uri = URI.create(String.join("/", BASE_URL, COUNTRY_CAPITAL_FILTER, query.replace(" ", "%20")));
        return restTemplate.exchange(uri, HttpMethod.GET, entity, CountryPopulationApiResponse.class);
    }

    public ResponseEntity<String> getLocation(final String country) {
        String query = String.format(COUNTRY_QUERY_TEMPLATE, country);
        final URI locationUri = URI.create(String.join("/", BASE_URL, COUNTRY_LOCATION_FILTER, query.replace(" ", "%20")));
        return restTemplate.exchange(locationUri, HttpMethod.GET, entity, String.class);
    }

    public ResponseEntity<CountryDetailsApiResponse> getCountryCurrency(String country) {
        String query = String.format(COUNTRY_QUERY_TEMPLATE, country);
        final URI uri = URI.create(String.join("/", BASE_URL, COUNTRY_CURRENCY_FILTER, query.replace(" ", "%20")));
        return restTemplate.exchange(uri, HttpMethod.GET, entity, CountryDetailsApiResponse.class);
    }

    public ResponseEntity<CountryPopulationApiResponse> getCountryPopulation(String country) {
        String query = String.format(COUNTRY_QUERY_TEMPLATE, country);
        final URI uri = URI.create(String.join("/", BASE_URL, COUNTRY_POPULATION_FILTER, query.replace(" ", "%20")));
        return restTemplate.exchange(uri, HttpMethod.GET, entity, CountryPopulationApiResponse.class);
    }
}
