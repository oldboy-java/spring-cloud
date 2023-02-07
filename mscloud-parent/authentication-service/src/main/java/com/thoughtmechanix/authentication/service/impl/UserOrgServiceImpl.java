package com.thoughtmechanix.authentication.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.thoughtmechanix.authentication.entity.UserOrganization;
import com.thoughtmechanix.authentication.mapper.UserOrgMapper;
import com.thoughtmechanix.authentication.service.UserOrgService;
import org.springframework.stereotype.Service;

@Service
public class UserOrgServiceImpl extends ServiceImpl<UserOrgMapper, UserOrganization> implements UserOrgService {
    @Override
    public UserOrganization findUserOrgByUserId(Long userId) {
        EntityWrapper<UserOrganization> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.last("limit 1");
        return this.selectOne(wrapper);
    }
}
