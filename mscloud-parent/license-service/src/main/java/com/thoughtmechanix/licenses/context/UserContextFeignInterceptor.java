package com.thoughtmechanix.licenses.context;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 拦截feign请求
 */
@Component
@Slf4j
public class UserContextFeignInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        log.info("tmx-correlation-id={}",UserContextHolder.getContext().getCorrelationId());
        requestTemplate.header(UserContext.CORRELATION_ID, UserContextHolder.getContext().getCorrelationId());
        requestTemplate.header(UserContext.AUTH_TOKEN, UserContextHolder.getContext().getAuthToken());
    }
}
