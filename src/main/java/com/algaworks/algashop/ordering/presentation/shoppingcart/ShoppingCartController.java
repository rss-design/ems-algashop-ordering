package com.algaworks.algashop.ordering.presentation.shoppingcart;

import com.algaworks.algashop.ordering.application.shoppingcart.management.ShoppingCartItemInput;
import com.algaworks.algashop.ordering.application.shoppingcart.management.ShoppingCartManagementApplicationService;
import com.algaworks.algashop.ordering.application.shoppingcart.query.ShoppingCartOutput;
import com.algaworks.algashop.ordering.application.shoppingcart.query.ShoppingCartQueryService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/shopping-carts")
@RequiredArgsConstructor
public class ShoppingCartController {

  private final ShoppingCartQueryService queryService;
  private final ShoppingCartManagementApplicationService managementService;

  @GetMapping("/{shoppingCartId}")
  public ShoppingCartOutput getById(@PathVariable UUID shoppingCartId) {
    return queryService.findById(shoppingCartId);
  }

  @GetMapping("/{shoppingCartId}/items")
  public ShoppingCartItemListModel getItems(@PathVariable UUID shoppingCartId) {
    var items = queryService.findById(shoppingCartId).getItems();
    return new ShoppingCartItemListModel(items);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ShoppingCartOutput create(@RequestBody @Valid ShoppingCartInput input) {
    UUID shoppingCartId = managementService.createNew(input.getCustomerId());
    return queryService.findById(shoppingCartId);
  }

  @PostMapping("/{shoppingCartId}/items")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void addItem(@PathVariable UUID shoppingCartId, @RequestBody @Valid ShoppingCartItemInput input) {
    input.setShoppingCartId(shoppingCartId);
    managementService.addItem(input);
  }

  @DeleteMapping("/{shoppingCartId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable UUID shoppingCartId) {
    managementService.delete(shoppingCartId);
  }

  @DeleteMapping("/{shoppingCartId}/items")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void empty(@PathVariable UUID shoppingCartId) {
    managementService.empty(shoppingCartId);
  }

  @DeleteMapping("/{shoppingCartId}/items/{itemId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void removeItem(@PathVariable UUID shoppingCartId, @PathVariable UUID itemId) {
    managementService.removeItem(shoppingCartId,itemId);
  }

}
