package com.olatunji.world.api.dtos;

import com.olatunji.world.api.models.Country;
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
