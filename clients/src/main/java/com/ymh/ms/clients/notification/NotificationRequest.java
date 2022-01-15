package com.ymh.ms.clients.notification;

public record NotificationRequest(
        Integer toCustomerId,
        String toCustomerName,
        String message) {
}
