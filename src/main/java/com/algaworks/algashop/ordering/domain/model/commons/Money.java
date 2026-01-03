package com.algaworks.algashop.ordering.domain.model.commons;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Money(BigDecimal value) implements Comparable<Money> {

    public static final Money ZERO = new Money(BigDecimal.ZERO);

    public Money(String value) {
        this(new BigDecimal(value));
    }

    public Money(BigDecimal value) {
        Objects.requireNonNull(value);
        this.value = value.setScale(2, RoundingMode.HALF_EVEN);

        if (this.value.signum() == -1) {
            throw new IllegalArgumentException();
        }
    }

    public Money multiply(Quantity quantity) {
        Objects.requireNonNull(quantity);

        if (quantity.value() < 1) {
            throw new IllegalArgumentException();
        }

        BigDecimal multiplied = this.value.multiply(new BigDecimal(quantity.value()));
        return new Money(multiplied);
    }

    public Money add(Money money) {
        Objects.requireNonNull(money);
        return new Money(this.value.add(money.value));
    }

    public Money divide(Money money) {
        Objects.requireNonNull(money);
        return new Money(this.value.divide(money.value, RoundingMode.HALF_EVEN));
    }

    @Override
    public int compareTo(Money money) {
        return this.value.compareTo(money.value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
