package com.thoughtmechanix.organization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
@EnableResourceServer  //启用资源服务器
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableOAuth2Client
public class OrgApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrgApplication.class, args);
    }
}
