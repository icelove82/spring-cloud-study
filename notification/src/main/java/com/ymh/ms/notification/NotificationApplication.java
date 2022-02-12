package com.ymh.ms.notification;

import com.ymh.ms.amqp.RabbitMQMessageProducer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

@EnableFeignClients("com.ymh.ms.clients")
@EnableEurekaClient
@SpringBootApplication(
        // For inject bean in CommandLineRunner
        scanBasePackages = {
                "com.ymh.ms.amqp",
                "com.ymh.ms.notification"
        }
)
@PropertySource("classpath:clients-${spring.profiles.active}.properties")
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
                    "YMH is winner",
                    config.getInternalExchange(),
                    config.getInternalNotificationRoutingKey());
        };
    }
}
