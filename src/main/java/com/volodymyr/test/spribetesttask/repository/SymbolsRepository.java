package com.volodymyr.test.spribetesttask.repository;

import com.volodymyr.test.spribetesttask.entity.SymbolEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SymbolsRepository extends JpaRepository<SymbolEntity, UUID> {

  Optional<SymbolEntity> findBySymbol(String symbol);

}
