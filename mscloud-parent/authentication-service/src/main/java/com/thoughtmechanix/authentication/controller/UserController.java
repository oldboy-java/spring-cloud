package com.thoughtmechanix.authentication.controller;

import com.thoughtmechanix.authentication.dto.UserDTO;
import com.thoughtmechanix.authentication.entity.UserEntity;
import com.thoughtmechanix.authentication.entity.UserOrganization;
import com.thoughtmechanix.authentication.service.UserOrgService;
import com.thoughtmechanix.authentication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Objects;

@RestController
@RequestMapping("/oauth")
public class UserController {

    @Autowired
    private UserOrgService userOrgService;

    @Autowired
    private UserService userService;

    /**
     * 获取用户信息
     * @param auth2Authentication
     * @return
     */
    @RequestMapping(value = { "/user" }, produces = "application/json")
    public UserDTO user(OAuth2Authentication auth2Authentication) {
        UserDTO.UserDTOBuilder builder = UserDTO.builder();

        // 如果使用客户端模式等方式获取的token不含有用户信息，需要针对认证结果判断
        if (auth2Authentication.getUserAuthentication() != null) {
            UserEntity user = userService.findUserByUserName(auth2Authentication.getUserAuthentication().getName());
            // 获取用户所属组织
            UserOrganization userOrg = userOrgService.findUserOrgByUserId(user.getId());
            builder.organizationId(Objects.isNull(userOrg)? "": userOrg.getOrganizationId());
            builder.id(user.getId()).username(user.getUsername()).fullname(user.getFullname()).mobile(user.getMobile());
            builder.authorities(new ArrayList(AuthorityUtils.authorityListToSet(auth2Authentication.getUserAuthentication().getAuthorities())));
        }
        return builder.build();
    }

}
