package com.volodymyr.test.spribetesttask.controller;

import com.volodymyr.test.spribetesttask.controller.exception.NotPossibleToFetchExchangeRatesException;
import com.volodymyr.test.spribetesttask.dto.CreateCurrencyRequest;
import com.volodymyr.test.spribetesttask.dto.GetCurrencyExchangeRatesResponse;
import com.volodymyr.test.spribetesttask.dto.GetSupportedCurrenciesResponse;
import com.volodymyr.test.spribetesttask.service.CurrencyData;
import com.volodymyr.test.spribetesttask.service.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController("/api")
public class CurrencyController {

  private final CurrencyService currencyService;

  @Operation(summary = "Get all supported currencies")
  @GetMapping(value = "/currencies", produces = "application/json")
  public ResponseEntity<GetSupportedCurrenciesResponse> getAllCurrencies() {
    return ResponseEntity.ok(
        new GetSupportedCurrenciesResponse(currencyService.getAllCurrencies()));
  }

  @Operation(summary = "Get exchange rates for a specific currency")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Exchange rates for the currency"),
      @ApiResponse(responseCode = "404", description = "Currency not found")
  })
  @GetMapping("/currencies/{currency}/rates")
  public ResponseEntity<GetCurrencyExchangeRatesResponse> getExchangeRateForCurrency(
      @PathVariable @NotNull @Size(min = 3, max = 3, message = "Currency must be 3 characters") String currency) {
    final Optional<CurrencyData> currencyData = currencyService.getExchangeRates(currency);

    return currencyData.map(data -> ResponseEntity.ok(
                new GetCurrencyExchangeRatesResponse(
                    currency,
                    data.getDescription(),
                    data.getDate().getTime(),
                    data.getRates()
                )
            )
        )
        .orElseGet(() -> ResponseEntity.notFound().build());

  }

  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Currency added"),
      @ApiResponse(responseCode = "400", description = "Currency symbol is invalid")
  })
  @Operation(summary = "Add a new currency")
  @PostMapping("/currencies")
  public ResponseEntity<Object> addNewCurrency(
      @RequestBody @NotNull CreateCurrencyRequest newCurrency) {
    try {
      currencyService.addCurrency(newCurrency.getCurrency(), newCurrency.getDescription());
    } catch (NotPossibleToFetchExchangeRatesException e) {
      //TODO this is not a good way to handle this exception.
      // Because I cannot really check how api works with free version therefore I cannot handle api errors properly.
      // The idea is to propagate 404 error to the client.
      // But, right now I am not entirely sure how to differentiate between 404 invalid url and 404 invalid base currency
      return ResponseEntity.badRequest().build();
    }

    return ResponseEntity.ok().build();
  }
}
