package com.ymh.ms.customer;

public record CustomerRegistrationRequest(
        String firstName,
        String lastName,
        String email) {
}
