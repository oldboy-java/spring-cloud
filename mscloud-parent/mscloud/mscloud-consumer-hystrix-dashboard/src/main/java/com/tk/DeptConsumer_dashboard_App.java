package com.tk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@SpringBootApplication
@EnableHystrix
public class DeptConsumer_dashboard_App
{
	public static void main(String[] args)
	{
		SpringApplication.run(DeptConsumer_dashboard_App.class, args);
	}
}
