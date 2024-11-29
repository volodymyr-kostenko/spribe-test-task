package com.volodymyr.test.spribetesttask.integration.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatesIntegration {

  private boolean success;
  private Long timestamp;
  private Date date;
  private String base;
  private Map<String, BigDecimal> rates;
}
