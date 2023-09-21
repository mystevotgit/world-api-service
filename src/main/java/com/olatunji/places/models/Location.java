package com.olatunji.places.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Location {
    private int longitude;
    private int latitude;
}
