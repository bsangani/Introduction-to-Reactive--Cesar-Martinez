package org.gridu.mycapstoneproject.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UsersRepository extends ReactiveMongoRepository<UsersRepository.User, String> {

    /*================================================================================================================*/
    /* REPOSITORY MODELS */

    @Document("users")
    record User(@Id String _id, String name, String phone){}

    /*================================================================================================================*/
    /* QUERY METHODS */

    Mono<User> findUserBy_id(String _id);
    Mono<User> findUserByName(String name);
    Mono<User> findUserByPhone(String phone);
}
