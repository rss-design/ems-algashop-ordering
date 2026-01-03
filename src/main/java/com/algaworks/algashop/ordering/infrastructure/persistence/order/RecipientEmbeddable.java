package com.algaworks.algashop.ordering.infrastructure.persistence.order;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipientEmbeddable {
    private String firstName;
    private String lastName;
    private String document;
    private String phone;
}
