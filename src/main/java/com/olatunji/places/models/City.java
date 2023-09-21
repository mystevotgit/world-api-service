package com.olatunji.places.models;

import lombok.*;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class City {
    private String city;
    private String country;
    private List<Population> populationCounts;

}
