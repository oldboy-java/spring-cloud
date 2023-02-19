package com.thoughtmechanix.licenses.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter{
    @Override
    public void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .antMatchers(HttpMethod.DELETE,"/v1/organizations/**")
                   // .hasRole("ADMIN")
                    .hasAuthority("del_org")
                    .anyRequest()
                    .authenticated();
    }

    @Autowired
    private ResourceServerProperties resourceServerTokenServices;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        // 指定resourceId，否则访问/oauth/user,虽然指定token信息，还是报错
        // Invalid token does not contain resource id (oauth2-resource)
        // 设置当前资源服务的访问ID，后续验证token访问权限时会验证token是否包含访问的资源服务ID
        resources.resourceId(resourceServerTokenServices.getResourceId())
                .stateless(true);
    }


 // 不起作用
//    @Bean
//    @ConfigurationProperties(prefix = "security.oauth2.client")
//    public ClientCredentialsResourceDetails clientCredentialsResourceDetails() {
//        return new ClientCredentialsResourceDetails();
//    }
//
//    @Bean
//    public RequestInterceptor oauth2FeignRequestInterceptor(){
//        return new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), clientCredentialsResourceDetails());
//    }
//
//    @Bean
//    public OAuth2RestTemplate oauth2RestTemplate() {
//        return new OAuth2RestTemplate(clientCredentialsResourceDetails());
//    }

}
