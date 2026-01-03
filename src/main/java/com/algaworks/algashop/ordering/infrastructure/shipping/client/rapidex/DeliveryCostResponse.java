package com.algaworks.algashop.ordering.infrastructure.shipping.client.rapidex;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryCostResponse {
    private String deliveryCost;
    private Long estimatedDaysToDeliver;
}
