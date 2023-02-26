package com.thoughtmechanix.licenses.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * 拦截RestTemplate请求
 */
@Slf4j
public class UserContextInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        UserContext context = UserContextHolder.getContext();
        log.info("correlationId={},authToken={}", context.getCorrelationId(), context.getAuthToken());
        HttpHeaders headers = request.getHeaders();
        headers.add(UserContext.CORRELATION_ID, context.getCorrelationId());
        headers.add(UserContext.AUTH_TOKEN, context.getAuthToken());

        return execution.execute(request, body);
    }
}