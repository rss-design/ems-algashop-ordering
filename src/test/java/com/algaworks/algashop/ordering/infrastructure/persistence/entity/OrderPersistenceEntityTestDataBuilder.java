package com.algaworks.algashop.ordering.infrastructure.persistence.entity;

import com.algaworks.algashop.ordering.domain.model.IdGenerator;
import com.algaworks.algashop.ordering.infrastructure.persistence.commons.AddressEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.order.BillingEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.order.RecipientEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.order.ShippingEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.order.OrderItemPersistenceEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Set;
import static com.algaworks.algashop.ordering.infrastructure.persistence.order.OrderPersistenceEntity.*;

public class OrderPersistenceEntityTestDataBuilder {

    private OrderPersistenceEntityTestDataBuilder() {
    }

    public static OrderPersistenceEntityBuilder existingOrder() {
        return builder()
            .id(IdGenerator.generateTSID().toLong())
            .customer(CustomerPersistenceEntityTestDataBuilder.aCustomer().build())
            .totalItems(3)
            .totalAmount(new BigDecimal(1250))
            .status("DRAFT")
            .paymentMethod("CREDIT_CARD")
            .placedAt(OffsetDateTime.now())
            .items(Set.of(
                existingItem().build(),
                existingItemAlt().build()
            ));
    }

    public static OrderItemPersistenceEntity.OrderItemPersistenceEntityBuilder existingItem() {
        return OrderItemPersistenceEntity.builder()
            .id(IdGenerator.generateTSID().toLong())
            .price(new BigDecimal("500"))
            .quantity(2)
            .totalAmount(new BigDecimal("1000"))
            .productName("Notebook")
            .productId(IdGenerator.generateTimeBasedUUID());
    }

    public static OrderItemPersistenceEntity.OrderItemPersistenceEntityBuilder existingItemAlt() {
        return OrderItemPersistenceEntity.builder()
            .id(IdGenerator.generateTSID().toLong())
            .price(new BigDecimal("250"))
            .quantity(1)
            .totalAmount(new BigDecimal("250"))
            .productName("Mouse pad")
            .productId(IdGenerator.generateTimeBasedUUID());
    }

    public static OrderPersistenceEntityBuilder existingCompleteOrder() {
        return builder()
            .id(IdGenerator.generateTSID().toLong())
            .customer(CustomerPersistenceEntityTestDataBuilder.aCustomer().build())
            .totalItems(2)
            .totalAmount(new BigDecimal(1000))
            .status("DRAFT")
            .paymentMethod("CREDIT_CARD")
            .placedAt(OffsetDateTime.now())
            .billing(anBilling())
            .shipping(aShipping())
            ;
    }

    private static ShippingEmbeddable aShipping() {
        return ShippingEmbeddable.builder()
            .cost(BigDecimal.TEN)
            .expectedDate(LocalDate.now().plusWeeks(1))
            .address(anAddress())
            .recipient(anRecipient())
            .build();
    }

    private static RecipientEmbeddable anRecipient() {
        return RecipientEmbeddable.builder()
            .firstName("Jerry")
            .lastName("Moe")
            .document("454-09-2025")
            .phone("321-222-1199")
            .build();
    }

    private static BillingEmbeddable anBilling() {
        return BillingEmbeddable.builder()
            .firstName("John")
            .lastName("Doe")
            .document("225-09-1992")
            .phone("123-111-9911")
            .email("jhon.doe@gmail.com")
            .address(anAddress())
            .build();
    }

    private static AddressEmbeddable anAddress() {
        return AddressEmbeddable.builder()
            .street("Bourbon Street")
            .complement("apt. 11")
            .neighborhood("North Ville")
            .number("1234")
            .city("Montfort")
            .state("South Carolina")
            .zipCode("79911")
            .build();
    }

}
