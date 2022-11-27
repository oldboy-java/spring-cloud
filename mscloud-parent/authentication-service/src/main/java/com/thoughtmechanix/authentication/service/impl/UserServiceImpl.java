package com.thoughtmechanix.authentication.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.thoughtmechanix.authentication.entity.UserEntity;
import com.thoughtmechanix.authentication.mapper.UserMapper;
import com.thoughtmechanix.authentication.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl  extends ServiceImpl<UserMapper, UserEntity> implements UserService {

  @Autowired
  private UserMapper userMapper;

    @Override
    public UserEntity findUserByUserName(String userName) {
        EntityWrapper<UserEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("username", userName);
        wrapper.last("limit 1");
        return this.selectOne(wrapper);
    }


    @Override
    public List<String> listPermissions(Long id) {
        return userMapper.listPermissionsByUserId(id);
    }


}
