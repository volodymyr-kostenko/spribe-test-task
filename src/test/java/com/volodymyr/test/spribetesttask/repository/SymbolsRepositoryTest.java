package com.volodymyr.test.spribetesttask.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.volodymyr.test.spribetesttask.entity.SymbolEntity;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SymbolsRepositoryTest {

  @Autowired
  private SymbolsRepository symbolsRepository;

  @Test
  @Transactional
  void symbolEntityIsRetrieved() {
    final SymbolEntity symbolEntity = SymbolEntity.builder()
        .id(UUID.randomUUID())
        .symbol("USD")
        .description("United States Dollar")
        .build();
    symbolsRepository.save(symbolEntity);

    final Optional<SymbolEntity> retrievedSymbolEntity = symbolsRepository.findById(
        symbolEntity.getId());

    assertThat(retrievedSymbolEntity).isNotEmpty().get().satisfies(entity -> {
      assertThat(entity.getSymbol()).isEqualTo(symbolEntity.getSymbol());
      assertThat(entity.getDescription()).isEqualTo(symbolEntity.getDescription());
    });
  }

}
