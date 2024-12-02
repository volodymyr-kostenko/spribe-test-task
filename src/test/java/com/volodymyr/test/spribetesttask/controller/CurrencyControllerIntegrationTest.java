package com.volodymyr.test.spribetesttask.controller;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.volodymyr.test.spribetesttask.controller.exception.NotPossibleToFetchExchangeRatesException;
import com.volodymyr.test.spribetesttask.service.CurrencyData;
import com.volodymyr.test.spribetesttask.service.CurrencyService;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
class CurrencyControllerIntegrationTest {

  @MockitoBean
  private CurrencyService currencyService;

  @Autowired
  private MockMvc mockMvc;

  @Test
  void supportedCurrenciesAreReturned() throws Exception {
    when(currencyService.getAllCurrencies()).thenReturn(Map.of("USD", "United States Dollar"));

    //TODO change to xpath fields navigation
    mockMvc.perform(MockMvcRequestBuilders.get("/currencies"))
        .andExpect(status().isOk())
        .andExpect(content().json("{\"symbols\": {\"USD\": \"United States Dollar\"}}"));
  }

  @Test
  void exchangeRatesForCurrencyAreReturned() throws Exception {
    final Instant instant = Instant.ofEpochSecond(1617225600);
    when(currencyService.getExchangeRates("USD"))
        .thenReturn(Optional.of(new CurrencyData("United States Dollar", Date.from(instant),
            Map.of("UAH", new BigDecimal("27.5")))));

    mockMvc.perform(MockMvcRequestBuilders.get("/currencies/USD/rates"))
        .andExpect(status().isOk())
        .andExpect(content().json(
            "{\"base\":\"USD\",\"description\":\"United States Dollar\",\"timestamp\":1617225600000,\"rates\":{\"UAH\":27.5}}"));
  }

  @Test
  void exchangeRatesForCurrencyAreNotFound() throws Exception {
    final Instant instant = Instant.ofEpochSecond(1617225600);
    when(currencyService.getExchangeRates("USD"))
        .thenReturn(Optional.empty());

    mockMvc.perform(MockMvcRequestBuilders.get("/currencies/USD/rates"))
        .andExpect(status().isNotFound());
  }

  @Test
  void currencyIsAdded() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post("/currencies")
            .contentType("application/json")
            .content("{\"currency\": \"USD\", \"description\": \"United States Dollar\"}"))
        .andExpect(status().isOk());

    verify(currencyService).addCurrency("USD", "United States Dollar");
  }

  @Test
  void currencyIsNotAdded() throws Exception {
    doThrow(new NotPossibleToFetchExchangeRatesException("Currency symbol is invalid"))
        .when(currencyService).addCurrency("USD", "United States Dollar");

    mockMvc.perform(MockMvcRequestBuilders.post("/currencies")
            .contentType("application/json")
            .content("{\"currency\": \"USD\", \"description\": \"United States Dollar\"}"))
        .andExpect(status().isBadRequest());

    verify(currencyService).addCurrency("USD", "United States Dollar");
  }

}
