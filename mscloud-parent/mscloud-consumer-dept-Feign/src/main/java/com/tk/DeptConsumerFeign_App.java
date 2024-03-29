package com.tk;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages="com.tk")
@EnableDiscoveryClient
@EnableHystrix
public class DeptConsumerFeign_App
{
	public static void main(String[] args)
	{
		SpringApplication.run(DeptConsumerFeign_App.class, args);
	}
}
