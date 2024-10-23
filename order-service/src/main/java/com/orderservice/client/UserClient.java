package com.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("user-app")
public interface UserClient {

    @GetMapping("/api/user")
    Long getUserId(@RequestParam String userEmail);
}
