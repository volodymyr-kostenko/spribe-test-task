package com.volodymyr.test.spribe_test_task.currency.controller;

import com.volodymyr.test.spribe_test_task.currency.dto.ExchangeRatesWrapper;
import com.volodymyr.test.spribe_test_task.currency.dto.Currency;
import com.volodymyr.test.spribe_test_task.currency.dto.SupportedCurrenciesWrapper;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping(produces = "application/json")
public class CurrencyController {

  @GetMapping(value = "/currencies", produces = "application/json")
  public ResponseEntity<SupportedCurrenciesWrapper> getCurrencies() {
    return ResponseEntity.ok(new SupportedCurrenciesWrapper(Map.of("USD", "United States Dollar")));
  }

  @GetMapping("/currencies/{currency}/rates")
  public ResponseEntity<ExchangeRatesWrapper> getExchangeRateForCurrency(
      @PathVariable String currency) {
    return ResponseEntity.ok(new ExchangeRatesWrapper("USD", 1617225600,
        Map.of("UAH", new java.math.BigDecimal("27.5"))));
  }

  @PostMapping("/currencies")
  public ResponseEntity addNewCurrency(@RequestBody Currency newCurrency) {
    // add new currency
    return ResponseEntity.ok().build();
  }
}
