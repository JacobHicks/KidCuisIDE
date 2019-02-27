package com.seji.kidcuiside;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MVCConfig implements WebMvcConfigurer {

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/easyui/**").addResourceLocations("classpath:/easyui/");
        registry.addResourceHandler("/codemirror-5.44.0/**").addResourceLocations("classpath:/codemirror-5.44.0/");
        registry.addResourceHandler("/java/**").addResourceLocations("classpath:/java/");
        registry.addResourceHandler("/templates/**").addResourceLocations("classpath:/templates/");
        registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/");
    }
}
