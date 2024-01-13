package com.ymh.ms.customer;

import com.ymh.ms.amqp.RabbitMQMessageProducer;
import com.ymh.ms.clients.fraud.FraudCheckResponse;
import com.ymh.ms.clients.fraud.FraudClient;
import com.ymh.ms.clients.notification.NotificationClient;
import com.ymh.ms.clients.notification.NotificationRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@Service
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final FraudClient fraudClient;
    private final NotificationClient notificationClient;
    private final RabbitMQMessageProducer rabbitMQMessageProducer;

    // Registration a customer
    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();

        // 1. store customer in db
        customerRepository.saveAndFlush(customer);

        // 2. check customer if fraud
        FraudCheckResponse fraudCheckResponse = fraudClient.isFraud(customer.getId());

        assert fraudCheckResponse != null;
        if (fraudCheckResponse.isFraud()) {
            throw new IllegalStateException("The customer is fraud");
        }

        // 3. send notification via RabbitMQ
        /* Via client will be non-async way, what means it will delay the user experience
        notificationClient.sendNotification(
                new NotificationRequest(
                        customer.getId(),
                        customer.getEmail(),
                        String.format("Hi %s, welcome to Alex YUN 's world ...", customer.getFirstName())
                )
        );
        */

        /* Via AMQP will be async way service */
        rabbitMQMessageProducer.publish(
                new NotificationRequest(
                        customer.getId(),
                        customer.getEmail(),
                        String.format("Hi %s, welcome to Alex YUN 's world ...", customer.getFirstName())
                ),
                "internal.exchange",
                "internal.notification.routing-key"
        );
    }

    @Async
    public void asyncMethodWithReturnType() throws InterruptedException {
        log.info("YMH => " + "Execute method asynchronously");

        TimeUnit.SECONDS.sleep(10);

        List<Customer> byJpaQuery = customerRepository.findAll();

        List<Customer> byNativeQuery = customerRepository.findByNativeQuery("yun", "myeonghun");

        log.info("YMH => " + "Execute finish method asynchronously");
    }
}
