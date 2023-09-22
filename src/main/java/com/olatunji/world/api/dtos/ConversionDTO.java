package com.olatunji.world.api.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversionDTO {
    @NotNull
    @Pattern(regexp = "^[A-Za-z\\s\\-']+$", message = "Invalid country name")
    @ApiModelProperty(value = "The name of the country whose currency you want to convert", example = "United States")
    private String country;
    @NotNull
    @PositiveOrZero(message = "Value must be non-negative")
    @ApiModelProperty(value = "Amount you want to convert", example = "100")
    private BigDecimal amount;
    @NotNull
    @Pattern(regexp = "^[A-Z]{3}$", message = "Invalid currency code format")
    @ApiModelProperty(value = "The currency you want to convert to", example = "NGN")
    private String targetCurrency;
}
