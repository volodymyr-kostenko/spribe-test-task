package com.volodymyr.test.spribe_test_task.currency.dto;

import java.math.BigDecimal;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ExchangeRatesWrapper {

  private final String base;
  private final long timestamp;
  private final Map<String, BigDecimal> rates;
}
