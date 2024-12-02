package com.volodymyr.test.spribetesttask.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.volodymyr.test.spribetesttask.integration.model.RatesIntegration;
import com.volodymyr.test.spribetesttask.integration.model.SymbolsIntegration;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.wiremock.spring.EnableWireMock;

@SpringBootTest
@EnableWireMock
class FixerIntegrationServiceIntegrationTest {

  @Autowired
  private FixerIntegrationService fixerIntegrationService;

  @Value("${wiremock.server.baseUrl}")
  private String wiremockUrl;

  @BeforeEach
  void setup() {
    ReflectionTestUtils.setField(fixerIntegrationService, "apiUrl", wiremockUrl);
    ReflectionTestUtils.setField(fixerIntegrationService, "apiKey", "key");
  }

  @Test
  void symbolsAreReturned() {
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

  @Test
  void ratesAreReturned() {
    WireMock.stubFor(
        WireMock.get(WireMock.urlEqualTo("/api/latest?access_key=key&base=USD"))
            .willReturn(WireMock.aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(
                    "{\"success\":true,\"timestamp\":1620000000,\"date\":\"2021-05-02\",\"base\":\"USD\",\"rates\":{\"UAH\":27.5,\"EUR\":0.8}}")));

    Optional<RatesIntegration> result = fixerIntegrationService.getRates("USD");

    assertThat(result).isNotEmpty().get().satisfies(ratesIntegration -> {
      assertThat(ratesIntegration.isSuccess()).isTrue();
      assertThat(ratesIntegration.getTimestamp()).isEqualTo(1620000000);
      assertThat(ratesIntegration.getBase()).isEqualTo("USD");
      assertThat(ratesIntegration.getRates()).containsEntry("UAH", new BigDecimal("27.5"));
      assertThat(ratesIntegration.getRates()).containsEntry("EUR", new BigDecimal("0.8"));
    });
  }
}
