package com.olatunji.world.api.controllers;

import com.olatunji.world.api.dtos.*;
import com.olatunji.world.api.models.City;
import com.olatunji.world.api.models.ConversionData;
import com.olatunji.world.api.services.WorldApiService;
import com.olatunji.world.api.models.CountryDetails;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/world")
@Api(value = "World API", description = "Operations related to world data")
public class ApiController {
    private final WorldApiService worldApiService;

    @Autowired
    public ApiController(WorldApiService worldApiService) {
        this.worldApiService = worldApiService;
    }

    @GetMapping("/most-populated-cities")
    @ApiOperation(value = "Get most populated cities", response = PopulationApiResponse.class)
    public ResponseEntity<PopulationApiResponse> getMostPopulatedCities(
            @ApiParam(value = "Number of cities to retrieve", required = true) @RequestParam int n) {
        final List<City> cities = worldApiService.getMostPopulousCity(n);
        log.debug("Most populated cities: {}", cities);
        return ResponseEntity.ok(PopulationApiResponse.builder()
                .error(false)
                .msg("filtered result")
                .data(cities)
                .build());
    }

    @GetMapping("/country/details")
    @ApiOperation(value = "Get country details", response = CountryDetailsApiResponse.class)
    public ResponseEntity<CountryDetailsApiResponse> getCountryDetails(
            @ApiParam(value = "Name of the country", required = true) @RequestParam String country) {
        final CountryDetails countryDetails = worldApiService.getCountryDetails(country);
        log.debug("Country details: {}", countryDetails);
        return ResponseEntity.ok(CountryDetailsApiResponse.builder()
                        .error(false)
                        .msg("Details about " + country)
                        .data(countryDetails)
                .build());
    }

    @GetMapping("/states/cities")
    @ApiOperation(value = "Get states and cities for a country", response = StateAndCitiesApiResponse.class)
    public ResponseEntity<StateAndCitiesApiResponse> getStatesAndCities(
            @ApiParam(value = "Name of the country", required = true) @RequestParam String country) {
        final Map<String, List<String>> countryMap = worldApiService.getStatesAndCities(country);
        log.debug("State and cities: {}", countryMap);
        return ResponseEntity.ok(StateAndCitiesApiResponse.builder()
                .error(false)
                .msg(country + " states and their cities retrieved")
                .data(countryMap)
                .build());
    }

    @PostMapping("/currency/convert")
    @ApiOperation(value = "Convert currency", response = CurrencyApiResponse.class)
    public ResponseEntity<CurrencyApiResponse> convert(
            @ApiParam(value = "Currency conversion details", required = true) @Valid @RequestBody final ConversionDTO conversionDTO) {
        final ConversionData conversionData = worldApiService.convert(conversionDTO);
        log.debug("Conversion data: {}", conversionData);
        if (conversionData == null) {
            return ResponseEntity.badRequest().body(CurrencyApiResponse.builder()
                    .error(false)
                    .msg("There is no conversion rate for the currency specified")
                    .build());
        }
        return ResponseEntity.ok(CurrencyApiResponse.builder()
                        .error(false)
                        .msg(conversionDTO.getCountry() + "'s currency was converted to "
                                + conversionDTO.getTargetCurrency())
                        .data(conversionData)
                .build());
    }

}
