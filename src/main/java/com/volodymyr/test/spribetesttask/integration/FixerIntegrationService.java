package com.volodymyr.test.spribetesttask.integration;

import com.volodymyr.test.spribetesttask.integration.model.IntegrationResponse;
import com.volodymyr.test.spribetesttask.integration.model.RatesIntegration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class FixerIntegrationService {

  private final RestTemplate restTemplate;

  @Value("${integration.currency.api.url}")
  public String apiUrl;

  @Value("${integration.currency.api.key}")
  public String apiKey;

  public Optional<RatesIntegration> getRates(String base) {
    return get(
        apiUrl + "/api/latest?access_key=" + apiKey + "&base=" + base,
        RatesIntegration.class);
  }

  private <T extends IntegrationResponse> Optional<T> get(String url, Class<T> responseType) {
    try {
      ResponseEntity<T> responseEntity = restTemplate.getForEntity(url, responseType);
      if (responseEntity.getStatusCode().is2xxSuccessful()
          && responseEntity.getBody() != null
          && responseEntity.getBody().isSuccess()) {
        return Optional.ofNullable(responseEntity.getBody());
      } else {
        log.error("Failed to get data from url: {}. Status code is: {}. Response body is: {}", url,
            responseEntity.getStatusCode(), responseEntity.getBody());
        return Optional.empty();
      }
    } catch (Exception e) {
      log.error("Failed to get data from url: {}", url, e);
      return Optional.empty();
    }
  }
}
