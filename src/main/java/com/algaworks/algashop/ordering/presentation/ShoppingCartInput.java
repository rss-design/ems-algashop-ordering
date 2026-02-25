package com.algaworks.algashop.ordering.presentation;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Data;

@Data
public class ShoppingCartInput {
  @NotNull
  private UUID customerId;
}
