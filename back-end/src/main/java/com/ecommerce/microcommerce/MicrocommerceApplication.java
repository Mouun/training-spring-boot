package com.ecommerce.microcommerce;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableCircuitBreaker
@EnableHystrixDashboard
public class MicrocommerceApplication {

  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(MicrocommerceApplication.class);
    app.setDefaultProperties(Collections.singletonMap("server.port", "8084"));
    app.run(args);
  }
}
