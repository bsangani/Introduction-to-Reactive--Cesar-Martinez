package org.gridu.mycapstoneproject.controllers;

import lombok.extern.slf4j.Slf4j;
import org.gridu.mycapstoneproject.service.UserOrderModelResponse;
import org.gridu.mycapstoneproject.service.UsersOrdersService;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

@Slf4j
@RestController
@RequestMapping("/capstone")
public class UserOrdersController {

    /*================================================================================================================*/
    /* AUTOWIRE */

    private final UsersOrdersService usersOrdersService;

    public UserOrdersController(UsersOrdersService usersOrdersService) {
        this.usersOrdersService = usersOrdersService;
    }

    /*================================================================================================================*/
    /* ENDPOINTS */

    @GetMapping("/user/{id}")
    public Mono<UsersOrdersService.User> getUserById(
            @RequestHeader("requestId") String requestId,
            @PathVariable("id") String id
    ) {
        log.info("getUserById: requestId={}", requestId);
        return this.usersOrdersService.getUserById(id);
    }

    @GetMapping("/product/{productCode}")
    public Flux<UsersOrdersService.Product> getUserProductsByProductCode(
            @PathVariable("productCode") String productCode
    ) {
        return this.usersOrdersService.getProductsByProductCode(productCode);
    }

    @GetMapping(value = "/user/{id}/orders", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<UserOrderModelResponse> getOrdersByUserId(
            @RequestHeader("requestId") String requestId,
            @PathVariable("id") String id
    ) {
        return this.usersOrdersService.getUserOrders(id)
                .contextWrite(Context.of("requestId", requestId));
    }
}
