package com.algaworks.algashop.ordering.infrastructure.product.client.http;

import com.algaworks.algashop.ordering.domain.model.commons.Money;
import com.algaworks.algashop.ordering.domain.model.product.Product;
import com.algaworks.algashop.ordering.domain.model.product.ProductCatalogService;
import com.algaworks.algashop.ordering.domain.model.product.ProductId;
import com.algaworks.algashop.ordering.domain.model.product.ProductName;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductCatalogServierHttpImpl implements ProductCatalogService {

  private final ProductCatalogApiClient productCatalogApiClient;

  @Override
  public Optional<Product> ofId(ProductId productId) {
    ProductResponse productResponse = productCatalogApiClient.getById(productId.value());
    return Optional.of(
      Product.builder()
        .id(new ProductId(productResponse.getId()))
        .name(new ProductName(productResponse.getName()))
        .price(new Money(productResponse.getSalePrice()))
        .inStock(productResponse.getInStock())
        .build()
    );
  }

}
