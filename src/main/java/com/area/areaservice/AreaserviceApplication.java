package com.area.areaservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactivefeign.spring.config.EnableReactiveFeignClients;

@SpringBootApplication
@EnableReactiveFeignClients
public class AreaserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AreaserviceApplication.class, args);
    }

}
