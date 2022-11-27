package io.spring2go.authcodeserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;


@Configuration
public class OAuth2Configuration extends  AuthorizationServerConfigurerAdapter {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDetailsService userDetailsService;

	
	/**
	 * 配置客户端详细信息
	 */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients)
            throws Exception {
    	
    	/**********************注册客户端应用程序***********************/
       
    	registerAppInMemory(clients);
    }

    
    /**
     * 定义AuthorizationServerConfigurer中使用的不同组件。这里使用Spring默认验证管理器和用户详细信息服务
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    	endpoints.authenticationManager(authenticationManager);
    	endpoints.userDetailsService(userDetailsService);
    }
    
    /**
     * 这里模拟注册一个应用程序到内存中
     */
    private void registerAppInMemory(ClientDetailsServiceConfigurer clients) throws Exception {
    	 clients.inMemory()
         .withClient("clientapp")  //注册应用程序名称
         .secret("112233")  //注册应用程序秘钥
//         .secret(new BCryptPasswordEncoder().encode("112233"))  //使用密文
         .resourceIds("res1,res2")   //资源列表 （对应资源服务ID）
         .autoApprove(false)     // 是否自动授权 false：不自动授权，会跳转到授权页面  true:直接颁发令牌
         .redirectUris("http://localhost:9001/callback") //设置回调地址
         .authorizedGrantTypes("authorization_code")   //设置授权类型：授权码模式，用逗号隔开，支持多种授权类型
         .scopes("read_userinfo", "read_contacts")   //设置作用域：自定义的字符串如all，read_userinfo等
     
    	// 配置另外的客户端
         .and()
         .withClient("clientapp2")  //注册应用程序名称
         .secret("112233")  //注册应用程序秘钥
//         .secret(new BCryptPasswordEncoder().encode("112233"))  //使用密文
         .resourceIds("res1,res2")   //资源列表 （对应资源服务ID）
         .autoApprove(false)     // 是否自动授权 false：不自动授权，会跳转到授权页面  true:直接颁发令牌
         .redirectUris("http://localhost:9001/callback") //设置回调地址
         .authorizedGrantTypes("authorization_code")   //设置授权类型：授权码模式，用逗号隔开，支持多种授权类型
         .scopes("read_userinfo", "read_contacts")   //设置作用域：自定义的字符串如all，read_userinfo等
         ;
    }
}
