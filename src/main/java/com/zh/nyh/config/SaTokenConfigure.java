package com.zh.nyh.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfigure implements WebMvcConfigurer{
	
	@Autowired
	MagicSaInterceptor magicSaInterceptor;
	
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	
        // 注册 Sa-Token 拦截器，定义详细认证规则 
        registry.addInterceptor(magicSaInterceptor).addPathPatterns("/**").excludePathPatterns("/error");
    }

}