package com.algaworks.algashop.ordering.infrastructure.product.client.http;

import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface ProductCatalogApiClient {
  @GetExchange(value = "/api/v1/products/{productId}", accept = "application/json")
  ProductResponse getById(@PathVariable UUID productId);
}
