package com.olatunji.places.models;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ConversionData {
    private String currency;
    private BigDecimal amount;
}
