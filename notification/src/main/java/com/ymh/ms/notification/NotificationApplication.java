package com.ymh.ms.notification;

import com.ymh.ms.amqp.RabbitMQMessageProducer;
import com.ymh.ms.clients.notification.NotificationRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@EnableFeignClients("com.ymh.ms.clients")
@EnableEurekaClient
@SpringBootApplication(
        // For inject bean in CommandLineRunner
        // 因为 ampq 模块的作用就相当于一个 lib，并不是一个 Spring Boot App，为了能自动装配所以在此标注
        scanBasePackages = {
                "com.ymh.ms.amqp",
                "com.ymh.ms.notification"
        }
)
public class NotificationApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationApplication.class, args);
    }

    // For AMQP testing
    // Inject producer & config
    @Bean
    CommandLineRunner commandLineRunner(RabbitMQMessageProducer producer, NotificationConfig config) {
        return args -> {
            producer.publish(
                    NotificationRequest.builder().toCustomerId(1).toCustomerName("YMH").
                            message("YMH is winner").build(),
                    config.getInternalExchange(),
                    config.getInternalNotificationRoutingKey());
        };
    }
}
