package com.ymh.ms.customer;

import com.ymh.ms.clients.fraud.FraudCheckResponse;
import com.ymh.ms.clients.fraud.FraudClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final FraudClient fraudClient;

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

        // send notification
    }
}
