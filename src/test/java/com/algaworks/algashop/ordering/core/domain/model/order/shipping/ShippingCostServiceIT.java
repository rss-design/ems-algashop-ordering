package com.algaworks.algashop.ordering.core.domain.model.order.shipping;

import com.algaworks.algashop.ordering.core.domain.model.AbstractDomainIT;
import com.algaworks.algashop.ordering.core.domain.model.commons.ZipCode;
import com.algaworks.algashop.ordering.core.domain.model.order.shipping.ShippingCostService.CalculationRequest;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.cloud.contract.wiremock.WireMockSpring.options;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ShippingCostServiceIT extends AbstractDomainIT {

    @Autowired
    private ShippingCostService shippingCostService;

    @Autowired
    private OriginAddressService originAddressService;

    private WireMockServer wireMockRapidex;

    @BeforeEach
    void setUp() {
      initWireMock();
    }

    @AfterEach
    void tearDown() {
      wireMockRapidex.stop();
    }

    private void initWireMock() {
      wireMockRapidex = new WireMockServer(options()
        .port(8780)
        .usingFilesUnderDirectory("src/test/resources/wiremock/rapidex")
        .extensions(new ResponseTemplateTransformer(true)));

      wireMockRapidex.start();
    }

    @Test
    void shouldCalculate() {
        ZipCode origin = originAddressService.originAddress().zipCode();
        ZipCode destination = new ZipCode("12345");

        var calculate = shippingCostService.calculate(new CalculationRequest(origin, destination));

        assertThat(calculate.cost()).isNotNull();
        assertThat(calculate.expectedDate()).isNotNull();
    }

}