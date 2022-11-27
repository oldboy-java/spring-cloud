package com.thoughtmechanix.authentication.config;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;

@Configuration
@EnableAuthorizationServer  //启用授权服务器功能
public class OAuth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter implements ApplicationRunner {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;


    @Autowired
    private DataSource dataSource;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;


    /**
     * 自定义授权码服务配置
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices(){
        // 使用内存存储授权码
//        return new InMemoryAuthorizationCodeServices();

        // 使用数据库存储授权码
        JdbcAuthorizationCodeServices  jdbcAuthorizationCodeServices = new JdbcAuthorizationCodeServices(dataSource);
        return jdbcAuthorizationCodeServices;

    }

    /**
     * 1 配置客户端详情
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        registerAppInMemory(clients);
        clients.withClientDetails(jdbcClientDetailsService());
    }


    /**
     * 配置数据库存储客户端信息：其中客户端：eagleeye是作为资源服务器校验使用的客户端
      *  不能删除，数据库中表oauh_client_details中必须有此客户端的配置数据
     * @return
     */
    @Bean
    public JdbcClientDetailsService jdbcClientDetailsService(){
        // JdbcClientDetailsService 是自带使用jdbcTemplate操作数据库来存储客户端信息
        // 也可以自定义实现ClientDetailsService接口
        // 依赖spring-jdbc
        JdbcClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        // 设置客户端密码加密器，JdbcClientDetailsService默认是不加密的。这里使用自定义加密器
        clientDetailsService.setPasswordEncoder(passwordEncoder);
        return clientDetailsService;
    }


    /**
     * 使用内存存储客户端信息，其中客户端：eagleeye是作为资源服务器校验使用的客户端
     *  不能删除，数据库中表oauh_client_details中必须有此客户端的配置数据
     * @param clients
     */
    @SneakyThrows
    private void registerAppInMemory(ClientDetailsServiceConfigurer clients) {
        // 使用内存模式存储客户端信息，实际开发中需要用户数据库存储
        clients.inMemory()
                // 注册应用程序名称
                .withClient("eagleeye")
                // 注册应用程序秘钥
                .secret("thisissecret")
      //          .secret(new BCryptPasswordEncoder().encode("thisissecret"))  //使用密文
                // 资源列表 （对应资源服务ID
                .resourceIds("res1,res2")
                //设置授权类型：用逗号隔开，支持多种授权类型
                .authorizedGrantTypes("authorization_code", "password","client_credentials","implicit","refresh_token")
                //用来限制客户端的访问范围，如果为空（默认）的话，那么客户端拥有全部的访问范围
                //设置作用域：自定义的字符串如all，read_userinfo等
                .scopes("all")

                //是否自动授权 false：不自动授权，会跳转到授权页面  true:直接颁发令牌
                .autoApprove(false)

                // 验证回调地址
                .redirectUris("http://www.baidu.com")



                // 配置第二个客户端
                .and()
                // 注册应用程序名称
                .withClient("eagleeye2")
                // 注册应用程序秘钥
                .secret("thisissecret2")
                // .secret(new BCryptPasswordEncoder().encode("thisissecret2"))  //使用密文

                // 资源列表 （对应资源服务ID
                .resourceIds("res2")
                //设置授权类型：用逗号隔开，支持多种授权类型。
                .authorizedGrantTypes("authorization_code", "password","client_credentials","implicit","refresh_token")
                //用来限制客户端的访问范围，如果为空（默认）的话，那么客户端拥有全部的访问范围
                //设置作用域：自定义的字符串如all，read_userinfo等
                .scopes("all")

                // 是否自动授权 false：不自动授权，会跳转到授权页面  true:直接颁发令牌
                .autoApprove(false)

                // 验证回调地址
                .redirectUris("http://www.baidu.com");

    }


    /**
     * 使用内存存储token，这个是默认存储方式
     * @return
     */
    @Bean
    public TokenStore tokenStore(){
        // 方式一、使用内存存储token信息
       // return new InMemoryTokenStore();

        // 方式二、使用数据库存储token
//        JdbcTokenStore jdbcTokenStore = new JdbcTokenStore(dataSource);
//        return jdbcTokenStore;

        // 方式三、使用redis存储
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        return redisTokenStore;
    }

    /**
     *  配置授权服务器TokenServices
     * @return
     */
    @Bean
    @Primary
    public AuthorizationServerTokenServices authorizationServerTokenServices() {
        // 配置DefaultTokenServices，其实现了AuthorizationServerTokenServices接口
        DefaultTokenServices service = new DefaultTokenServices();
        // 设置客户端详情
        service.setClientDetailsService(jdbcClientDetailsService());
        // 支持刷新token
        service.setSupportRefreshToken(true);
        //设置token存储方式
        service.setTokenStore(tokenStore());
        // 令牌默认有效期12小时
        service.setAccessTokenValiditySeconds(12*60*60);
        // 刷新令牌默认有效期30天
        service.setRefreshTokenValiditySeconds(30*24*60*60);
        return service;
    }



    /**
     * 2 配置令牌访问端点和令牌访问服务
     * 令牌访问端点这个URL应该被Spring Security保护起来只供授权用户访问
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
      endpoints
              //1 配置授权类型
             //  1.1 认证管理器，使用password授权类型时，需要注入一个 AuthenticationManager 对象。
            .authenticationManager(authenticationManager)
              // 1.2  配置用户详细信息
            .userDetailsService(userDetailsService)

              // 1.3 配置授权码服务 （授权码模式需要配置）
              .authorizationCodeServices(authorizationCodeServices())

        //2 配置令牌访问端点（令牌访问端点这个URL应该被Spring Security保护起来只供授权用户访问）
              // 一般无需设置，使用默认端点，这里为了演示可以自定义端点地址（默认地址和自定义地址一样了）
              //授权端点
//      .pathMapping("/oauth/authorize","/oauth/authorize")
//              //令牌端点
//      .pathMapping("/oauth/token","/abc/oauth/token")
//              //用户确认授权提交端点
//      .pathMapping("/oauth/confirm_access","/oauth/confirm_access")
//              //授权服务错误信息端点。
//      .pathMapping(" /oauth/error"," /oauth/error")
//              //用于资源服务访问的令牌解析端点
//      .pathMapping(" /oauth/check_token"," /oauth/check_token")
//              //提供公有密匙的端点，如果你使用JWT令牌的话
//      .pathMapping("  /oauth/token_key","  /oauth/token_key")

      // 3 配置令牌管理服务tokenService
      .tokenServices(authorizationServerTokenServices())

              // 4 设置访问端口支持的请求方式 POST请求
      .allowedTokenEndpointRequestMethods(HttpMethod.POST, HttpMethod.GET);
    }




    /**
     * 用来配置令牌端点的安全约束
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                //  /oauth/token_key 安全配置endpoint完全公开，默认denyAll()
                .tokenKeyAccess("permitAll()")
                // /oauth/check_token 安全配置endpoint完全公开，默认denyAll()
                .checkTokenAccess("permitAll()")
                //允许客户端进行表单认证
                .allowFormAuthenticationForClients()

                // 客户端端登录时密码采用加密器编码 （是指定clientSecret）
                .passwordEncoder(passwordEncoder);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

    }
}
