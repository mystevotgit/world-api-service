package com.olatunji.world.api.dtos;

import com.olatunji.world.api.models.ConversionData;
import lombok.*;

@Getter
@ToString
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CurrencyApiResponse {
    private boolean error;
    private String msg;
    private ConversionData data;
}
