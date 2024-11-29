package com.volodymyr.test.spribetesttask.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class FixerIntegrationService {

  private final RestTemplate restTemplate;
  private final String baseUrl = "https://data.fixer.io";
  private final String apiKey = "";

  public void getSymbols() {
    final ResponseEntity<String> response = restTemplate.getForEntity(
        baseUrl + "/api/symbols?access_key=" + apiKey, String.class);
  }

  public void getExchangeRatesForCurrency(String currency) {
    // get exchange rates for currency
  }

  public void addCurrency(String currency, String description) {
    // add currency
  }
}
