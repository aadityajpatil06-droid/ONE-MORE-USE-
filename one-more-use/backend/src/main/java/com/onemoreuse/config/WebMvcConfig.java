package com.onemoreuse.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Web configuration for CORS policy and static resource handling.
 *
 * 1. Enables Cross-Origin Resource Sharing (CORS) so the separate vanilla HTML/JS
 *    frontend running on file:// or any local dev port can call Spring Boot REST APIs.
 * 2. Maps the URL pattern /uploads/** to the physical uploads folder on the server
 *    so uploaded item photos can be viewed directly in any browser.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            if (!java.nio.file.Files.exists(uploadPath)) {
                Path localUploads = Paths.get("uploads").toAbsolutePath().normalize();
                if (java.nio.file.Files.exists(localUploads)) {
                    uploadPath = localUploads;
                } else {
                    java.nio.file.Files.createDirectories(uploadPath);
                }
            }
        } catch (Exception ignored) {
        }

        String uploadUri = uploadPath.toUri().toString();

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadUri.endsWith("/") ? uploadUri : uploadUri + "/");
    }
}
