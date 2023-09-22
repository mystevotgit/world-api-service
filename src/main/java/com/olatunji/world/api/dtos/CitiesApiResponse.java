package com.olatunji.world.api.dtos;

import lombok.*;

import java.util.List;

@Getter
@ToString
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CitiesApiResponse {
    private boolean error;
    private String msg;
    private List<String> data;
}
