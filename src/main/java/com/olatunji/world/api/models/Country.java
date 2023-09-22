package com.olatunji.world.api.models;

import lombok.*;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Country {
    private String name;
    private String iso3;
    private List<State> states;
}
