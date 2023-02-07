package com.thoughtmechanix.authentication.service;


import com.thoughtmechanix.authentication.entity.UserOrganization;

public interface UserOrgService {

    UserOrganization findUserOrgByUserId(Long userId);
}
