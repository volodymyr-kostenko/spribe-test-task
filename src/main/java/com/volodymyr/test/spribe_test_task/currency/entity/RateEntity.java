package com.volodymyr.test.spribe_test_task.currency.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rates")
public class RateEntity {

  @Id
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "base_id")
  private SymbolEntity base;

  @ManyToOne
  @JoinColumn(name = "target_id")
  private SymbolEntity target;

  private BigDecimal rate;

  private Date createdAt;
}
