package com.thoughtmechanix.authentication.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * SpringSecurityWeb安全配置
 */
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 配置AuthenticationManager，被Spring Security用于处理验证
     * @return
     * @throws Exception
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

//    /**
//     * 配置用户详情，Spring Security使用UserDetailsService处理返回的用户信息，这些用户信息将
//     * 由Spring Security返回
//     * @return
//     * @throws Exception
//     */
//   @Override
//    @Bean
//    public UserDetailsService userDetailsServiceBean() throws Exception {
//        return super.userDetailsServiceBean();
//    }


    /**
     * 配置个人用户凭证和角色
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }


    /**
     * 配置使用明文验证密码
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
       return  NoOpPasswordEncoder.getInstance();
    }


    /**
     * 安全配置
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http.csrf().disable()
             .authorizeRequests()
               .antMatchers("/oauth/**").permitAll()
               // 客户端操作放行
               .antMatchers("/clients/**","/login","/logout","/actuator/**","/error").permitAll()
               .anyRequest().authenticated()
               // 必须加上formLogin，不然授权码模式报403
               .and()
               .formLogin();
    }
}
