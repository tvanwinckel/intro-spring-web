# Exercise 1: Solutions

Using Java configuration

```java
@Configuration
public class WebmvcApplicationConfiguration {

    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>
    webServerFactoryCustomizer() {
        return factory -> factory.setContextPath("/spring-web-mvc");
    }
}
```

Using Java system properties

```java
public static void main(String[] args) {
    System.setProperty("server.servlet.context-path", "/spring-web-mvc");

    SpringApplication.run(WebmvcApplication.class, args);
}
```

Using Spring Boot properties

```properties
spring.mvc.servlet.path=/spring-web-mvc
```
