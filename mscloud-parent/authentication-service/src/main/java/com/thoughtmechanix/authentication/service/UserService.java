package com.thoughtmechanix.authentication.service;


import com.thoughtmechanix.authentication.entity.UserEntity;

import java.util.List;

public interface UserService {

    UserEntity findUserByUserName(String userName);


    List<String> listPermissions(Long id);


}
