package com.tk.controller;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.ribbon.proxy.annotation.Http.Header;
import com.tk.entity.Dept;
import com.tk.service.DeptService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@DefaultProperties(commandProperties ={@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value ="4000")}, defaultFallback="globalFallback")
public class DeptController {
	
	@Autowired
	private DeptService service;
	
	
	
	/**
	 * 定义全局降级方法
	 * @return
	 */
	public String globalFallback() {
		return "执行了全局的降级方法";
	}
	
	

	/**
	 * 这个方法仅仅用了模拟一个高频访问接口，业务处理慢。在高并发场景下此接口会影响到其他的接口的正常访问
	 * 验证时取消@HystrixCommand注解，保证共用Tomcat线程池中的线程
	 * @param id
	 * @return
	 * // 一旦调用服务方法失败并抛出了错误信息后，会自动调用@HystrixCommand标注好的fallbackMethod调用类中的指定方法
	 */
	@RequestMapping(value = "/dept/get/{id}", method = RequestMethod.GET)
	@HystrixCommand(fallbackMethod = "processHystrix_Get", commandKey="dept",
			
				commandProperties= {
						
						@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value ="113000"), //此处的配置优先于配置文件中配置的超时时间，这样可以针对某些方法进行定制化超时配置
						
						@HystrixProperty(name="circuitBreaker.enabled", value = "true"),  //启用熔断
						@HystrixProperty(name="metrics.rollingStats.timeInMilliseconds", value = "10000"),  // 统计调用次数和错误比的时间窗口
						@HystrixProperty(name="circuitBreaker.requestVolumeThreshold", value = "20"),   // 在指定时间窗口内的请求次数阀值
						@HystrixProperty(name="circuitBreaker.errorThresholdPercentage", value = "50"),  // 统计时间窗口内错误比阀值 默认50%
						@HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds", value = "5000"),  // 在熔断状态下放行一些流量访问已熔断服务的时间窗口
					   }
			) 
	public Dept get(@PathVariable("id") Long id, HttpServletRequest  req)  {
		 String  correlationId = req.getHeader("tmx-correlation-id");
		log.info("tmx-correlation-id is {}", correlationId);
		log.info("get 线程池：{}", Thread.currentThread().getName());
		process();
		Dept dept = service.get(id);
		if (dept == null) {
			throw new RuntimeException("部门ID=【" + id + "】没有对应信息");
		}
		return dept;
	}

	/**
	 * 模拟处理超时了
	 */
	private void process() {
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public Dept processHystrix_Get(@PathVariable("id") Long id, HttpServletRequest  req) {
		return new Dept().setDeptno(id).setDname("该ID：" + id + "没有没有对应的信息,null--@HystrixCommand")
				.setDb_source("no this database in MySQL");
	}

	
	/**
	 *此接口仅用来模拟此接口因为get高并发访问，导致此接口访问变慢
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/dept/detail/{id}", method = RequestMethod.GET)
	@HystrixCommand   // 不指定降级方法及其他配置属性，则会继承默认配置
	public Dept detail(@PathVariable("id") Long id) {
		log.info("detail 线程池：{}", Thread.currentThread().getName());
		Dept dept = new Dept().setDb_source("1").setDeptno(1002L).setDname("DEPT-1");
		return dept;
	}
}
