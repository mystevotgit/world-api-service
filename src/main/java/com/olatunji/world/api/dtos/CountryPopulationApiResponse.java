package com.olatunji.world.api.dtos;

import com.olatunji.world.api.models.CountryDetail;
import lombok.*;

@Getter
@ToString
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CountryPopulationApiResponse {
    private boolean error;
    private String msg;
    private CountryDetail data;
}
