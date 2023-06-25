package com.ymh.ms.apigw.security;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("fake")
public class FakeApiAuthorizationChecker implements ApiKeyAuthorizationChecker {

    private Map<String, List<String>> keys = Map.of(
            "super", List.of("customer", "fraud", "notification"),
            "ymh", List.of("customer", "fraud"),
            "kyj", List.of("customer")
    );

    @Override
    public boolean isAuthorized(String key, String application) {
        return keys.getOrDefault(key, List.of())
                .stream()
                .anyMatch(it -> it.contains(application));
    }
}
