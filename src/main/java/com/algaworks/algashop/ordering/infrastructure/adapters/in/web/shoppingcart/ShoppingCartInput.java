package com.algaworks.algashop.ordering.infrastructure.adapters.in.web.shoppingcart;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Data;

@Data
public class ShoppingCartInput {
  @NotNull
  private UUID customerId;
}
