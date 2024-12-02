package com.volodymyr.test.spribetesttask.controller;

import com.volodymyr.test.spribetesttask.dto.Currency;
import com.volodymyr.test.spribetesttask.dto.ExchangeRatesWrapper;
import com.volodymyr.test.spribetesttask.dto.SupportedCurrenciesWrapper;
import com.volodymyr.test.spribetesttask.service.CurrencyData;
import com.volodymyr.test.spribetesttask.service.CurrencyService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController("/api")
public class CurrencyController {

  private final CurrencyService currencyService;

  @GetMapping(value = "/currencies", produces = "application/json")
  public ResponseEntity<SupportedCurrenciesWrapper> getAllCurrencies() {
    return ResponseEntity.ok(new SupportedCurrenciesWrapper(currencyService.getAllCurrencies()));
  }

  @GetMapping("/currencies/{currency}/rates")
  public ResponseEntity<ExchangeRatesWrapper> getExchangeRateForCurrency(
      @PathVariable String currency) {
    final Optional<CurrencyData> currencyData = currencyService.getExchangeRates(currency);

    return currencyData.map(data -> ResponseEntity.ok(
                new ExchangeRatesWrapper(
                    currency,
                    data.getDescription(),
                    data.getDate().getTime(),
                    data.getRates()
                )
            )
        )
        .orElseGet(() -> ResponseEntity.notFound().build());

  }

  @PostMapping("/currencies")
  public ResponseEntity<Object> addNewCurrency(@RequestBody Currency newCurrency) {
    currencyService.addCurrency(newCurrency.getCurrency(), newCurrency.getDescription());

    return ResponseEntity.ok().build();
  }
}
