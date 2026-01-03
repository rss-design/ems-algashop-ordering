package com.algaworks.algashop.ordering.domain.model.valueobject;

import com.algaworks.algashop.ordering.domain.model.commons.Money;
import java.math.BigDecimal;
import java.math.RoundingMode;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MoneyTest {

    public static final BigDecimal EXPECTED_RESULT = BigDecimal.TEN
        .setScale(2, RoundingMode.HALF_EVEN);

    @Test
    void shouldGenerateWithValue() {
        Money money = new Money(BigDecimal.TEN);
        assertThat(money.value()).isEqualTo(EXPECTED_RESULT);
    }

    @Test
    void shouldGenerateWithStringValue() {
        Money money = new Money("10");
        assertThat(money.value()).isEqualTo(EXPECTED_RESULT);
    }

    @Test
    void shouldNotGenerateWithNegativeValue() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> new Money(new BigDecimal(-10)));
    }

    @Test
    void shoudlAddValue() {
        Money money = new Money(BigDecimal.ZERO);
        Money addMoney = new Money(BigDecimal.TEN);
        assertThat(money.add(addMoney).value()).isEqualTo(EXPECTED_RESULT);
    }

    @Test
    void shouldDivideValue() {
        Money money = new Money(new BigDecimal(100));
        Money dividerMoney = new Money(BigDecimal.TEN);
        assertThat(money.divide(dividerMoney).value()).isEqualTo(EXPECTED_RESULT);

    }

//    @Test
//    void shoulMultiplyValue() {
//        // verificar como mockar a dependencia de Quatity
//        var quantity = Mockito.mock(Quantity.class);
//        Money money = new Money(BigDecimal.TEN);
//        assertThat(money.multiply(quantity).value()).isEqualTo(BigDecimal.ZERO);
//    }

    @Test
    void testNullPointer() {
        assertThrows(NullPointerException.class, ()-> new Money((String) null));
        // new Money("TESTE");
        //new Money((String) null);
    }

}