package com.tvanwinckel.webmvc.config;

import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebmvcApplicationConfiguration {

    //TODO: Exercise 1: Using Java configuration
//    @Bean
//    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>
//    webServerFactoryCustomizer() {
//        return factory -> factory.setContextPath("/spring-web-mvc");
//    }
}
