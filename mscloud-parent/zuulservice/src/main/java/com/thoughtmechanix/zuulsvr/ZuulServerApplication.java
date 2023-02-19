package com.thoughtmechanix.zuulsvr;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;


@SpringBootApplication
@EnableZuulProxy
@EnableDiscoveryClient
public class ZuulServerApplication {

//    @Autowired
//    private UserContextInterceptor interceptor;
//
//    @Primary
//    @Bean
//    @LoadBalanced
//    public RestTemplate getCustomRestTemplate() {
//        RestTemplate template = new RestTemplate();
//        List interceptors = template.getInterceptors();
//        if (interceptors == null) {
//            template.setInterceptors(Collections.singletonList(interceptor));
//        } else {
//            interceptors.add(interceptor);
//            template.setInterceptors(interceptors);
//        }
//        return template;
//    }

    public static void main(String[] args) {
        SpringApplication.run(ZuulServerApplication.class, args);
    }
}

