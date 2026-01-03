package com.algaworks.algashop.ordering.infrastructure.persistence.provider;

import com.algaworks.algashop.ordering.domain.model.customer.Customer;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.commons.Email;
import com.algaworks.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.SpringDataAuditingConfig;
import com.algaworks.algashop.ordering.infrastructure.persistence.customer.CustomersPersistenceProvider;
import com.algaworks.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityRepository;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({
    CustomersPersistenceProvider.class,
    CustomerPersistenceEntityAssembler.class,
    CustomerPersistenceEntityDisassembler.class,
    SpringDataAuditingConfig.class
})
class CustomersPersistenceProviderIT {

    private CustomersPersistenceProvider persistenceProvider;
    private CustomerPersistenceEntityRepository entityRepository;

    @Autowired
    public CustomersPersistenceProviderIT(CustomersPersistenceProvider persistenceProvider,
                                          CustomerPersistenceEntityRepository entityRepository) {
        this.persistenceProvider = persistenceProvider;
        this.entityRepository = entityRepository;
    }

    @Test
    void shouldUpdateAndKeepPersistenceEntityState() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        UUID customerId = customer.id().value();

        persistenceProvider.add(customer);

        var persistenceEntity = entityRepository.findById(customerId).orElseThrow();

        assertThat(persistenceEntity).satisfies(
          c -> assertThat(c.getCreatedByUserId()).isNotNull(),
          c -> assertThat(c.getLastModifiedAt()).isNotNull(),
          c -> assertThat(c.getLastModifiedByUserId()).isNotNull()
        );

        customer = persistenceProvider.ofId(customer.id()).orElseThrow();
        String newEmail = "test@test.com";
        customer.changeEmail(new Email(newEmail));
        persistenceProvider.add(customer);

        persistenceEntity = entityRepository.findById(customerId).orElseThrow();

        assertThat(persistenceEntity).satisfies(
            c -> assertThat(c.getEmail()).isEqualTo(newEmail),
            c -> assertThat(c.getCreatedByUserId()).isNotNull(),
            c -> assertThat(c.getLastModifiedAt()).isNotNull(),
            c -> assertThat(c.getLastModifiedByUserId()).isNotNull()
        );

    }

    @Test
    void shouldAddFindAndNotFailWhenNoTransaction() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        persistenceProvider.add(customer);

        assertThatNoException().isThrownBy(
            () -> persistenceProvider.ofId(customer.id()).orElseThrow()
        );
    }

    @Test
    void shouldCorrectlyCount() {
        assertThat(persistenceProvider.count()).isZero();

        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        persistenceProvider.add(customer);

        assertThat(persistenceProvider.count()).isEqualTo(1L);
    }

    @Test
    void shouldVerifyExistsCustomer() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();

        boolean existsCustomer = persistenceProvider.exists(customer.id());
        assertThat(existsCustomer).isFalse();

        persistenceProvider.add(customer);
        existsCustomer = persistenceProvider.exists(customer.id());
        assertThat(existsCustomer).isTrue();
    }

}