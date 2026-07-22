package com.algaworks.algashop.ordering.core.ports.in.customer;

import java.util.UUID;

public interface ForManagingCustomer {
  UUID create(CustomerInput input);
  void update(UUID rawCustomerId, CustomerUpdateInput input);
  void archive(UUID rawCustomerId);
  void changeEmail(UUID rawCustomerId, String newEmail);
}
