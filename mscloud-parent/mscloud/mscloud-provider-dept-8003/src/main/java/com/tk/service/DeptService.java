package com.tk.service;

import java.util.List;

import com.tk.entity.Dept;

public interface DeptService
{
	public boolean add(Dept dept);

	public Dept get(Long id);

	public List<Dept> list();
}
