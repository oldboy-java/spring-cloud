package com.thoughtmechanix.authentication.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer     //启用资源服务器功能
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private ResourceServerProperties resourceServerTokenServices;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        // 指定resourceId，否则访问/oauth/user,虽然指定token信息，还是报错
        // Invalid token does not contain resource id (oauth2-resource)
        resources.resourceId(resourceServerTokenServices.getResourceId())
                        .stateless(true);
    }


    /**
     * 资源服务器中必须配置可以直接访问的地址
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors().disable();
        http.authorizeRequests()
                .antMatchers("/oauth/**").permitAll()
               .antMatchers("/clients/**","/login","/logout","/actuator/**","/error").permitAll()
        .anyRequest().authenticated();
    }

}
