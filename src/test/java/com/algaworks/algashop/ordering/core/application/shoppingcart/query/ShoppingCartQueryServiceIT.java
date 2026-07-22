package com.algaworks.algashop.ordering.core.application.shoppingcart.query;

import com.algaworks.algashop.ordering.core.application.AbstractApplicationIT;
import com.algaworks.algashop.ordering.core.domain.model.customer.Customer;
import com.algaworks.algashop.ordering.core.domain.model.customer.CustomerTestDataBuilder;
import com.algaworks.algashop.ordering.core.domain.model.customer.Customers;
import com.algaworks.algashop.ordering.core.domain.model.shoppingcart.ShoppingCart;
import com.algaworks.algashop.ordering.core.domain.model.shoppingcart.ShoppingCartNotFoundException;
import com.algaworks.algashop.ordering.core.domain.model.shoppingcart.ShoppingCarts;
import com.algaworks.algashop.ordering.core.ports.in.shoppingcart.ForQueryingShoppingCarts;
import com.algaworks.algashop.ordering.core.ports.in.shoppingcart.ShoppingCartOutput;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ShoppingCartQueryServiceIT extends AbstractApplicationIT {

    @Autowired
    private ForQueryingShoppingCarts queryService;

    @Autowired
    private ShoppingCarts shoppingCarts;

    @Autowired
    private Customers customers;

    @Test
    void shouldFindById() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        customers.add(customer);

        ShoppingCart shoppingCart = ShoppingCart.startShopping(customer.id());
        shoppingCarts.add(shoppingCart);

        ShoppingCartOutput output = queryService.findById(shoppingCart.id().value());

        Assertions.assertWith(output,
            o -> assertThat(o.getId()).isEqualTo(shoppingCart.id().value()),
            o -> assertThat(o.getCustomerId()).isEqualTo(shoppingCart.customerId().value())
            );
    }

    @Test
    void shouldFindByCustomerId() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        customers.add(customer);

        ShoppingCart shoppingCart = ShoppingCart.startShopping(customer.id());
        shoppingCarts.add(shoppingCart);

        ShoppingCartOutput output = queryService.findByCustomerId(shoppingCart.customerId().value());

        Assertions.assertWith(output,
            o -> assertThat(o.getId()).isEqualTo(shoppingCart.id().value()),
            o -> assertThat(o.getCustomerId()).isEqualTo(shoppingCart.customerId().value())
        );
    }

    @Test
    void shouldThrowShoppingCartNotFoundExceptionWhenFindByIdWithNonExistingShoppingCart() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        customers.add(customer);

        ShoppingCart shoppingCart = ShoppingCart.startShopping(customer.id());
        shoppingCarts.add(shoppingCart);

        Assertions.assertThatExceptionOfType(ShoppingCartNotFoundException.class)
            .isThrownBy(() -> queryService.findById(UUID.randomUUID()));
    }

    @Test
    void shouldThrowShoppingCartNotFoundExceptionWhenFindByCustomerIdWithNonExistingShoppingCart() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        customers.add(customer);

        ShoppingCart shoppingCart = ShoppingCart.startShopping(customer.id());
        shoppingCarts.add(shoppingCart);

        Assertions.assertThatExceptionOfType(ShoppingCartNotFoundException.class)
            .isThrownBy(() -> queryService.findByCustomerId(UUID.randomUUID()));
    }

}