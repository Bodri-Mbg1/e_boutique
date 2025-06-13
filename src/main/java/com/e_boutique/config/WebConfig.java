package com.e_boutique.config;


import com.e_boutique.security.RateLimitingFilter;
import com.e_boutique.security.XssProtectionFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean<RateLimitingFilter> rateLimitingFilter() {
        FilterRegistrationBean<RateLimitingFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RateLimitingFilter());
        registration.addUrlPatterns("/*"); // Appliquer à tout
        registration.setOrder(1); // prioritaire
        return registration;
    }

    @Bean
    public FilterRegistrationBean<XssProtectionFilter> xssFilter() {
        FilterRegistrationBean<XssProtectionFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new XssProtectionFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(2);
        return registration;
    }

}
