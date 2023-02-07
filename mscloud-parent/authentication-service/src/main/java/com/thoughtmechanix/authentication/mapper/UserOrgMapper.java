package com.thoughtmechanix.authentication.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.thoughtmechanix.authentication.entity.UserOrganization;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserOrgMapper extends BaseMapper<UserOrganization> {

}
