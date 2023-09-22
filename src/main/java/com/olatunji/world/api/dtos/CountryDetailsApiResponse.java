package com.olatunji.world.api.dtos;

import com.olatunji.world.api.models.CountryDetails;
import lombok.*;

@Getter
@ToString
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CountryDetailsApiResponse {
    private boolean error;
    private String msg;
    private CountryDetails data;
}
