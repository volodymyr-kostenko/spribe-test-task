package com.volodymyr.test.spribetesttask.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.volodymyr.test.spribetesttask.entity.RateEntity;
import com.volodymyr.test.spribetesttask.entity.SymbolEntity;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RatesRepositoryTest {

  @Autowired
  private SymbolsRepository symbolsRepository;

  @Autowired
  private RatesRepository ratesRepository;

  @Test
  @Transactional
  void rateEntityIsRetrieved() {
    final SymbolEntity symbolEntity1 = SymbolEntity.builder()
        .id(UUID.randomUUID())
        .symbol("USD")
        .description("United States Dollar")
        .build();
    final SymbolEntity symbolEntity2 = SymbolEntity.builder()
        .id(UUID.randomUUID())
        .symbol("UAH")
        .description("Ukrainian Hryvnia")
        .build();
    symbolsRepository.saveAll(List.of(symbolEntity1, symbolEntity2));

    final RateEntity rateEntity = RateEntity.builder()
        .id(UUID.randomUUID())
        .base(symbolEntity1)
        .target(symbolEntity2)
        .rate(new BigDecimal("27.5"))
        .build();
    ratesRepository.save(rateEntity);

    final Optional<RateEntity> retrievedRateEntity = ratesRepository.findById(rateEntity.getId());

    assertThat(retrievedRateEntity).isNotEmpty();

  }

}
