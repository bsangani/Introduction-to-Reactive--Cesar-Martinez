package org.gridu.mycapstoneproject.service;

import lombok.extern.slf4j.Slf4j;
import org.gridu.mycapstoneproject.repository.UsersRepository;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class UsersOrdersService {

    /*================================================================================================================*/
    /* AUTOWIRE */

    private final UsersRepository usersRepository;
    private final OrdersSearchService ordersSearchService;
    private final ProductsInfoService productsInfoService;

    public UsersOrdersService(
            UsersRepository usersRepository,
            OrdersSearchService ordersSearchService,
            ProductsInfoService productsInfoService
    ) {
        this.usersRepository = usersRepository;
        this.ordersSearchService = ordersSearchService;
        this.productsInfoService = productsInfoService;
    }

    /*================================================================================================================*/
    /* SERVICE MODELS */

    public record User(String _id, String name, String phone) { }
    public record Product(
            String productId,
            String productCode,
            String productName,
            Double score
    ) { }


    /*================================================================================================================*/
    /* METHODS */

    public Mono<User> getUserById(String id) {

        var userRepo = this.usersRepository
                .findById(id)
                .log();

        return userRepo
                .map(u -> new User(u._id(), u.name(), u.phone()));

    }

    public Flux<Product> getProductsByProductCode(String productCode) {

        return this.productsInfoService.getProductsByProductCode(productCode)
                .map(p -> new Product(p.productId(), p.productCode(), p.productName(), p.score()))
                .log();
    }

    public Flux<UserOrderModelResponse> getUserOrders(String userId) {

       return this.usersRepository.findById(userId)
                .flatMapMany(userInfo ->
                        this.ordersSearchService.getOrdersByPhoneNumber(userInfo.phone())
                         .map(order -> new UserOrderModelResponse(
                                 order.orderNumber(), order.productCode(), userInfo.name(), userInfo.phone())
                         ).onErrorResume(t -> Flux.empty())
                )
               .flatMap(userModel -> this.productsInfoService.getProductsByProductCode(userModel.getProductCode())
                        .sort((a, b) -> Double.compare(b.score(), a.score()))
                        .take(1)
                        .map(product -> {
                            userModel.setProductCode(product.productCode());
                            userModel.setProductId(product.productId());
                            userModel.setProductName(product.productName());
                            return userModel;
                        })
                       .onErrorResume(throwable -> {
                           log.error(throwable.getMessage(), throwable);
                           return Flux.just(userModel);
                       })
               );

    }

}
