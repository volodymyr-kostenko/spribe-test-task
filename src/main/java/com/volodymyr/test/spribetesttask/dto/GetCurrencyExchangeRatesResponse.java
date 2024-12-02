package com.volodymyr.test.spribetesttask.dto;

import java.math.BigDecimal;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetCurrencyExchangeRatesResponse {

  private final String base;
  private final String description;
  private final long timestamp;
  private final Map<String, BigDecimal> rates;
}
