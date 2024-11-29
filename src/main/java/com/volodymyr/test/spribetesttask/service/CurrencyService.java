package com.volodymyr.test.spribetesttask.service;

import com.volodymyr.test.spribetesttask.entity.RateEntity;
import com.volodymyr.test.spribetesttask.entity.SymbolEntity;
import com.volodymyr.test.spribetesttask.integration.FixerIntegrationService;
import com.volodymyr.test.spribetesttask.integration.model.RatesIntegration;
import com.volodymyr.test.spribetesttask.repository.RatesRepository;
import com.volodymyr.test.spribetesttask.repository.SymbolsRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyService {

  private final SymbolsRepository symbolsRepository;
  private final RatesRepository exchangeRateRepository;
  private final FixerIntegrationService fixerIntegrationService;

  private final ConcurrentHashMap<String, CurrencyData> ratesCache = new ConcurrentHashMap<>();
  private final RatesRepository ratesRepository;

  public Map<String, String> getAllCurrencies() {
    return ratesCache.entrySet().stream()
        .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getDescription()));
  }

  public CurrencyData getExchangeRates(String currencyCode) {
    return ratesCache.get(currencyCode);
  }

  @Transactional
  public void addCurrency(String symbol, String description) {
    //TODO save a new currency to the DB
    final Optional<SymbolEntity> existingSymbolEntity = symbolsRepository.findBySymbol(symbol);
    if (existingSymbolEntity.isPresent()) {
      log.info("Currency with symbol {} already exists", symbol);
      return;
    }

    //try synchonization by a map

    final List<SymbolEntity> allSymbolEntities = symbolsRepository.findAll();

    final Optional<RatesIntegration> ratesIntegrationOptional = fixerIntegrationService.getRates(
        symbol,
        allSymbolEntities.stream().map(SymbolEntity::getSymbol).toList());

    ratesIntegrationOptional.ifPresent(ratesIntegration -> {
      final SymbolEntity newSymbolEntity = symbolsRepository.save(
          new SymbolEntity(UUID.randomUUID(), symbol, description));

      if (!allSymbolEntities.isEmpty()) {
        final List<RateEntity> rateEntities = ratesIntegration.getRates().entrySet().stream()
            .map(symbolToRate -> {
                  final SymbolEntity targetSymbolEntity = allSymbolEntities.stream()
                      .filter(symbolEntity -> symbolEntity.getSymbol().equals(symbolToRate.getKey()))
                      .findFirst().get();

                  return new RateEntity(UUID.randomUUID(), newSymbolEntity, targetSymbolEntity,
                      symbolToRate.getValue(), ratesIntegration.getDate());
                }

            ).toList();

        ratesRepository.saveAll(rateEntities);
      }

      //update caches
      ratesCache.put(symbol,
          new CurrencyData(description, ratesIntegration.getDate(), ratesIntegration.getRates()));
    });
  }

  @Scheduled(fixedRate = 3600000) // 1 hour
  public void fetchAndStoreExchangeRates() {
    loadRates();
  }

  public void loadRates() {
    // warm up cache

  }
}
