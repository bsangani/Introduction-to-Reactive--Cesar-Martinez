package org.gridu.mycapstoneproject.service;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest
@WireMockTest( httpPort = 8081)
class OrdersSearchServiceTest {

    @Autowired
    private OrdersSearchService ordersSearchService;

    @Test
    void getOrdersByPhoneNumber() {

        var phoneNumber = "00112233";
        stubFor(get(urlPathEqualTo("/orderSearchService/order/phone"))
                .willReturn(ok().withHeader("Content-Type", "application/x-ndjson")
                        .withBody("""
                                {
                                    "phoneNumber": "000006",
                                    "orderNumber": "Order_0",
                                    "productCode": "3852"
                                }
                                {
                                    "phoneNumber": "000006",
                                    "orderNumber": "Order_1",
                                    "productCode": "5256"
                                }
                                """)));

        var ordersFlux = this.ordersSearchService.getOrdersByPhoneNumber(phoneNumber).log();

        StepVerifier.create(ordersFlux)
                .expectNextCount(2)
                .verifyComplete();

    }
}