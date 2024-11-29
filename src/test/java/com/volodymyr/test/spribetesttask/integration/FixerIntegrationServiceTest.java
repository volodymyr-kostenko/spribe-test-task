package com.volodymyr.test.spribetesttask.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.volodymyr.test.spribetesttask.integration.model.RatesIntegration;
import com.volodymyr.test.spribetesttask.integration.model.SymbolsIntegration;
import java.math.BigDecimal;
import java.util.List;
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

  @Test
  void ratesAreReturned() {
    //TODO add proper injection of wiremockUrl
    fixerIntegrationService.apiUrl = wiremockUrl;

    WireMock.stubFor(
        WireMock.get(WireMock.urlEqualTo("/api/latest?access_key=key&base=USD&symbols=UAH,EUR"))
            .willReturn(WireMock.aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(
                    "{\"success\":true,\"timestamp\":1620000000,\"date\":\"2021-05-02\",\"base\":\"USD\",\"rates\":{\"UAH\":27.5,\"EUR\":0.8}}")));

    Optional<RatesIntegration> result = fixerIntegrationService.getRates("USD",
        List.of("UAH", "EUR"));

    assertThat(result).isNotEmpty().get().satisfies(ratesIntegration -> {
      assertThat(ratesIntegration.isSuccess()).isTrue();
      assertThat(ratesIntegration.getTimestamp()).isEqualTo(1620000000);
//      final Instant instant = Instant.ofEpochSecond(ratesIntegration.getDate().getTime());
//      assertThat(instant.get(ChronoField.DAY_OF_MONTH)).isEqualTo(2);
//      assertThat(instant.get(ChronoField.MONTH_OF_YEAR)).isEqualTo(5);
//      assertThat(instant.get(ChronoField.YEAR)).isEqualTo(2021);
      assertThat(ratesIntegration.getBase()).isEqualTo("USD");
      assertThat(ratesIntegration.getRates()).containsEntry("UAH", new BigDecimal("27.5"));
      assertThat(ratesIntegration.getRates()).containsEntry("EUR", new BigDecimal("0.8"));
    });
  }
}
