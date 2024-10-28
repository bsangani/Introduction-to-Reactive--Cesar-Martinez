package org.gridu.mycapstoneproject.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import static org.gridu.mycapstoneproject.Constants.PRODUCTS_SERVICE_URL;
import static org.gridu.mycapstoneproject.LogginUtil.logOnNext;

@Slf4j
@Service
public class ProductsInfoService {

    /*================================================================================================================*/
    /* AUTOWIRE */

    private final WebClient webClient;

    public ProductsInfoService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(PRODUCTS_SERVICE_URL)
                .build();
    }

    /*================================================================================================================*/
    /* SERVICE MODELS */
    public record Product(
            String productId,
            String productCode,
            String productName,
            Double score
    ) { }

    /*================================================================================================================*/
    /* EXTERNAL SERVICE CALLS */
    public Flux<Product> getProductsByProductCode(String productCode) {

        return webClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path("/product/names")
                                .queryParam("productCode", productCode)
                                .build())
                .retrieve()
                .bodyToFlux(Product.class)
                .subscribeOn(Schedulers.boundedElastic())
                .doOnEach(logOnNext(product -> {
                    log.info("Product found: {}", product);
                }));
    }
}
