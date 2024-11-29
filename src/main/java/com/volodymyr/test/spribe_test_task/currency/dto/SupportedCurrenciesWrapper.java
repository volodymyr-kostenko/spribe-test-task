package com.volodymyr.test.spribe_test_task.currency.dto;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SupportedCurrenciesWrapper {

  private final Map<String, String> symbols;
}
