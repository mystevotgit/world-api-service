package com.olatunji.world.api.models;

import lombok.*;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Population {
    private String year;
    private String value;
}
