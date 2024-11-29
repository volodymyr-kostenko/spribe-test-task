package com.volodymyr.test.spribetesttask.repository;

import com.volodymyr.test.spribetesttask.entity.RateEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatesRepository extends JpaRepository<RateEntity, UUID> {

}
