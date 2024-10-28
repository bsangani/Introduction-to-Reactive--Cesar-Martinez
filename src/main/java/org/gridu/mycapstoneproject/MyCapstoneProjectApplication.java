package org.gridu.mycapstoneproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories
public class MyCapstoneProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyCapstoneProjectApplication.class, args);
    }

}
