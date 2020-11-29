package com.tk.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tk.dao.DeptDao;
import com.tk.entity.Dept;
import com.tk.service.DeptService;

@Service
public class DeptServiceImpl implements DeptService
{
	@Autowired
	private DeptDao dao;
	
	public boolean add(Dept dept)
	{
		return dao.addDept(dept);
	}

	public Dept get(Long id)
	{
		return dao.findById(id);
	}

	public List<Dept> list()
	{
		return dao.findAll();
	}

}
