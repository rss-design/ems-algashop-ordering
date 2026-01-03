package com.algaworks.algashop.ordering.domain.model.order;

import com.algaworks.algashop.ordering.domain.model.commons.Address;
import com.algaworks.algashop.ordering.domain.model.commons.Money;
import java.time.LocalDate;
import java.util.Objects;
import lombok.Builder;

@Builder(toBuilder = true)
public record Shipping(Money cost, LocalDate expectedDate, Recipient recipient, Address address) {

    public Shipping {
        Objects.requireNonNull(cost);
        Objects.requireNonNull(expectedDate);
        Objects.requireNonNull(recipient);
        Objects.requireNonNull(address);
    }

}
