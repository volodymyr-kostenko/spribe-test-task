package com.volodymyr.test.spribetesttask.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.volodymyr.test.spribetesttask.integration.model.SymbolsIntegration;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.wiremock.spring.EnableWireMock;

@SpringBootTest
@EnableWireMock
class FixerIntegrationServiceTest {

  @Autowired
  private FixerIntegrationService fixerIntegrationService;

  @Value("${wiremock.server.baseUrl}")
  private String wiremockUrl;

  @Value("${wiremock.server.port}")
  private String wiremockPort;

  @Test
  void symbolsAreReturned() {
    //TODO add proper injection of wiremockUrl
    fixerIntegrationService.apiUrl = wiremockUrl;

    WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/api/symbols?access_key=key"))
        .willReturn(WireMock.aResponse()
            .withHeader("Content-Type", "application/json")
            .withBody(
                "{\"success\":true,\"symbols\":{\"USD\":\"United States Dollar\",\"EUR\":\"Euro\"}}")));

    Optional<SymbolsIntegration> result = fixerIntegrationService.getSymbols();

    assertThat(result).isNotEmpty().get().satisfies(symbolsIntegration -> {
      assertThat(symbolsIntegration.isSuccess()).isTrue();
      assertThat(symbolsIntegration.getSymbols()).containsEntry("USD", "United States Dollar");
      assertThat(symbolsIntegration.getSymbols()).containsEntry("EUR", "Euro");
    });
  }
}
