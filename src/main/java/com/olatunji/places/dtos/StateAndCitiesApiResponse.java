package com.olatunji.places.dtos;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@ToString
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StateAndCitiesApiResponse {
    private boolean error;
    private String msg;
    private Map<String, List<String>> data;
}
