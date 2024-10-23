package com.shoppingcartservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@EnableAspectJAutoProxy
@OpenAPIDefinition(info = @Info(
        title = "API Cart-service",
        version = "1.0",
        description = "Documentation API Cart-service v1.0"))
public class ShoppingCartServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingCartServiceApplication.class, args);
    }
}
