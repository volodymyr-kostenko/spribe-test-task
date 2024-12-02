package com.volodymyr.test.spribetesttask.service;

import com.volodymyr.test.spribetesttask.entity.CurrencyEntity;
import com.volodymyr.test.spribetesttask.integration.FixerIntegrationService;
import com.volodymyr.test.spribetesttask.repository.CurrenciesRepository;
import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyService {

  private final CurrenciesRepository currenciesRepository;
  private final FixerIntegrationService fixerIntegrationService;

  @Getter
  private final ConcurrentHashMap<String, CurrencyData> ratesCache = new ConcurrentHashMap<>();

  public Map<String, String> getAllCurrencies() {
    return ratesCache.entrySet().stream()
        .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getDescription()));
  }

  public CurrencyData getExchangeRates(String currencyCode) {
    return ratesCache.get(currencyCode);
  }

  @Transactional
  public void addCurrency(String symbol, String description) {
    final Optional<CurrencyEntity> existingSymbolEntity = currenciesRepository.findBySymbol(symbol);
    if (existingSymbolEntity.isPresent()) {
      log.info("Currency with symbol {} already exists", symbol);
      return;
    }

    fixerIntegrationService.getRates(symbol).ifPresent(ratesIntegration -> {
      final Map<String, BigDecimal> rates = ratesIntegration.getRates();

      final CurrencyEntity currencyEntity = CurrencyEntity.builder()
          .id(UUID.randomUUID())
          .symbol(symbol)
          .description(description)
          .rates(rates)
          .build();

      currenciesRepository.save(currencyEntity);

      ratesCache.put(symbol,
          new CurrencyData(description, ratesIntegration.getDate(), rates));
    });
  }

  @Transactional
  @Scheduled(cron = "0 0 * * * *") // every hour
  public void fetchAndStoreExchangeRates() {
    log.info("Fetching and storing exchange rates");

    currenciesRepository.findAll().forEach(currencyEntity -> {
      fixerIntegrationService.getRates(currencyEntity.getSymbol()).ifPresent(ratesIntegration -> {
        currencyEntity.setRates(ratesIntegration.getRates());
        currencyEntity.setUpdatedAt(ratesIntegration.getDate());
        currenciesRepository.save(currencyEntity);

        ratesCache.put(currencyEntity.getSymbol(),
            new CurrencyData(currencyEntity.getDescription(), ratesIntegration.getDate(),
                ratesIntegration.getRates()));
      });
    });
  }

  @PostConstruct
  public void loadRates() {
    log.info("Loading rates from database");

    currenciesRepository.findAll().forEach(currencyEntity -> {
      final CurrencyData currencyData = new CurrencyData(currencyEntity.getDescription(),
          currencyEntity.getUpdatedAt(),
          currencyEntity.getRates());
      ratesCache.put(currencyEntity.getSymbol(), currencyData);
    });
  }
}
