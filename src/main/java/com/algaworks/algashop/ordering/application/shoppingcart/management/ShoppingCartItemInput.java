package com.algaworks.algashop.ordering.application.shoppingcart.management;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartItemInput {
    private UUID shoppingCartId;
    private UUID productId;
    private Integer quantity;
}
