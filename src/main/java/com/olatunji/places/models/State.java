package com.olatunji.places.models;

import lombok.*;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class State {
    private String name;
    private String state_code;
}
