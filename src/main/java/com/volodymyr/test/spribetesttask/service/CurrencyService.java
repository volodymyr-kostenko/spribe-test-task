package com.volodymyr.test.spribetesttask.service;

import com.volodymyr.test.spribetesttask.repository.RatesRepository;
import com.volodymyr.test.spribetesttask.repository.SymbolsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CurrencyService {

  private final SymbolsRepository currencyRepository;
  private final RatesRepository exchangeRateRepository;
  private final RestTemplate restTemplate;

//  public List<Currency> getAllCurrencies() {
//    return currencyRepository.findAll();
//  }
//
//  public Map<String, Double> getExchangeRates(String currencyCode) {
//    // Logic to get rates from memory Map or fetch from DB
//  }
//
//  public Currency addCurrency(String code, String name) {
//    // Save new currency to DB
//  }

  @Scheduled(fixedRate = 3600000) // 1 hour
  public void fetchAndStoreExchangeRates() {
    // Logic to fetch exchange rates from external API and store in DB
  }
}
