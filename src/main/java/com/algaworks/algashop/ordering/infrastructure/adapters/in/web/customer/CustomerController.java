package com.algaworks.algashop.ordering.infrastructure.adapters.in.web.customer;

import com.algaworks.algashop.ordering.core.ports.in.customer.CustomerInput;
import com.algaworks.algashop.ordering.core.ports.in.customer.CustomerUpdateInput;
import com.algaworks.algashop.ordering.core.ports.in.customer.CustomerFilter;
import com.algaworks.algashop.ordering.core.ports.in.customer.CustomerOutput;
import com.algaworks.algashop.ordering.core.ports.in.customer.ForManagingCustomer;
import com.algaworks.algashop.ordering.core.ports.in.customer.ForQueryingCustomers;
import com.algaworks.algashop.ordering.core.ports.in.customer.CustomerSummaryOutput;
import com.algaworks.algashop.ordering.core.ports.in.shoppingcart.ShoppingCartOutput;
import com.algaworks.algashop.ordering.core.ports.in.shoppingcart.ForQueryingShoppingCarts;
import com.algaworks.algashop.ordering.infrastructure.adapters.in.web.PageModel;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromMethodCall;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

  private final ForManagingCustomer forManagingCustomer;
  private final ForQueryingCustomers forQueryingCustomers;
  private final ForQueryingShoppingCarts forQueryingShoppingCarts;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CustomerOutput create(@RequestBody @Valid CustomerInput input, HttpServletResponse httpServletResponse) {
    UUID customerId = forManagingCustomer.create(input);

    UriComponentsBuilder builder = fromMethodCall(on(CustomerController.class).findById(customerId));
    httpServletResponse.addHeader("Location", builder.toUriString());

    return forQueryingCustomers.findById(customerId);
  }

  @GetMapping
  public PageModel<CustomerSummaryOutput> findAll(CustomerFilter customerFilter) {
    return PageModel.of(forQueryingCustomers.filter(customerFilter));
  }

  @GetMapping("/{customerId}")
  public CustomerOutput findById(@PathVariable UUID customerId) {
    return forQueryingCustomers.findById(customerId);
  }

  @GetMapping("/{customerId}/shopping-cart")
  public ShoppingCartOutput findShoppingCartByCustomerId(@PathVariable UUID customerId) {
    return forQueryingShoppingCarts.findByCustomerId(customerId);
  }

  @PutMapping("/{customerId}")
  @ResponseStatus(HttpStatus.OK)
  public CustomerOutput update(@PathVariable UUID customerId, @RequestBody @Valid CustomerUpdateInput input) {
    forManagingCustomer.update(customerId,input);
    return forQueryingCustomers.findById(customerId);
  }

  @DeleteMapping("/{customerId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable UUID customerId) {
    forManagingCustomer.archive(customerId);
  }

}