package com.ymh.ms.customer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CustomerConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Value("${server.port}")
    private String test;

    @Value("classpath:public/hi.txt")
    Resource publicFile;

    @Value("classpath:static/test.txt")
    Resource staticFile;


    @Bean
    public Resource getPath1() {
        return publicFile;
    }

    @Bean
    public Resource getPath2() {
        return staticFile;
    }
}
