package com.tk.service;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tk.entity.Dept;

//@FeignClient(value="MSCLOUD-DEPT") //指定微服务名称,注意是大写
@FeignClient(value="mscloud-provider-dept-hystrix-8001",fallbackFactory=DeptFeignClientServiceFallbackFactory.class) //指定微服务名称,支持服务降级
public interface DeptFeignClientService {

	@RequestMapping(value = "/dept/add")  //指定微服务方法
	public boolean add(Dept dept);

	@RequestMapping(value = "/dept/get/{id}")
	public Dept get(@PathVariable("id") Long id);

	@RequestMapping(value = "/dept/list")
	public List<Dept> list();
}
