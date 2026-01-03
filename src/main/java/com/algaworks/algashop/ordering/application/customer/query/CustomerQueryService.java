package com.algaworks.algashop.ordering.application.customer.query;

import java.util.UUID;

import org.springframework.data.domain.Page;

public interface CustomerQueryService {
    CustomerOutput findById(UUID customerId);
    Page filter(CustomerFilter filter);
}
