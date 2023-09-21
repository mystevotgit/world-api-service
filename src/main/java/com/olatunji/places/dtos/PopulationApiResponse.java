package com.olatunji.places.dtos;

import com.olatunji.places.models.City;
import lombok.*;

import java.util.List;

@Getter
@ToString
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PopulationApiResponse {
    private boolean error;
    private String msg;
    private List<City> data;
}
