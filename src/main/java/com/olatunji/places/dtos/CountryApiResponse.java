package com.olatunji.places.dtos;

import com.olatunji.places.models.Country;
import lombok.*;

@Getter
@ToString
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CountryApiResponse {
    private boolean error;
    private String msg;
    private Country data;
}
