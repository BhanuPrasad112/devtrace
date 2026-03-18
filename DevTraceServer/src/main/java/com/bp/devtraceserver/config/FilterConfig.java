package com.bp.devtraceserver.config;


import com.bp.devtraceserver.filter.RequestCaptureFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final RequestCaptureFilter requestCaptureFilter;


    @Bean
    public FilterRegistrationBean<RequestCaptureFilter> captureFilter(){

        FilterRegistrationBean<RequestCaptureFilter> registration = new FilterRegistrationBean<>();

        registration.setFilter(requestCaptureFilter);

        registration.addUrlPatterns("/*");

        registration.setOrder(1);

        return  registration;

    }
}
