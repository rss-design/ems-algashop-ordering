package com.algaworks.algashop.ordering.infrastructure.adapters.in.listener.customer;

import com.algaworks.algashop.ordering.core.ports.in.customer.ForAddingLoyaltyPoints;
import com.algaworks.algashop.ordering.core.ports.in.customer.ForConfirmCustomerRegistration;
import com.algaworks.algashop.ordering.core.ports.out.customer.ForNotifyingCustomers.NotifyNewRegistrationInput;
import com.algaworks.algashop.ordering.core.domain.model.customer.CustomerArchivedEvent;
import com.algaworks.algashop.ordering.core.domain.model.customer.CustomerRegisteredEvent;
import com.algaworks.algashop.ordering.core.domain.model.order.OrderReadyEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomerEventListener {

    private final ForConfirmCustomerRegistration forConfirmCustomerRegistration;
    private final ForAddingLoyaltyPoints forAddingLoyaltyPoints;

    @EventListener
    public void listen(CustomerRegisteredEvent event) {
        log.info("CustomerRegisteredEvent listen received: {}", event);
        forConfirmCustomerRegistration.confirm(event.customerId().value());
    }

    @EventListener
    public void listen(CustomerArchivedEvent event) {
        log.info("CustomerArchivedEvent listen received: {}", event);
    }

    @EventListener
    public void listen(OrderReadyEvent event) {
        log.info("OrderReadyEvent listen received: {}", event);

        forAddingLoyaltyPoints.addLoyaltyPoints(
            event.customerId().value(),
            event.orderId().toString()
        );
    }

}
