package com.volodymyr.test.spribetesttask.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.volodymyr.test.spribetesttask.entity.CurrencyEntity;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CurrenciesRepositoryIntegrationTest {

  @Autowired
  private CurrenciesRepository currenciesRepository;

  @Test
  @Transactional
  void symbolEntityIsRetrieved() {
    final CurrencyEntity currencyEntity = CurrencyEntity.builder()
        .id(UUID.randomUUID())
        .symbol("USD")
        .description("United States Dollar")
        .rates(Map.of("EUR", new BigDecimal("0.8")))
        .updatedAt(new Date())
        .build();
    currenciesRepository.save(currencyEntity);

    final Optional<CurrencyEntity> retrievedCurrencyEntity = currenciesRepository.findById(
        currencyEntity.getId());

    assertThat(retrievedCurrencyEntity).isNotEmpty().get().satisfies(entity -> {
      assertThat(entity.getSymbol()).isEqualTo(currencyEntity.getSymbol());
      assertThat(entity.getDescription()).isEqualTo(currencyEntity.getDescription());
      assertThat(entity.getRates()).isEqualTo(currencyEntity.getRates());
      assertThat(entity.getUpdatedAt().getTime()).isEqualTo(currencyEntity.getUpdatedAt().getTime());
    });
  }

}
