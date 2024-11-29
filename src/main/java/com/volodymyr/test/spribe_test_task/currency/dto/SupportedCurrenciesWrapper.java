package com.volodymyr.test.spribe_test_task.currency.dto;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

//@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class SupportedCurrenciesWrapper {

  private Map<String, String> symbols;
}
