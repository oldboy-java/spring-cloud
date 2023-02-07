package com.thoughtmechanix.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO implements Serializable {
    private Long id;
    private String username;
    private String password;
    private String fullname;
    private String mobile;

    // 权限编码集合
    private List<String> authorities;

    // 用户所属组织
    private String organizationId;
}
