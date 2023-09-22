package com.olatunji.world.api.models;

import lombok.*;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class State {
    private String name;
    private String state_code;
}
