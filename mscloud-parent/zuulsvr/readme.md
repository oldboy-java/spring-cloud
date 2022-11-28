## 案例一:通过zull实现请求头全链路传递
	实现从zuul请求开始，实现请求头从服务A传递到服务B，即从mscloud-consumer-dept-Feign 传递到 mscloud-provider-dept-hystrix-8001

##  实现步骤

http://localhost:8080/api/depts/consumer/dept/get/1

###   zuulsvr工程

    1、配置
        zuul:
					  prefix:  /api
					  routes:
					    MSCLOUD-CONSUMER-DEPT-FEIGN: /depts/**
					    ignored-services: "*"
    
    2、定义TrackingFilter，并在请求头中增加 tmx-correlation-id，根据请求路径路由到mscloud-consumer-dept-Feign服务
      
2、 mscloud-consumer-dept-Feign 
     
     
    1、定义UserContextFilter拦截zuul转发请求， 过滤器从请求头中获取 tmx-correlation-id等头信息，保存到用户上下   文UserContext中，放行请求到目标请求地址  。请求转发到DeptController_Consumer控制器的get方法即：/consumer/dept/get/{id}地址上。
    
    2、定义 UserContextInterceptor implements ClientHttpRequestInterceptor 并从上下文中解析出请求头信息放入到
    请求头中
    
    3、定义RestTemplate模板并设置拦截器UserContextInterceptor
    
    4、在get方法中使用RestTemplate调用mscloud-provider-dept-hystrix-8001中的接口。
    
     
 3、mscloud-provider-dept-hystrix-8001
      
       在DeptController的get请求方法中，从请求头中获取tmx-correlation-id信息
 