package com.volodymyr.test.spribetesttask.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
class CurrencyControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void supportedCurrenciesAreReturned() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/currencies"))
        .andExpect(status().isOk())
        .andExpect(content().json("{\"symbols\": {\"USD\": \"United States Dollar\"}}"));
  }

  @Test
  void exchangeRatesForCurrencyAreReturned() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/currencies/USD/rates"))
        .andExpect(status().isOk())
        .andExpect(content().json(
            "{\"base\": \"USD\", \"timestamp\": 1617225600, \"rates\": {\"UAH\": 27.5}}"));
  }

  @Test
  void currencyIsAdded() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post("/currencies")
        .contentType("application/json")
        .content("{\"currency\": \"USD\", \"description\": \"United States Dollar\"}"))
        .andExpect(status().isOk());
  }

}
