package com.algaworks.algashop.ordering.application.customer.management;

import com.algaworks.algashop.ordering.application.customer.notification.CustomerNotificationApplicationService;
import com.algaworks.algashop.ordering.application.customer.notification.CustomerNotificationApplicationService.NotifyNewRegistrationInput;
import com.algaworks.algashop.ordering.application.customer.query.CustomerOutput;
import com.algaworks.algashop.ordering.application.customer.query.CustomerQueryService;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerArchivedEvent;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerArchivedException;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerEmailIsInUseException;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerNotFoundException;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerRegisteredEvent;
import com.algaworks.algashop.ordering.infrastructure.listener.customer.CustomerEventListener;
import java.time.LocalDate;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class CustomerManagementApplicationServiceIT {

    @Autowired
    private CustomerManagementApplicationService customerManagementApplicationService;

    @MockitoSpyBean
    private CustomerEventListener customerEventListener;

    @MockitoSpyBean
    private CustomerNotificationApplicationService customerNotificationApplicationService;

    @Autowired
    private CustomerQueryService queryService;

    @Test
    public void shouldRegister() {
        CustomerInput input = CustomerInputTestDataBuilder.aCustomer().build();

        UUID customerId = customerManagementApplicationService.create(input);
        assertThat(customerId).isNotNull();

        CustomerOutput customerOutput = queryService.findById(customerId);

        assertThat(customerOutput)
                .extracting(
                    CustomerOutput::getId,
                    CustomerOutput::getFirstName,
                    CustomerOutput::getLastName,
                    CustomerOutput::getEmail,
                    CustomerOutput::getBirthDate
                    ).containsExactly(
                        customerId,
                        "John",
                        "Doe",
                        "johndoe@email.com",
                        LocalDate.of(1991, 7,5)
                    );

        assertThat(customerOutput.getRegisteredAt()).isNotNull();

        Mockito.verify(customerEventListener)
            .listen(Mockito.any(CustomerRegisteredEvent.class));

        Mockito.verify(customerEventListener,  Mockito.never())
            .listen(Mockito.any(CustomerArchivedEvent.class));

        Mockito.verify(customerNotificationApplicationService)
            .notifyNewRegistration(Mockito.any(NotifyNewRegistrationInput.class));
    }

    @Test
    public void shouldUpdate() {
        CustomerInput input = CustomerInputTestDataBuilder.aCustomer().build();
        CustomerUpdateInput updateInput = CustomerUpdateInputTestDataBuilder.aCustomerUpdate().build();

        UUID customerId = customerManagementApplicationService.create(input);
        assertThat(customerId).isNotNull();

        customerManagementApplicationService.update(customerId, updateInput);

        CustomerOutput customerOutput = queryService.findById(customerId);

        assertThat(customerOutput)
            .extracting(
                CustomerOutput::getId,
                CustomerOutput::getFirstName,
                CustomerOutput::getLastName,
                CustomerOutput::getEmail,
                CustomerOutput::getBirthDate
            ).containsExactly(
                customerId,
                "Matt",
                "Damon",
                "johndoe@email.com",
                LocalDate.of(1991, 7,5)
            );

        assertThat(customerOutput.getRegisteredAt()).isNotNull();
    }

    @Test
    void shouldArchiveCustomer() {
        CustomerInput input = CustomerInputTestDataBuilder.aCustomer().build();
        UUID customerId = customerManagementApplicationService.create(input);
        assertThat(customerId).isNotNull();

        customerManagementApplicationService.archive(customerId);

        CustomerOutput customerOutput = queryService.findById(customerId);

        assertThat(customerOutput)
            .isNotNull()
            .extracting(
                CustomerOutput::getId,
                CustomerOutput::getFirstName,
                CustomerOutput::getLastName,
                CustomerOutput::getPhone,
                CustomerOutput::getDocument,
                CustomerOutput::getBirthDate,
                CustomerOutput::getPromotionNotificationsAllowed
            )
            .containsExactly(
                customerId,
                "Anonymous",
                "Anonymous",
                "000-000-0000",
                "000-00-0000",
                null,
                false
            );

        assertThat(customerOutput.getEmail()).endsWith("@anonymous.com");
        assertThat(customerOutput.getArchived()).isTrue();
        assertThat(customerOutput.getArchivedAt()).isNotNull();

        assertThat(customerOutput.getAddress()).isNotNull();
        assertThat(customerOutput.getAddress().getNumber()).isNotNull().isEqualTo("Anonymized");
        assertThat(customerOutput.getAddress().getComplement()).isNull();
    }

    @Test
    void shouldThrowCustomerNotFoundExceptionWhenArchivingNonExistingCustomer() {
        UUID nonExistentCustomer = UUID.randomUUID();
        Assertions.assertThatExceptionOfType(CustomerNotFoundException.class)
            .isThrownBy(() -> customerManagementApplicationService.archive(nonExistentCustomer));
    }

    @Test
    void shouldThrowCustomerArchivedExceptionWhenArchivingAlreadyArchivedCustomer() {
        CustomerInput input = CustomerInputTestDataBuilder.aCustomer().build();
        UUID customerId = customerManagementApplicationService.create(input);
        assertThat(customerId).isNotNull();

        customerManagementApplicationService.archive(customerId);
        CustomerOutput customerOutput = queryService.findById(customerId);
        assertThat(customerOutput.getArchived()).isTrue();

        assertThatExceptionOfType(CustomerArchivedException.class)
            .isThrownBy(() -> customerManagementApplicationService.archive(customerId));
    }

    @Test
    void shouldChangeEmail() {
        CustomerInput input = CustomerInputTestDataBuilder.aCustomer().build();
        UUID customerId = customerManagementApplicationService.create(input);
        assertThat(customerId).isNotNull();

        String newEmail = "new@email.com";

        customerManagementApplicationService.changeEmail(customerId, newEmail);
        CustomerOutput customerOutput = queryService.findById(customerId);

        assertThat(customerOutput.getEmail()).isEqualTo(newEmail);
    }

    @Test
    void shouldThrowCustomerNotFoundExceptionWhenChangeEmailNonExistingCustomer(){
        UUID nonExistentCustomer = UUID.randomUUID();
        Assertions.assertThatExceptionOfType(CustomerNotFoundException.class)
            .isThrownBy(() -> customerManagementApplicationService.changeEmail(nonExistentCustomer,null));
    }

    @Test
    void shouldThrowCustomerArchivedExceptionWhenChangeEmailAlreadyArchivedCustomer(){
        CustomerInput input = CustomerInputTestDataBuilder.aCustomer().build();
        UUID customerId = customerManagementApplicationService.create(input);
        assertThat(customerId).isNotNull();

        customerManagementApplicationService.archive(customerId);
        CustomerOutput customerOutput = queryService.findById(customerId);
        assertThat(customerOutput.getArchived()).isTrue();

        assertThatExceptionOfType(CustomerArchivedException.class)
            .isThrownBy(() -> customerManagementApplicationService.archive(customerId));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenChangeInvalidEmail(){
        CustomerInput input = CustomerInputTestDataBuilder.aCustomer().build();
        UUID customerId = customerManagementApplicationService.create(input);
        String invalidEmail = "invalid.email";

        assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> customerManagementApplicationService.changeEmail(customerId,invalidEmail));
    }

    @Test
    void shouldThrowCustomerEmailIsInUseExceptionWhenChangeExitingEmail(){
        CustomerInput customerInput1 = CustomerInputTestDataBuilder.aCustomer().build();
        CustomerInput customerInput2 = CustomerInputTestDataBuilder.aCustomer()
            .email("rafael@email.com")
            .build();

        UUID customerId1 = customerManagementApplicationService.create(customerInput1);
        assertThat(customerId1).isNotNull();

        UUID customerId2 = customerManagementApplicationService.create(customerInput2);
        assertThat(customerId2).isNotNull();

        String existingEmail = customerInput1.getEmail();

        assertThatExceptionOfType(CustomerEmailIsInUseException.class)
            .isThrownBy(() -> customerManagementApplicationService.changeEmail(customerId2,existingEmail));
    }
}