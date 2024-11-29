package com.volodymyr.test.spribe_test_task.currency.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Currency {

  private final String currency;
  private final String description;
}
