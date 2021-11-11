package com.lguplus.ukids.admin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
	/******/
    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "HEAD", "OPTIONS")
                .allowedHeaders("Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method",
                        "access-control-allow-methods", "Session-Key", "User-Id", "x-correlation-id", "x-session-id",
                        "x-employee-id")
                .exposedHeaders("Access-Control-Allow-Headers", "Access-Control-Allow-Origin",
                        "Access-Control-Allow-Credentials", "locale", "Content-Disposition")
                .allowCredentials(true).maxAge(3000L);
    }
    //CCCCCCCOOOOOONNNNNNFFFFFILLLLLLLCTTTTTT
}
