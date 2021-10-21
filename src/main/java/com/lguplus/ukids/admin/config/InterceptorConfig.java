package com.lguplus.ukids.admin.config;

import java.util.Arrays;
import java.util.List;

import com.lguplus.ukids.admin.interceptor.AuthenticationInterceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private AuthenticationInterceptor authenticationInterceptor;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        List<String> urlPatterns = Arrays.asList("/*", "/*/*", "/*/*/**");
        List<String> common = Arrays.asList("/healthy", "/webjars/**");
        List<String> samples = Arrays.asList("/v1/samples", "/v1/samples/**", "/v1/sample/**");
        List<String> swagger = Arrays.asList("/swagger-ui.html", "/v3/api-docs", "/v3/api-docs/**", "/swagger-ui/", "/swagger-ui/**", "/swagger-resources/**");
        List<String> temporary = Arrays.asList("/v1/pages");

        //registry.addInterceptor(authenticationInterceptor).addPathPatterns(urlPatterns).excludePathPatterns(common).excludePathPatterns(samples).excludePathPatterns(swagger).excludePathPatterns(temporary);
    }
}
