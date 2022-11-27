package com.thoughtmechanix.authentication.security;

import com.thoughtmechanix.authentication.dto.UserDTO;
import com.thoughtmechanix.authentication.entity.UserEntity;
import com.thoughtmechanix.authentication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MsCloudUserDetailService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userService.findUserByUserName(username);
        // 账号不存在
        if (user == null) {
            throw  new UsernameNotFoundException("用户账号不存在");
        }
        // 后续DaoAuthenticationProvider中会比对密码是否匹配，不需要手动来处理

        //获取权限
        List<String> permissions = userService.listPermissions(user.getId());
        UserDTO userDTO = UserDTO.builder().id(user.getId()).username(user.getUsername())
                .authorities(permissions)
                .fullname(user.getFullname()).mobile(user.getMobile()).password(user.getPassword()).build();
        return new MsCloudUserDetail(userDTO);
    }
}
