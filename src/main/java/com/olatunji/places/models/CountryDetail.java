package com.olatunji.places.models;

import lombok.*;

import java.util.List;

@Getter
@ToString
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CountryDetail {
    private String country;
    private String code;
    private String iso3;
    private List<Population> populationCounts;
    private String capital;
}
