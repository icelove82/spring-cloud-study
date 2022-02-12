package com.ymh.ms.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;

@EnableFeignClients("com.ymh.ms.clients")
@EnableEurekaClient
@SpringBootApplication(
        scanBasePackages = {
                "com.ymh.ms.amqp",
                "com.ymh.ms.customer"
        }
)
@PropertySource("classpath:clients-${spring.profiles.active}.properties")
public class CustomerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class, args);
    }
}
