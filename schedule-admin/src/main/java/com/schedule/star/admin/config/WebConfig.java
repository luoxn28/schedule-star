package com.schedule.star.admin.config;

import com.schedule.star.admin.aspect.LoginAspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author xiangnan
 * @date 2018/3/10 10:25
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginAspect())
                .addPathPatterns("/**")
                .excludePathPatterns("/login",
                        "/api/**");
        super.addInterceptors(registry);
    }

}
