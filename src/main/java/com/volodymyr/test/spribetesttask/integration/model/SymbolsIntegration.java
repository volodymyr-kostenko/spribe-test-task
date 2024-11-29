package com.volodymyr.test.spribetesttask.integration.model;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SymbolsIntegration implements IntegrationResponse {

  private boolean success;

  //  @JsonProperty("symbols")
  private Map<String, String> symbols;
}
