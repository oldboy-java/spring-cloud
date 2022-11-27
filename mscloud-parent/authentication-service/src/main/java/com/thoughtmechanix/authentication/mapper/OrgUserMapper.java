package com.thoughtmechanix.authentication.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.thoughtmechanix.authentication.entity.UserOrganization;
import org.springframework.stereotype.Repository;

@Repository
public interface OrgUserMapper extends BaseMapper<UserOrganization> {
    public UserOrganization findByUserName(String userName);
}
