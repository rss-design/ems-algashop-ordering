package com.algaworks.algashop.ordering.infrastructure.persistence.customer;

import com.algaworks.algashop.ordering.domain.model.customer.Customer;
import com.algaworks.algashop.ordering.domain.model.commons.Address;
import com.algaworks.algashop.ordering.infrastructure.persistence.commons.AddressEmbeddable;

import org.springframework.stereotype.Component;

@Component
public class CustomerPersistenceEntityAssembler {

    public CustomerPersistenceEntity fromDomain(Customer customer) {
        return merge(new CustomerPersistenceEntity(), customer);
    }

    public CustomerPersistenceEntity merge(CustomerPersistenceEntity customerPersistence, Customer customer) {
        customerPersistence.setId(customer.id().value());
        customerPersistence.setFirstName(customer.fullName().firstName());
        customerPersistence.setLastName(customer.fullName().lastName());
        customerPersistence.setBirthDate(customer.birthDate() != null ? customer.birthDate().value() : null);
        customerPersistence.setEmail(customer.email().value());
        customerPersistence.setPhone(customer.phone().value());
        customerPersistence.setDocument(customer.document().value());
        customerPersistence.setPromotionNotificationsAllowed(customer.isPromotionNotificationsAllowed());
        customerPersistence.setArchived(customer.isArchived());
        customerPersistence.setRegisteredAt(customer.registeredAt());
        customerPersistence.setArchivedAt(customer.archivedAt());
        customerPersistence.setLoyaltyPoints(customer.loyaltyPoints().value());
        customerPersistence.setAddress(this.addressEmbeddable(customer.address()));
        customerPersistence.setVersion(customer.version());

        customerPersistence.addEvent(customer.domainEvents());
        return customerPersistence;
    }

    private AddressEmbeddable addressEmbeddable(Address address) {
        if (address == null) {
            return null;
        }

        return AddressEmbeddable.builder()
            .street(address.street())
            .complement(address.complement())
            .neighborhood(address.neighborhood())
            .number(address.number())
            .city(address.city())
            .state(address.state())
            .zipCode(address.zipCode().value())
            .build();
    }

}
