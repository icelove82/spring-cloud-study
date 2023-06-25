package com.ymh.ms.clients.notification;

import lombok.Builder;

@Builder
public record NotificationRequest(
        Integer toCustomerId,
        String toCustomerName,
        String message) {
}
