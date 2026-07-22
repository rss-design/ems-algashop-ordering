package com.algaworks.algashop.ordering.core.application.shoppingcart;

import com.algaworks.algashop.ordering.core.ports.in.shoppingcart.ForQueryingShoppingCarts;
import com.algaworks.algashop.ordering.core.ports.in.shoppingcart.ShoppingCartOutput;
import com.algaworks.algashop.ordering.core.ports.out.shoppingcart.ForObtainingShoppingCarts;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartQueryService implements ForQueryingShoppingCarts {

  private final ForObtainingShoppingCarts forObtainingShoppingCarts;

  @Override
  public ShoppingCartOutput findById(UUID shoppingCartId) {
    return forObtainingShoppingCarts.findById(shoppingCartId);
  }

  @Override
  public ShoppingCartOutput findByCustomerId(UUID customerId) {
    return forObtainingShoppingCarts.findByCustomerId(customerId);
  }
}
