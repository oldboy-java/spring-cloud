package com.tk.controller;

import com.google.common.collect.Lists;
import com.tk.entity.Dept;
import com.tk.service.DeptFeignClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;

@RestController
@Slf4j
public class DeptController_Consumer {

	@Autowired
	private DeptFeignClientService deptFeignClientService;

	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	private ThreadPoolExecutor executor;
	
	@Autowired
	private RestTemplate restTemplate;

	// 定义并发流量10个
	private Semaphore semaphore = new Semaphore(10);

	@RequestMapping(value = "/consumer/dept/add")
	public boolean add(Dept dept) {
		return deptFeignClientService.add(dept);
	}

	@RequestMapping(value = "/consumer/dept/get/{id}")
	public Dept get(@PathVariable("id") Long id) {
//		return deptFeignClientService.get(id);
		
		// 使用RestTemplate请求，会执行拦截器UserContextInterceptor，给请求增加请求头

		ResponseEntity<Dept> responseEntity = restTemplate.exchange("http://mscloud-provider-dept-hystrix-8001/dept/get/{id}", HttpMethod.GET, null, Dept.class, id);
		return responseEntity.getBody();
	}

	
	/**
	 * 在方法内部使用多线程并发访问30次，并将结果返回
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/consumer/dept/concurrency/get/{id}")
//	@HystrixCommand(commandKey="concurrency")
	public List<Dept> getConcurrency(@PathVariable("id") Long id) throws Exception {
		return getDeptConcurrency(id);
	}

	private List<Dept> getDeptConcurrency(Long id) throws Exception {
		List<Dept> list = Lists.newCopyOnWriteArrayList();
		for (int i = 0; i < 30; i++) {
			Future<Dept> f = executor.submit(() -> {
				// 开始处理前，获取凭证
				try {
					semaphore.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				Dept dept = deptFeignClientService.get(id);

				// 处理完，释放凭证
				semaphore.release();
				return dept;
			});
			list.add(f.get());
		}
		return list;

	}

	@RequestMapping(value = "/consumer/dept/list")
	public List<Dept> list() {
		return deptFeignClientService.list();
	}

	@GetMapping("/discovery")
	public String discovery() {
		List<String> services = discoveryClient.getServices();
//		log.info("所有服务列表：{}", JSON.toJSON(services));

		for (String service : services) {
			List<ServiceInstance> instances = discoveryClient.getInstances(service);
//			log.info("{}的服务实例列表：{}", service, JSON.toJSON(instances));
		}
		return "SUCCESS";
	}

}
