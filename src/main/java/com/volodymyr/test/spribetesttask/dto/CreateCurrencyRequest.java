package com.volodymyr.test.spribetesttask.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateCurrencyRequest {

  @NotNull(message = "Currency is required")
  @Size(min = 3, max = 3, message = "Currency must be 3 characters")
  private final String currency;

  @NotNull
  @Size(max = 255, message = "Description must be less than 255 characters")
  private final String description;
}
