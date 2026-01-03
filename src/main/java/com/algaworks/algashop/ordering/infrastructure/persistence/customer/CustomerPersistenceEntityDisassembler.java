package com.algaworks.algashop.ordering.infrastructure.persistence.customer;

import com.algaworks.algashop.ordering.domain.model.customer.Customer;
import com.algaworks.algashop.ordering.domain.model.commons.Address;
import com.algaworks.algashop.ordering.domain.model.customer.BirthDate;
import com.algaworks.algashop.ordering.domain.model.commons.Document;
import com.algaworks.algashop.ordering.domain.model.commons.Email;
import com.algaworks.algashop.ordering.domain.model.commons.FullName;
import com.algaworks.algashop.ordering.domain.model.customer.LoyaltyPoints;
import com.algaworks.algashop.ordering.domain.model.commons.Phone;
import com.algaworks.algashop.ordering.domain.model.commons.ZipCode;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerId;
import com.algaworks.algashop.ordering.infrastructure.persistence.commons.AddressEmbeddable;

import org.springframework.stereotype.Component;

@Component
public class CustomerPersistenceEntityDisassembler {
    public Customer toDomainEntity(CustomerPersistenceEntity persistenceEntity) {
        return Customer.existing()
            .id(new CustomerId(persistenceEntity.getId()))
            .fullName(new FullName(persistenceEntity.getFirstName(), persistenceEntity.getLastName()))
            .birthDate(persistenceEntity.getBirthDate() != null ? new BirthDate(persistenceEntity.getBirthDate()) : null)
            .email(new Email(persistenceEntity.getEmail()))
            .phone(new Phone(persistenceEntity.getPhone()))
            .document(new Document(persistenceEntity.getDocument()))
            .promotionNotificationsAllowed(persistenceEntity.getPromotionNotificationsAllowed())
            .archived(persistenceEntity.getArchived())
            .registeredAt(persistenceEntity.getRegisteredAt())
            .archivedAt(persistenceEntity.getArchivedAt())
            .loyaltyPoints(new LoyaltyPoints(persistenceEntity.getLoyaltyPoints()))
            .address(this.address(persistenceEntity.getAddress()))
            .version(persistenceEntity.getVersion())
            .build();
    }

    private Address address(AddressEmbeddable addressEmbeddable) {
        if (addressEmbeddable == null) {
            return null;
        }

        return Address.builder()
            .street(addressEmbeddable.getStreet())
            .complement(addressEmbeddable.getComplement())
            .neighborhood(addressEmbeddable.getNeighborhood())
            .number(addressEmbeddable.getNumber())
            .city(addressEmbeddable.getCity())
            .state(addressEmbeddable.getState())
            .zipCode(new ZipCode(addressEmbeddable.getZipCode()))
            .build();
    }

}
