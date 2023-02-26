package com.thoughtmechanix.licenses.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenFeignConfig {

    @Bean
    Logger.Level feignLoggerLevel(){
        Logger.Level level = Logger.Level.FULL;
        return level;
    }
}
