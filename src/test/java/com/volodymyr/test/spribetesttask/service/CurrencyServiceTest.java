package com.volodymyr.test.spribetesttask.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;

import com.volodymyr.test.spribetesttask.integration.FixerIntegrationService;
import com.volodymyr.test.spribetesttask.integration.model.RatesIntegration;
import com.volodymyr.test.spribetesttask.repository.CurrenciesRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


@SpringBootTest
class CurrencyServiceTest {

  @Autowired
  private CurrencyService currencyService;

  @Autowired
  private CurrenciesRepository currencyRepository;

  @MockitoBean
  private FixerIntegrationService fixerIntegrationService;

  @Test
  @Transactional
  void currencyIsAdded() {
    final Instant instant = Instant.ofEpochSecond(1620000000);
    when(fixerIntegrationService.getRates("USD"))
        .thenReturn(
            Optional.of(
                new RatesIntegration(true, instant.getEpochSecond(),
                    Date.from(instant), "USD",
                    Map.of("UAH", new BigDecimal("27.5"), "EUR", new BigDecimal("0.8"))
                )
            )
        );

    currencyService.addCurrency("USD", "United States Dollar");

    assertThat(currencyRepository.findBySymbol("USD")).isNotEmpty();
  }

  @Transactional
  @Test
  void exchangeRatesAreReturned() {
    final Instant instant = Instant.ofEpochSecond(1620000000);
    when(fixerIntegrationService.getRates("USD"))
        .thenReturn(
            Optional.of(
                new RatesIntegration(true, instant.getEpochSecond(),
                    Date.from(instant), "USD",
                    Map.of("UAH", new BigDecimal("0.04"), "EUR", new BigDecimal("0.8"))
                )
            )
        );
    currencyService.addCurrency("USD", "United States Dollar");

    final CurrencyData result = currencyService.getExchangeRates("USD");

    assertThat(result.getDate().getTime()).isEqualTo(instant.toEpochMilli());
    assertThat(result.getDescription()).isEqualTo("United States Dollar");
    assertThat(result.getRates()).hasSize(2)
        .containsEntry("UAH", new BigDecimal("0.04"))
        .containsEntry("EUR", new BigDecimal("0.8"));
  }

  @Transactional
  @Test
  void allCurrenciesAreReturned() {
    final Instant instant = Instant.ofEpochSecond(1620000000);
    when(fixerIntegrationService.getRates("USD"))
        .thenReturn(
            Optional.of(
                new RatesIntegration(true, instant.getEpochSecond(),
                    Date.from(instant), "USD",
                    Map.of("UAH", new BigDecimal("27.5"), "EUR", new BigDecimal("0.8"))
                )
            )
        );
    currencyService.addCurrency("USD", "United States Dollar");

    final Map<String, String> result = currencyService.getAllCurrencies();

    assertThat(result).hasSize(1).containsEntry("USD", "United States Dollar");
  }

}
