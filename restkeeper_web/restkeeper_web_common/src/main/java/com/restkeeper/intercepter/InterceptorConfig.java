package com.restkeeper.intercepter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Value("${app.type:''}")
    private String appType;

    @Bean
    public HandlerInterceptor MyInterceptor() {
        if("micApp".equals(appType)){
            return new MicroHandlerInterceptor();
        }else {
            return new WebHandlerInterceptor();
        }

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptor = registry.addInterceptor(MyInterceptor());
        // 拦截所有、排除
        interceptor.addPathPatterns("/**")
                .excludePathPatterns("/login","/login");
    }

    /**
     * 跨域支持配置
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowCredentials(true).allowedOrigins("*").allowedMethods("GET", "PUT", "DELETE", "POST", "OPTIONS").maxAge(3600);
    }


}

