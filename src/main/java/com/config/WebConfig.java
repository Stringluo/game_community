package com.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            registry.addResourceHandler("/img/**").
                    addResourceLocations("file:E:/02-javaStu/Game_Community/img/");
        } else {
            //非win系统 可以根据逻辑再做处理;
            registry.addResourceHandler("/img/**").
                    addResourceLocations("file:/www/wwwroot/game_community/static/img/");
        }
    }

}
