package com.volodymyr.test.spribetesttask.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateCurrencyRequest {

  private final String currency;
  private final String description;
}
