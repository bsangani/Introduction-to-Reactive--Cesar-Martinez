package org.gridu.mycapstoneproject.service;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WireMockTest( httpPort = 8082)
class ProductsInfoServiceTest {

    @Autowired
    private ProductsInfoService productsInfoService;


    @Test
    void getProductsByProductCode() {

        var productCode = "001";
        stubFor(get(urlPathEqualTo("/productInfoService/product/names"))
                .willReturn(ok().withHeader("Content-Type", "application/x-ndjson")
                        .withBody("""
                                [
                                    {"productId":"111","productCode":"001","productName":"IceCream","score":9411.96},
                                    {"productId":"222","productCode":"001","productName":"Milk","score":3784.83}
                                ]
                                """)));

        var productsFlux = this.productsInfoService.getProductsByProductCode(productCode).log();

        StepVerifier.create(productsFlux)
                .expectNextCount(2)
                .verifyComplete();
    }
}