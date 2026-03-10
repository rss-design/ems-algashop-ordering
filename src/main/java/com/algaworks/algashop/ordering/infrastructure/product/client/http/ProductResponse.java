package com.algaworks.algashop.ordering.infrastructure.product.client.http;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
  private UUID id;
  private String name;
  private BigDecimal salePrice;
  private Boolean inStock;
}
