package com.zack.znjj.common.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class JWTConfiguration extends WebMvcConfigurerAdapter {

    /**
     * 需要先实例化 才能在拦截器里面应用，不然的话 service会报空指针
     *
     * @return
     */
    @Bean
    public HandlerInterceptor getMyInterceptor() {
        return new JWTTokenInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getMyInterceptor()).addPathPatterns("/**");
    }
}
