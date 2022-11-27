# 一、官方提供oauth2相关脚本

## 1、官方地址

https://github.com/spring-attic/spring-security-oauth/blob/main/spring-security-oauth2/src/test/resources/schema.sql

## 2、mysql数据库相应脚本

```
create table oauth_access_token
(
    token_id          varchar(256) null comment 'MD5加密的access_token的值',
    token             blob null comment 'OAuth2AccessToken.java对象序列化后的二进制数据',
    authentication_id varchar(256) not null comment 'MD5加密过的username,client_id,scope'
        primary key,
    user_name         varchar(256) null comment '登录的用户名',
    client_id         varchar(256) null comment '客户端ID',
    authentication    blob null comment 'OAuth2Authentication.java对象序列化后的二进制数据',
    refresh_token     varchar(256) null comment 'MD5加密后的refresh_token的值'
) comment '访问令牌' charset = utf8;
```

```
create table oauth_approvals
(
    userId         varchar(256) null comment '登录的用户名',
    clientId       varchar(256) null comment '客户端ID',
    scope          varchar(256) null comment '申请的权限',
    status         varchar(10) null comment '状态(Approve或Deny)',
    expiresAt      datetime null comment '过期时间',
    lastModifiedAt datetime null comment '最终修改时间'
) comment '授权记录' charset = utf8;
```

```
create table oauth_client_details
(
    client_id               varchar(128) not null comment '客户端ID' primary key,
    resource_ids            varchar(256) null comment '资源ID集合，多个资源时用英文逗号分隔',
    client_secret           varchar(256) null comment '客户端密匙',
    scope                   varchar(256) null comment '客户端申请的权限范围',
    authorized_grant_types  varchar(256) null comment '客户端支持的grant_type',
    web_server_redirect_uri varchar(256) null comment '重定向URI',
    authorities             varchar(256) null comment '客户端所拥有的SpringSecurity的权限值，多个用英文逗号分隔',
    access_token_validity   int null comment '访问令牌有效时间值(单位秒)',
    refresh_token_validity  int null comment '更新令牌有效时间值(单位秒)',
    additional_information  varchar(4096) null comment '预留字段',
    autoapprove             varchar(256) null comment '用户是否自动Approval操作'
) comment '客户端信息' charset = utf8;
```

```
create table oauth_client_token
(
    token_id          varchar(256) null comment 'MD5加密的access_token值',
    token             blob null comment 'OAuth2AccessToken.java对象序列化后的二进制数据',
    authentication_id varchar(128) not null comment 'MD5加密过的username,client_id,scope'
        primary key,
    user_name         varchar(256) null comment '登录的用户名',
    client_id         varchar(256) null comment '客户端ID'
) comment '该表用于在客户端系统中存储从服务端获取的token数据' charset = utf8;
```

```
create table oauth_code
(
    code           varchar(256) null comment '授权码(未加密)',
    authentication blob null comment 'AuthorizationRequestHolder.java对象序列化后的二进制数据'
) comment '授权码' charset = utf8;

```

```
create table oauth_refresh_token
(
    token_id       varchar(256) null comment 'MD5加密过的refresh_token的值',
    token          blob null comment 'OAuth2RefreshToken.java对象序列化后的二进制数据',
    authentication blob null comment 'OAuth2Authentication.java对象序列化后的二进制数据'
) comment '刷新令牌' charset = utf8;
```

## 3、表介绍

### oauth_client_details（客户端信息）

    对`oauth_client_details`的操作主要集中在`JdbcClientDetailsService.java`中

| 字段名                  | 字段说明                                                     |
| ----------------------- | ------------------------------------------------------------ |
| client_id               | 主键,必须唯一,不能为空. 用于唯一标识每一个客户端(client); 在注册时必须填写(也可由服务端自动生成). 对于不同的grant_type,该字段都是必须的. 在实际应用中的另一个名称叫appKey,与client_id是同一个概念 |
| resource_ids            | 客户端所能访问的资源id集合,多个资源时用逗号(,)分隔,如: “unity-resource,mobileresource”. 该字段的值必须来源于与security.xml中标签‹oauth2:resource-server的属性resource-id值一致. 在security.xml配置有几个‹oauth2:resource-server标签, 则该字段可以使用几个值. 在实际应用中, 我们一般将资源进行分类,并分别配置对应的‹oauth2:resource-server,如订单资源配置一个‹oauth2:resource-server, 用户资源又配置一个‹oauth2:resource-server. 当注册客户端时,根据实际需要可选择资源id,也可根据不同的注册流程,赋予对应的资源id |
| client_secret           | 用于指定客户端(client)的访问密匙; 在注册时必须填写(也可由服务端自动生成). 对于不同的grant_type,该字段都是必须的. 在实际应用中的另一个名称叫appSecret,与client_secret是同一个概念 |
| scope                   | 指定客户端申请的权限范围,可选值包括read,write,trust;若有多个权限范围用逗号(,)分隔,如:“read,write”. scope的值与security.xml中配置的‹intercept-url的access属性有关系.如‹intercept-url的配置为‹intercept-url pattern="/m/**"access=“ROLE_MOBILE,SCOPE_READ”/>则说明访问该URL时的客户端必须有read权限范围. write的配置值为SCOPE_WRITE, trust的配置值为SCOPE_TRUST. 在实际应该中, 该值一般由服务端指定, 常用的值为read,write |
| authorized_grant_types  | 指定客户端支持的grant_type,可选值包括authorization_code,password,refresh_token,implicit,client_credentials, 若支持多个grant_type用逗号(,)分隔,如: “authorization_code,password”. 在实际应用中,当注册时,该字段是一般服务器端指定的,而不是由申请者去选择的,最常用的grant_type组合有:“authorization_code,refresh_token”(针对通过浏览器访问的客户端);“password,refresh_token”(针对移动设备的客户端). implicit与client_credentials在实际中很少使用. |
| web_server_redirect_uri | 客户端的重定向URI,可为空, 当grant_type为authorization_code或implicit时, 在Oauth的流程中会使用并检查与注册时填写的redirect_uri是否一致. 下面分别说明:当grant_type=authorization_code时, 第一步 从 spring-oauth-server获取 'code’时客户端发起请求时必须有redirect_uri参数, 该参数的值必须与 web_server_redirect_uri的值一致. 第二步 用 ‘code’ 换取 ‘access_token’ 时客户也必须传递相同的redirect_uri. 在实际应用中,web_server_redirect_uri在注册时是必须填写的, 一般用来处理服务器返回的code, 验证state是否合法与通过code去换取access_token值.在spring-oauth-client项目中, 可具体参考AuthorizationCodeController.java中的authorizationCodeCallback方法.当grant_type=implicit时通过redirect_uri的hash值来传递access_token值.如:http://localhost:7777/spring-oauth-client/implicit#access_token=dc891f4a-ac88-4ba6-8224-a2497e013865&token_type=bearer&expires_in=43199然后客户端通过JS等从hash值中取到access_token值. |
| authorities             | 指定客户端所拥有的Spring Security的权限值,可选, 若有多个权限值,用逗号(,)分隔, 如:"ROLE_<br/> |
| access_token_validity   | 设定客户端的access_token的有效时间值(单位:秒),可选, 若不设定值则使用默认的有效时间值(60 * 60 * 12, 12小时). 在服务端获取的access_token JSON数据中的expires_in字段的值即为当前access_token的有效时间值. 在项目中, 可具体参考DefaultTokenServices.java中属性accessTokenValiditySeconds. 在实际应用中, 该值一般是由服务端处理的, 不需要客户端自定义. |
| refresh_token_validity  | 设定客户端的refresh_token的有效时间值(单位:秒),可选,若不设定值则使用默认的有效时间值(60 * 60 * 24 * 30, 30天). 若客户端的grant_type不包括refresh_token,则不用关心该字段 在项目中, 可具体参考DefaultTokenServices.java中属性refreshTokenValiditySeconds. 在实际应用中, 该值一般是由服务端处理的, 不需要客户端自定义. |
| additional_information  | 这是一个预留的字段,在Oauth的流程中没有实际的使用,可选,但若设置值,必须是JSON格式的数据,如:{“country”:“CN”,“country_code”:“086”}按照spring-security-oauth项目中对该字段的描述 Additional information for this client, not need by the vanilla OAuth protocolbut might be useful, for example,for storing descriptive information. (详见ClientDetails.java的getAdditionalInformation()方法的注释)在实际应用中, 可以用该字段来存储关于客户端的一些其他信息,如客户端的国家,地区,注册时的IP地址等等.create_time数据的创建时间,精确到秒,由数据库在插入数据时取当前系统时间自动生成(扩展字段) |
| autoapprove             | 设置用户是否自动Approval操作, 默认值为 ‘false’, 可选值包括 ‘true’,‘false’, ‘read’,‘write’. 该字段只适用于grant_type="authorization_code"的情况,当用户登录成功后,若该值为’true’或支持的scope值,则会跳过用户Approve的页面, 直接授权. 该字段与 trusted 有类似的功能, 是spring-security-oauth2 的 2.0 版本后添加的新属性. 在项目中,主要操作oauth_client_details表的类是JdbcClientDetailsService.java, 更多的细节请参考该类. 也可以根据实际的需要,去扩展或修改该类的实现 |

### oauth_access_token（访问令牌）

     对`oauth_access_token`表的操作主要集中在`JdbcTokenStore.java`中

| 字段名            | 字段说明                                                     |
| ----------------- | ------------------------------------------------------------ |
| token_id          | MD5加密的access_token的值.                                   |
| token             | 这是一个二进制的字段, 存储的数据是OAuth2AccessToken.java对象序列化后的二进制数据 |
| authentication_id | 该字段具有唯一性, 是根据当前的username(如果有),client_id与scope通过MD5加密生成的. 具体实现请参考DefaultClientKeyGenerator.java类 |
| user_name         | 登录时的用户名                                               |
| client_id         | 认证授权客户端ID                                             |
| authentication    | OAuth2Authentication.java对象序列化后的二进制数据            |
| refresh_token     | MD5加密后的refresh_token的值.在项目中,主要操作oauth_access_token表的对象是JdbcTokenStore.java. |

### oauth_client_token（客户端令牌）

    该表用于在客户端系统中存储从服务端获取的token数据. 对oauth_client_token表的主要操作在JdbcClientTokenServices.java中

| 字段名            | 字段说明                                                     |
| ----------------- | ------------------------------------------------------------ |
| token_id          | 从服务器端获取到的access_token的值.                          |
| token             | 这是一个二进制的字段,存储的数据是OAuth2AccessToken.java对象序列化后的二进制数据 |
| authentication_id | 该字段具有唯一性, 是根据当前的username(如果有),client_id与scope通过MD5加密生成的. 具体实现请参考DefaultClientKeyGenerator.java类. |
| user_name         | 登录时的用户名                                               |
| client_id         | 认证授权客户端ID                                             |

### oauth_refresh_token（刷新令牌）

	在项目中,主要操作oauth_refresh_token表的对象是JdbcTokenStore.java. (与操作oauth_access_token表的对象一样)。 如果客户端的grant_type不支持refresh_token,则不会使用该表

| 字段名         | 字段说明                                                |
| -------------- | ------------------------------------------------------- |
| token_id       | MD5加密过的refresh_token的值                            |
| token          | OAuth2RefreshToken.java对象序列化后的二进制数据         |
| authentication | 存储将OAuth2Authentication.java对象序列化后的二进制数据 |

### oauth_code（授权码）

	在项目中,主要操作oauth_code表的对象是JdbcAuthorizationCodeServices.java。 只有当grant_type为"authorization_code"时,该表中才会有数据产生; 其他的grant_type没有使用该表

| 字段名         | 字段说明                                                |
| -------------- | ------------------------------------------------------- |
| code           | 存储服务端系统生成的code的值(未加密))                   |
| authentication | AuthorizationRequestHolder.java对象序列化后的二进制数据 |

# 二、授权服务器配置

### 1、 启用授权服务器功能

### 2、配置客户端详情

### 3、配置管理令牌服务和令牌端点的安全约束

    .pathMapping("/oauth/token","/abc/oauth/token")
则获取token请求地址为:contextPath + /abc/oauth/token
<br>
    以上3步通过继承AuthorizationServerConfigurerAdapter进行配置

`@EnableAuthorizationServer
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {
//略...
}`
<hr>

### 4、Web安全配置
 继承WebSecurityConfigurerAdapter进行配置

<hr>


# 三、授权模式验证
### 1、密码模式

POST <br>
`localhost:8080/oauth/token?grant_type=password&username=john.carnell&password=password1&scope=all&client_id=eagleeye&client_secret=thisissecret 
`
<br>
&nbsp;&nbsp;&nbsp;&nbsp;其中
grant_type=password
username和password指定用户详情中指定的账号信息
client_id和client_secret指定客户端详情中配置的客户端

&nbsp;&nbsp;&nbsp;&nbsp;响应结果<br>
`{
"access_token": "d7154954-6c36-486e-8778-ba73aedfca14",
"token_type": "bearer",
"refresh_token": "2b1b7526-8ac9-44cf-ac85-28cc3f50e89f", //默认过期时间30天
"expires_in": 7199,  // 默认过期时间12小时
"scope": "all"
}`

&nbsp;&nbsp;&nbsp;&nbsp;此模式十分简单，但是却意味着直接将用户敏感信息泄漏给了client，因此这就说明这种模式只能用于client是我 们自己开发的情况下。因此密码模式一般用于我们自己开发的，第一方原生App或第一方单页面应用。

### 2、客户端模式

POST <br>
`localhost:8080/oauth/token?grant_type=client_credentials&scope=all&client_id=eagleeye&client_secret=thisissecret`
<br>
&nbsp;&nbsp;&nbsp;&nbsp;其中
client_id和client_secret指定客户端详情中配置的客户端

&nbsp;&nbsp;&nbsp;&nbsp;响应结果<br>
`{
"access_token": "4d968cd2-7282-4026-8586-024c9f5f2988",
"token_type": "bearer",
"expires_in": 7199,  
"scope": "all"
}`

&nbsp;&nbsp;&nbsp;&nbsp;此模式是最方便但最不安全的模式。因此这就要求我们对client完全的信任，
而client本身也是安全的。因 此这种模式一般用来提供给我们完全信任的服务器端服务。
比如，<b>合作方系统对接，拉取一组用户信息</b>。



### 3、授权码模式

#### 3.1 发起/oauth/authorize请求
GET <br>
`localhost:8080/oauth/authorize?client_id=eagleeye3&response_type=code&scope=all&redirect_uri=http://www.baidu.com
`

#### 3.2 重定向到登录页面

 输入正确的账号和密码，页面重定向https://www.baidu.com/?code=VfOGbZ


#### 3.3 使用授权码获取accessToken
POST <br>
 `localhost:8080/oauth/token?grant_type=authorization_code&scope=all&client_id=eagleeye&client_secret=thisissecret&code=VfOGbZ&redirect_uri=http://www.baidu.com`


&nbsp;&nbsp;&nbsp;&nbsp;响应结果<br>
`{
"access_token": "0575be2f-0386-4ddd-9c6a-5917eb10e33d",
"token_type": "bearer",
"refresh_token": "78cfd20f-c747-463d-b977-c9c7990cd8c7",
"expires_in": 42098,
"scope": "all"
}
`

### 四、oauth2如何refreshToken
当使用密码模式时access_token过期后，可以使用refreshToken重新获取access_token

POST
`localhost:8080/oauth/token?grant_type=refresh_token&scope=all
&client_id=eagleeye&client_secret=thisissecret&refresh_token=14bbf37f-9445-4b17-baa9-12be6dd803f7`

<br>
&nbsp;&nbsp;&nbsp;&nbsp;其中<br>
grant_type=refresh_token ：指明使用刷新token
client_id和client_secret：指定客户端详情中配置的客户端
refresh_token：指定刷新token值

&nbsp;&nbsp;&nbsp;&nbsp;响应结果<br>
`{
"access_token": "c7d2ade6-c4c4-4c8a-a0f9-fed91b8b1ddd",
"token_type": "bearer",
"refresh_token": "14bbf37f-9445-4b17-baa9-12be6dd803f7",
"expires_in": 9,
"scope": "all"
}`



# 五、问题汇总
### 1、There is no PasswordEncoder mapped for the id "null"
在Spring Security 5.0之前，PasswordEncoder 的默认值为 NoOpPasswordEncoder 既表示为纯文本密码
在 Spring Security 5.0.x 以后，密码的一般格式为：{ID} encodedPassword ，ID 主要用于查找 PasswordEncoder 对应的编码标识符，并且encodedPassword 是所选的原始编码密码 PasswordEncoder。ID 必须书写在密码的前面，开始用{，和 结束 }。如果 ID 找不到，ID 则为null

`/**
 * 配置使用明文验证密码
* @return
*/
@Bean
public PasswordEncoder passwordEncoder(){
return  NoOpPasswordEncoder.getInstance();
}`
