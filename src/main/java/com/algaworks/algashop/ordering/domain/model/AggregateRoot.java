package com.algaworks.algashop.ordering.domain.model;

import java.util.List;

public interface AggregateRoot<ID> extends DomainEventSource {
    ID id();

    @Override
    List<Object> domainEvents();

    @Override
    void clearDomainEvents();
}
