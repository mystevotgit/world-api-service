package com.olatunji.world.api.models;

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
