package com.tk.cfgbean;


import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Lists;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommand.Setter;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import com.netflix.loadbalancer.RetryRule;
import com.netflix.loadbalancer.RoundRobinRule;
import com.tk.interceptor.UserContextInterceptor;

import feign.Feign;
import feign.Target;
import feign.hystrix.HystrixFeign;
import feign.hystrix.SetterFactory;


@Configuration
public class ConfigBean //boot -->spring   applicationContext.xml --- @Configuration配置   ConfigBean = applicationContext.xml
{ 

	
	@Bean //显示配置负载均衡算法
	public IRule ribbonRule() {
		//return new RandomRule();  //设置随机规则
		return new RoundRobinRule(); //轮训规则
//		return new RetryRule(); //重试规则
	}

	
	/**
	 * 简单创建一个线程池
	 * @return
	 */
	@Bean
	public ThreadPoolExecutor executor() {
		ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 10, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
		return executor;
	}
	
	
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(name = "feign.hystrix.enabled")
	public Feign.Builder feignHystrixBuilder() {
		return HystrixFeign.builder().setterFactory(new SetterFactory() {

			@Override
			public Setter create(Target<?> target, Method method) {
				String groupKey = target.name();
				String commandKey = Feign.configKey(target.type(), method);
				return HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupKey))
							//.andCommandKey(HystrixCommandKey.Factory.asKey(commandKey))
							.andCommandKey(HystrixCommandKey.Factory.asKey(groupKey));
//							.andCommandKey(HystrixCommandKey.Factory.asKey(target.type().getSimpleName()));
				}
		});
	}
	
	
	/**
	 * 实例化RestTemplate，同时设置拦截器，统一设置请求头参数
	 * @return
	 */
	@Bean
	@LoadBalanced  //客户端负载均衡
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		List<ClientHttpRequestInterceptor>interceptors = restTemplate.getInterceptors();
		if (CollectionUtils.isEmpty(interceptors)) {
			restTemplate.setInterceptors(Collections.singletonList(new UserContextInterceptor()));
		}else {
			interceptors.add(new UserContextInterceptor());
			restTemplate.setInterceptors(interceptors);
		}
		return restTemplate;
	}
	
}

//@Bean
//public UserServcie getUserServcie()
//{
//	return new UserServcieImpl();
//}
// applicationContext.xml == ConfigBean(@Configuration)
//<bean id="userServcie" class="com.atguigu.tmall.UserServiceImpl">


