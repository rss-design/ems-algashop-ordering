package com.algaworks.algashop.ordering.application.shoppingcart.query;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartItemOutput {
  private UUID id;
  private UUID productId;
  private String name;
  private BigDecimal price;
  private Integer quantity;
  private BigDecimal totalAmount;
  private Boolean available;
}