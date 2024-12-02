package com.volodymyr.test.spribetesttask.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CurrencyData {

  private final String description;

  private final Date date;

  private final Map<String, BigDecimal> rates;
}
