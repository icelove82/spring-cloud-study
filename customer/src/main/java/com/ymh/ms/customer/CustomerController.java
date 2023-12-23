package com.ymh.ms.customer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("api/v1/customers")
public class CustomerController {

    private final Resource getPath1;
    private final Resource getPath2;
    private final CustomerService customerService;

    @PostMapping
    public void registerCustomer(@RequestBody CustomerRegistrationRequest customerRegistrationRequest) {
        log.info("new customer registration {}", customerRegistrationRequest);
        customerService.registerCustomer(customerRegistrationRequest);
    }

    // for test
    @GetMapping
    public String getEcho() throws IOException {
        return "classpath:public -> " + getPath1.getFile().getAbsolutePath() + "<br>" +
                "classpath:static -> " + getPath2.getFile().getAbsolutePath();
    }
}
