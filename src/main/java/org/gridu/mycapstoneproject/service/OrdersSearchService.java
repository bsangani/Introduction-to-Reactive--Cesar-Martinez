package org.gridu.mycapstoneproject.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.util.context.Context;

import static org.gridu.mycapstoneproject.Constants.ORDERS_SERVICE_URL;
import static org.gridu.mycapstoneproject.LogginUtil.logOnNext;

@Slf4j
@Service
public class OrdersSearchService {

    /*================================================================================================================*/
    /* AUTOWIRE */

    private final WebClient webClient;

    public OrdersSearchService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(ORDERS_SERVICE_URL)
                .build();
    }

    /*================================================================================================================*/
    /* SERVICE MODELS */
    public record Order(String phoneNumber, String orderNumber, String productCode) { }

    /*================================================================================================================*/
    /* EXTERNAL SERVICE CALLS */

    public Flux<Order> getOrdersByPhoneNumber(String phoneNumber) {

        return webClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path("/order/phone")
                                .queryParam("phoneNumber", phoneNumber)
                                .build())
                .retrieve()
                .bodyToFlux(Order.class)
                .subscribeOn(Schedulers.boundedElastic())
                .contextWrite(Context.of("requestId", "sdghsdgh"))
                .doOnEach(logOnNext(order -> {
                    log.info("Order found: {}", order);
                }));
    }
}
