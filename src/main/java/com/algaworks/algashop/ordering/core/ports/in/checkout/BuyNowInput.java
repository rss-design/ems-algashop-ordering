package com.algaworks.algashop.ordering.core.ports.in.checkout;

import com.algaworks.algashop.ordering.core.ports.in.order.BillingData;
import com.algaworks.algashop.ordering.core.ports.in.order.ShippingInput;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BuyNowInput {
  @Valid
  @NotNull
  private ShippingInput shipping;

  @Valid
  @NotNull
  private BillingData billing;

  @NotNull
  private UUID productId;

  @NotNull
  private UUID customerId;

  @NotNull
  @Positive
  private Integer quantity;

  @NotBlank
  private String paymentMethod;

  private UUID creditCardId;
}
