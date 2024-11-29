package com.volodymyr.test.spribetesttask.repository;

import com.volodymyr.test.spribetesttask.entity.SymbolEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SymbolsRepository extends JpaRepository<SymbolEntity, UUID> {

  SymbolEntity findBySymbol(String symbol);

}
