package com.olatunji.places.models;

import lombok.*;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CountryDetails {
    private Integer population;
    private String capitalCity;
    private Location location;
    private String currency;
    private String ISO2;
    private String ISO3;

}
