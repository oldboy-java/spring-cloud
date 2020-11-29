package com.tk.interceptor;

import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * Feign拦截器，可以统一对Feign的调用进行拦截
 */
@Configuration
public class FeignRequestInterceptor implements RequestInterceptor {

	public void apply(RequestTemplate template) {
		
		// 设置请求头
		template.header("x-tenant-id", "5");
		
		// 设置请求query参数值
		template.query("timestamp",System.currentTimeMillis()+"");
	
	}

}