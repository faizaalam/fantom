package com.blockchain.fantom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class FantomApplication {


    public static void main(String[] args) {
        SpringApplication.run(FantomApplication.class, args);
    }

}