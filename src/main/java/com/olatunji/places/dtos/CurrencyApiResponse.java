package com.olatunji.places.dtos;

import com.olatunji.places.models.ConversionData;
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
