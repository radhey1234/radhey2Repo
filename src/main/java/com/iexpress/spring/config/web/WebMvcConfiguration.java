package com.iexpress.spring.config.web;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.iexpress.spring.resolver.JwtUserRequestResolver;

@EnableWebMvc
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
	
	/**
	 * Adding resolver to Spring Web mvc
	 */
    @Override	
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        JwtUserRequestResolver jwtResolver = new JwtUserRequestResolver();
        argumentResolvers.add(jwtResolver);
    }
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
          .addResourceHandler("/resources/**")
          .addResourceLocations("/resources/");	
    }

}