package com.volodymyr.test.spribetesttask.repository;

import com.volodymyr.test.spribetesttask.entity.CurrencyEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrenciesRepository extends JpaRepository<CurrencyEntity, UUID> {

  Optional<CurrencyEntity> findBySymbol(String symbol);

}
