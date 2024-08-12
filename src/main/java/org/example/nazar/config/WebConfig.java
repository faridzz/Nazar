package org.example.nazar.config;

import org.example.nazar.interceptors.ReqAndResCounterInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final ReqAndResCounterInterceptor reqAndResCounterInterceptor;

    public WebConfig(ReqAndResCounterInterceptor reqAndResCounterInterceptor) {
        this.reqAndResCounterInterceptor = reqAndResCounterInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(reqAndResCounterInterceptor);
    }
}
