package com.thoughtmechanix.authentication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

@Configuration
@EnableResourceServer     //启用资源服务器功能
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

    public static final String RESOURCE_ID = "res1";

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        // 指定resourceId，否则访问/oauth/user,虽然指定token信息，还是报错
        // Invalid token does not contain resource id (oauth2-resource)
        resources.resourceId(RESOURCE_ID)
                        .stateless(true)
                // 指定资源服务令牌解析服务
                        .tokenServices(resourceServerTokenServices());
    }


    /**
     * 配置资源服务令牌解析服务，此处也可以通过配置属性来配置
     * @return
     */
    @Bean
    public ResourceServerTokenServices resourceServerTokenServices(){
        //使用远程服务请求授权服务器校验token,必须指定校验token 的url、client_id，client_secret
        RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
        remoteTokenServices.setClientId("eagleeye");
        remoteTokenServices.setClientSecret("thisissecret");
        // 设置token校验地址
        remoteTokenServices.setCheckTokenEndpointUrl("http://localhost:8080/oauth/check_token");
        return remoteTokenServices;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                // 指定/oauth/user必须使用token访问
                .requestMatchers().antMatchers("/oauth/user")
                .and()
                // 其他请求必须认证
                .authorizeRequests().anyRequest().authenticated();
    }

}
