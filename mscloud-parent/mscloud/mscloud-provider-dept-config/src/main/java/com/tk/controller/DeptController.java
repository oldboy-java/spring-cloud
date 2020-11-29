package com.tk.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tk.entity.Dept;
import com.tk.service.DeptService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Api(value="部门服务接口", tags= {"部门"})
public class DeptController
{
	@Autowired
	private DeptService service;

	@Value("${age}")
	private  int age;
	

	@RequestMapping(value = "/dept/add", method = RequestMethod.POST)
	@ApiOperation(value="新增部门", tags= {"部门"})
	public boolean add(@ApiParam(value="部门对象", required = true)@RequestBody Dept dept)
	{
		return service.add(dept);
	}

	@RequestMapping(value = "/dept/get/{id}", method = RequestMethod.GET)
	@ApiOperation(value="根据部门ID查找部门", tags= {"部门"})
	public Dept get(@ApiParam(value="部门ID", required = true)@PathVariable("id") Long id, @RequestHeader(name = "x-tenant-id") Long tenantId, 
			@RequestParam(name="timestamp", required = false) long timestamp)
	{
		log.info("tk.age-->>{}", age);
		log.info("---->>>>从消费端获取请求头参数：\t x-tenant-id={}, timestamp={}" , tenantId, timestamp);
		return service.get(id);
	}

	@RequestMapping(value = "/dept/list", method = RequestMethod.GET)
	@ApiOperation(value="获取部门列表", tags= {"部门"})
	public List<Dept> list()
	{
		return service.list();
	}

}
