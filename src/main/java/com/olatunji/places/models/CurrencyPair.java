package com.olatunji.places.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public enum CurrencyPair {
    EUR_TO_NGN("EUR", "NGN", new BigDecimal("493.06")),
    NGN_TO_EUR("NGN","EUR",  new BigDecimal("0.002028151")),
    USD_TO_NGN("USD", "NGN", new BigDecimal("460.72")),
    NGN_TO_USD("NGN","USD",  new BigDecimal("0.002170516")),
    JPY_TO_NGN("JPY", "NGN", new BigDecimal("3.28")),
    NGN_TO_JPY("NGN","JPY",  new BigDecimal("0.304878049")),
    GBP_TO_NGN("GBP", "NGN", new BigDecimal("570.81")),
    NGN_TO_GBP("NGN","GBP",  new BigDecimal("0.001751896")),
    EUR_TO_UGX("EUR", "UGX", new BigDecimal("4")),
    UGX_TO_EUR("UGX","EUR",  new BigDecimal("0.25")),
    USD_TO_UGX("USD", "UGX", new BigDecimal("3")),
    UGX_TO_USD("UGX","USD",  new BigDecimal("0.333333333")),
    JPY_TO_UGX("JPY", "UGX", new BigDecimal("26.62")),
    UGX_TO_JPY("UGX","JPY",  new BigDecimal("0.037565740")),
    GBP_TO_UGX("GBP", "UGX", new BigDecimal("4")),
    UGX_TO_GBP("UGX","GBP",  new BigDecimal("0.25")),
    UGX_TO_NGN("UGX","NGN",  new BigDecimal("153.573333333")),
    NGN_TO_UGX("UGX","NGN",  new BigDecimal("0.006511547"));

    private final String from;
    private final String to;
    private final BigDecimal rate;

    public static CurrencyPair getCurrencyPair(final String from, final String to) {
        for (final CurrencyPair currencyPair : values()) {
            if (currencyPair.from.equals(from) && currencyPair.to.equals(to)) {
                return currencyPair;
            }
        }
        return null;
    }
}
