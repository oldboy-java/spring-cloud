package com.tk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableConfigServer //启用配置服务器注解
//@EnableEurekaClient   //启用服务注册功能，将配置信息注册到Eureka中
@EnableDiscoveryClient
public class Config_3344_App {

	public static void main(String[] args)
	{
		SpringApplication.run(Config_3344_App.class, args);
	}
}
