package com.thoughtmechanix.organization.config;

import com.thoughtmechanix.organization.context.UserContextFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.Arrays;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Bean
    public FilterRegistrationBean<UserContextFilter>  userContextFilterFilterRegistrationBean (){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        // 设置过滤器
        filterRegistrationBean.setFilter(new UserContextFilter());
        //设置过滤器的优先级
        filterRegistrationBean.setOrder(90);
        // 指定过滤器拦截路径
        filterRegistrationBean.setUrlPatterns(Arrays.asList("/*"));
        return filterRegistrationBean;
    }

}
