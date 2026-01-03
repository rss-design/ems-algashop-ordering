package com.algaworks.algashop.ordering.infrastructure.persistence.order;

import com.algaworks.algashop.ordering.infrastructure.persistence.commons.AddressEmbeddable;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippingEmbeddable {
    private BigDecimal cost;
    private LocalDate expectedDate;

    @Embedded
    private AddressEmbeddable address;

    @Embedded
    private RecipientEmbeddable recipient;
}
