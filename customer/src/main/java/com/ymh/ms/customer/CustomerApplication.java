package com.ymh.ms.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients("com.ymh.ms.clients")
@EnableEurekaClient
@SpringBootApplication(
        // 因为 ampq 模块的作用就相当于一个 lib，并不是一个 Spring Boot App，为了能自动装配所以在此标注
        scanBasePackages = {
                "com.ymh.ms.amqp",
                "com.ymh.ms.customer"
        }
)
public class CustomerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class, args);
    }
}
