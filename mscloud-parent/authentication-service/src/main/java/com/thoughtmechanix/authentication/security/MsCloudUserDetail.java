package com.thoughtmechanix.authentication.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtmechanix.authentication.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MsCloudUserDetail implements UserDetails {

    private UserDTO userDTO;

    @Override
    @JsonIgnore //忽略UserDetails中的authorities序列化redis
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> authorities = userDTO.getAuthorities();
        if (!CollectionUtils.isEmpty(authorities)) {
            return   AuthorityUtils.createAuthorityList(authorities.toArray(new String[authorities.size()]));
        }
        return Collections.emptyList();
    }

    @Override
    @JsonIgnore //忽略UserDetails中的password序列化redis
    public String getPassword() {
        return userDTO.getPassword();
    }

    @Override
    @JsonIgnore  //忽略UserDetails中的username序列化redis
    public String getUsername() {
        return userDTO.getUsername();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}
