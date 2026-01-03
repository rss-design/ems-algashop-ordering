package com.algaworks.algashop.ordering.infrastructure.notification.customer;

import com.algaworks.algashop.ordering.application.customer.notification.CustomerNotificationApplicationService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomerNotificationApplicationServiceFakeImpl
    implements CustomerNotificationApplicationService {

    @Override
    public void notifyNewRegistration(NotifyNewRegistrationInput input) {
        log.info("Welcome {}", input.fistName());
        log.info("User your email to access you account {}", input.email());
    }

}
