package com.algaworks.algashop.ordering.domain.model.order;

import com.algaworks.algashop.ordering.domain.model.customer.CustomerId;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderCancelTest {

    @Test
    void givenEmptyOrder_whenCancel_shouldAllow() {
        Order order = Order.draft(new CustomerId());

        order.cancel();

        Assertions.assertWith(order,
            o -> Assertions.assertThat(o.status()).isEqualTo(OrderStatus.CANCELED),
            o -> Assertions.assertThat(o.isCanceled()).isTrue(),
            o -> Assertions.assertThat(o.canceledAt()).isNotNull()
            );
    }

    @Test
    void givenFilledOrder_whenCancel_shouldAllow() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.DRAFT).build();

        order.cancel();

        Assertions.assertWith(order,
            o -> Assertions.assertThat(o.status()).isEqualTo(OrderStatus.CANCELED),
            o -> Assertions.assertThat(o.isCanceled()).isTrue(),
            o -> Assertions.assertThat(o.canceledAt()).isNotNull()
        );
    }

    @Test
    void givenCanceledOrder_whenCancelAgain_shouldThrowException() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.CANCELED).build();

        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
            .isThrownBy(order::cancel);

        Assertions.assertWith(order,
            o -> Assertions.assertThat(o.status()).isEqualTo(OrderStatus.CANCELED),
            o -> Assertions.assertThat(o.isCanceled()).isTrue(),
            o -> Assertions.assertThat(o.canceledAt()).isNotNull()
        );
    }

}