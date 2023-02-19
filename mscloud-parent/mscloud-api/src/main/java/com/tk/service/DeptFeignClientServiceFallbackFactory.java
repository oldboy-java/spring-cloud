package com.tk.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.tk.entity.Dept;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class DeptFeignClientServiceFallbackFactory implements FallbackFactory<DeptFeignClientService> {

	public DeptFeignClientService create(Throwable cause) {
		return new DeptFeignClientService() {
			
			public List<Dept> list() {
				// TODO Auto-generated method stub
				return null;
			}
			
			public Dept get(Long id) {
				log.info("当前执行降级逻辑线程:{}", Thread.currentThread().getName());
				
				return new Dept().setDeptno(id).setDname("该ID：" + id + "没有没有对应的信息,Consumer客户端提供的降级信息,此刻服务Provider已经关闭")
						.setDb_source("no this database in MySQL");
			}
			
			public boolean add(Dept dept) {
				// TODO Auto-generated method stub
				return false;
			}
		};
	}
}