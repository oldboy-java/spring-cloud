package com.tk.entity;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain=true)  //链式访问
public class Dept implements Serializable {

	@ApiModelProperty(value="部门编码")
	private Long 	deptno; // 主键
	
	@ApiModelProperty(value = "部门名称")
	private String 	dname; // 部门名称
	
	@ApiModelProperty(value  ="来源")
	private String 	db_source;// 来自那个数据库，因为微服务架构可以一个服务对应一个数据库，同一个信息被存储到不同数据库
	
	public Dept(String dname)
	{
		super();
		this.dname = dname;
	}
}
