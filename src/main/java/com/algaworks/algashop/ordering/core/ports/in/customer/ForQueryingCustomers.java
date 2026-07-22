package com.algaworks.algashop.ordering.core.ports.in.customer;

import java.util.UUID;

import org.springframework.data.domain.Page;

public interface ForQueryingCustomers {
    CustomerOutput findById(UUID customerId);
    Page filter(CustomerFilter filter);
}
