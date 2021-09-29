package com.yu.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.security.core.context.SecurityContextHolder;

public class CommonRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String accessToken = (String) SecurityContextHolder.getContext().getAuthentication().getDetails();
        requestTemplate.header("Authorization", "Bearer "+accessToken);
    }

}
