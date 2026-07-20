package com.algaworks.algashop.ordering.core.domain.model.order;

import com.algaworks.algashop.ordering.core.domain.model.Specification;
import com.algaworks.algashop.ordering.core.domain.model.customer.Customer;
import java.time.Year;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomerHasOrderedEnoughAtYearSpecification implements Specification<Customer> {

    private final Orders orders;
    private final long expectedOrderCount;

    @Override
    public boolean isSatisfiedBy(Customer customer) {
        return orders.salesQuantityByCustomerInYear(
            customer.id(),
            Year.now()
        ) >= expectedOrderCount;
    }

}
