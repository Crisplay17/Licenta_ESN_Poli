package com.ESN_Poliapp.Proiect;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.http.converter.StringHttpMessageConverter;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/volunteers/profilePicture").allowedOrigins("*");
    }
    @Bean
    public FormHttpMessageConverter formHttpMessageConverter() {
        FormHttpMessageConverter converter = new FormHttpMessageConverter();
        converter.setCharset(StandardCharsets.UTF_8);
        return converter;
    }

    // Adaugăm converterul la lista de convertere a aplicației
    @Bean
    public WebMvcConfigurer webMvcConfigurer(FormHttpMessageConverter formHttpMessageConverter) {
        return new WebMvcConfigurer() {
            @Override
            public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
                converters.add(formHttpMessageConverter);
            }
        };
    }
}
