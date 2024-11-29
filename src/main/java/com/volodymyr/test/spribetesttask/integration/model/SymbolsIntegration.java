package com.volodymyr.test.spribetesttask.integration.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SymbolsIntegration {

  private boolean success;

//  @JsonProperty("symbols")
  private Map<String, String> symbols;
}
