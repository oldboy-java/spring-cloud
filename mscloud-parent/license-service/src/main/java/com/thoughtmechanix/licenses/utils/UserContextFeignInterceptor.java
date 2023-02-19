package com.thoughtmechanix.licenses.utils;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserContextFeignInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(UserContext.CORRELATION_ID, UserContextHolder.getContext().getCorrelationId());
        requestTemplate.header(UserContext.AUTH_TOKEN, UserContextHolder.getContext().getAuthToken());
    }
}
