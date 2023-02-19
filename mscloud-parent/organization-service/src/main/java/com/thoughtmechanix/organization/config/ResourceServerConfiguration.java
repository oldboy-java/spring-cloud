package com.thoughtmechanix.organization.config;


import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;


/**
 * 资源服务器配置
 * OAuth2ResourceServerConfiguration默认会实例化ResourceSecurityConfigurer,会根据配置来设置ResourceId
 * 由于自定义实例化ResourceSecurityConfigurer后,默认实例化不生效，需要手动设置ResourceId
 */
@Configuration
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

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
     * 定义资源访问规则
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception{
        http
        .authorizeRequests()
                // 设置删除组织必须有del_org（见t_permission表）权限，因为授权服务中设置的是权限
          .antMatchers(HttpMethod.DELETE, "/v1/organizations/**")
//          .hasRole("ADMIN")
                // SecurityExpressionRoot 解析hasAuthority
                .hasAuthority("del_org")
          .anyRequest()
          .authenticated();
    }

    @Bean
    @ConfigurationProperties(prefix = "security.oauth2.client")
    public ClientCredentialsResourceDetails clientCredentialsResourceDetails() {
        return new ClientCredentialsResourceDetails();
    }

    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor(){
        return new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), clientCredentialsResourceDetails());
    }

    @Bean
    public OAuth2RestTemplate oauth2RestTemplate() {
        return new OAuth2RestTemplate(clientCredentialsResourceDetails());
    }
}
