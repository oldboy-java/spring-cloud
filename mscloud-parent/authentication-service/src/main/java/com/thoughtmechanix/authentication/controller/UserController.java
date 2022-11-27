package com.thoughtmechanix.authentication.controller;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/oauth")
public class UserController {

    /**
     * 获取用户信息
     * @param user
     * @return
     */
    @RequestMapping(value = { "/user" }, produces = "application/json")
    public Map<String, Object> user(OAuth2Authentication user) {
        Map<String, Object> userInfo = new HashMap<>();

        // 如果使用客户端模式等方式获取的token不含有用户信息，需要针对认证结果判断
        if (user.getUserAuthentication() != null) {
            userInfo.put("user", user.getUserAuthentication().getPrincipal());
            userInfo.put("authorities", AuthorityUtils.authorityListToSet(user.getUserAuthentication().getAuthorities()));
            return userInfo;
        }else {
            return new HashMap<>();
        }
    }


}
