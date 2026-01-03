package com.algaworks.algashop.ordering.domain.model.order;

import com.algaworks.algashop.ordering.domain.model.customer.Customers;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.commons.Money;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerId;
import com.algaworks.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.order.OrderPersistenceEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.order.OrderPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.customer.CustomersPersistenceProvider;
import com.algaworks.algashop.ordering.infrastructure.persistence.order.OrdersPersistenceProvider;
import java.time.Year;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@DataJpaTest
@Import({OrdersPersistenceProvider.class,
         OrderPersistenceEntityAssembler.class,
         OrderPersistenceEntityDisassembler.class,
         CustomersPersistenceProvider.class,
         CustomerPersistenceEntityAssembler.class,
         CustomerPersistenceEntityDisassembler.class
})
class OrdersIT {

    private Orders orders;
    private Customers customers;

    @Autowired
    public OrdersIT(Orders orders, Customers customers) {
        this.orders = orders;
        this.customers = customers;
    }

    @BeforeEach
    void setUp() {
        if (!customers.exists(CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID)) {
            customers.add(CustomerTestDataBuilder.existingCustomer().build());
        }
    }

    @Test
    void shouldPersistAndFind() {
        Order originalOrder = OrderTestDataBuilder.anOrder().build();
        OrderId orderId = originalOrder.id();
        orders.add(originalOrder);

        Optional<Order> possibleOrder = orders.ofId(orderId);
        assertThat(possibleOrder).isPresent();

        Order savedOrder = possibleOrder.get();
        assertThat(savedOrder).satisfies(
          s -> assertThat(s.id()).isEqualTo(orderId),
          s -> assertThat(s.customerId()).isEqualTo(originalOrder.customerId()),
          s -> assertThat(s.totalAmount()).isEqualTo(originalOrder.totalAmount()),
          s -> assertThat(s.totalItems()).isEqualTo(originalOrder.totalItems()),
          s -> assertThat(s.placedAt()).isEqualTo(originalOrder.placedAt()),
          s -> assertThat(s.paidAt()).isEqualTo(originalOrder.paidAt()),
          s -> assertThat(s.canceledAt()).isEqualTo(originalOrder.canceledAt()),
          s -> assertThat(s.readyAt()).isEqualTo(originalOrder.readyAt()),
          s -> assertThat(s.status()).isEqualTo(originalOrder.status()),
          s -> assertThat(s.paymentMethod()).isEqualTo(originalOrder.paymentMethod())
        );
    }

    @Test
    void shouldUpdateExistingOrder() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        orders.add(order);

        orders.ofId(order.id()).orElseThrow();
        order.markAsPaid();

        orders.add(order);
        orders.ofId(order.id()).orElseThrow();

        Assertions.assertThat(order.isPaid()).isTrue();
    }

    @Test
    void shouldNotAllowStaleUpdates() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        orders.add(order);

        Order orderT1 = orders.ofId(order.id()).orElseThrow();
        Order orderT2 = orders.ofId(order.id()).orElseThrow();

        orderT1.markAsPaid();
        orders.add(orderT1);

        orderT2.cancel();

        Assertions.assertThatExceptionOfType(ObjectOptimisticLockingFailureException.class)
            .isThrownBy(()-> orders.add(orderT2));

        Order savedOrder = orders.ofId(order.id()).orElseThrow();

        Assertions.assertThat(savedOrder.canceledAt()).isNull();
        Assertions.assertThat(savedOrder.paidAt()).isNotNull();
    }

    @Test
    void shouldCountExistingOrders() {
        Assertions.assertThat(orders.count()).isZero();

        Order order1 = OrderTestDataBuilder.anOrder().build();
        Order order2 = OrderTestDataBuilder.anOrder().build();

        orders.add(order1);
        orders.add(order2);

        Assertions.assertThat(orders.count()).isEqualTo(2L);
    }

    @Test
    void shouldReturnIfOrdersExists() {
        Order order = OrderTestDataBuilder.anOrder().build();
        orders.add(order);

        Assertions.assertThat(orders.exists(order.id())).isTrue();
        Assertions.assertThat(orders.exists(new OrderId())).isFalse();
    }

    @Test
    void shouldListExistsOrdersByYear() {
        orders.add(OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build());
        orders.add(OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build());
        orders.add(OrderTestDataBuilder.anOrder().status(OrderStatus.CANCELED).build());
        orders.add(OrderTestDataBuilder.anOrder().status(OrderStatus.DRAFT).build());

        System.out.println(orders);

        CustomerId customerId = CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID;

        List<Order> ordersList = this.orders.placedByCustomerInYear(customerId, Year.now());
        Assertions.assertThat(ordersList).isNotEmpty();
        Assertions.assertThat(ordersList).hasSize(2);

        ordersList = orders.placedByCustomerInYear(customerId, Year.now().minusYears(1));
        Assertions.assertThat(ordersList).isEmpty();

        ordersList = orders.placedByCustomerInYear(new CustomerId(), Year.now());
        Assertions.assertThat(ordersList).isEmpty();
    }

    @Test
    void shouldReturnTotalSoldByCustomer() {
        Order order1 = OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).build();
        Order order2 = OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).build();
        orders.add(order1);
        orders.add(order2);

        orders.add(
            OrderTestDataBuilder.anOrder().status(OrderStatus.CANCELED).build()
        );
        orders.add(
            OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build()
        );

        Money expectedAmount = order1.totalAmount().add(order2.totalAmount());

        CustomerId customerId = CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID;

        Assertions.assertThat(orders.totalSoldForCustomer(customerId)).isEqualTo(expectedAmount);
        Assertions.assertThat(orders.totalSoldForCustomer(new CustomerId())).isEqualTo(Money.ZERO);
    }

    @Test
    void shouldReturnSalesQuantityByCustomer() {
        Order order1 = OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).build();
        Order order2 = OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).build();
        orders.add(order1);
        orders.add(order2);

        orders.add(
            OrderTestDataBuilder.anOrder().status(OrderStatus.CANCELED).build()
        );
        orders.add(
            OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build()
        );

        CustomerId customerId = CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID;

        Assertions.assertThat(orders.salesQuantityByCustomerInYear(customerId, Year.now())).isEqualTo(2L);
        Assertions.assertThat(orders.salesQuantityByCustomerInYear(customerId, Year.now().minusYears(1))).isZero();
    }

}