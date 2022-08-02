# Introduction to Spring Web MVC

## Introduction and overview

> Spring Web MVC is the original web framework built on the Servlet API and has been included in the Spring framework from the beginning. The name has been derived form its source module `sping-webmvc`, but is also known as **Spring Web** or **Spring MVC**.

The goal of this document is to offer you an easy and quick way to get started with Spring Web. We will focus on support for Servlet-stack web applications build on the Servlet API and deployed to Servlet containers. Following topics will be covered:

* Dispatcherservlet
* Controllers
* Request mapping
* Media types
  * Parameters and headers
  * Handler methods
  * Models
  * Exception handling
  * Controller Advice
* Extras
  * URI links
  * CORS
  * Caching
  * View Technologies
  * Forms

---

## The Dispatcherservlet

Spring MVC, as many other web frameworks, is designed aroudn the *Front Controller* pattern where a central *Servlet*, the *DispatcherServlet*, provides a shared algorithm for request processing while actual work is performed by configurable delegate components. This model is flexible and supports diverse workflows.

### The Front Controller pattern

![Front Controller Pattern](https://example.com "Front Controller Pattern")

1. An incoming request will be sent to the **Front Controller** (the Servlet)
2. The **Front Controller** decides to whom it has to hand over the request, based on the request headers.
3. The **controller** that took the request will process the request by sending it to a suitable service class.
4. After all processing is done, the **controller** receives the **model** from the Service or Data Acces layer.
5. The **controller** sends the model to the **Front Controller**.
6. The **Dispatcher Servlet** finds the view template, using a view resolver and sends the model to it.
7. Using the **View Template** and **model**, a view page is build and sent back to the Front Controller.
8. The **Front Controller** sends out the constructed view page back to the the one who originally requested it.

### Servlets

The *DispatcherServlet*, needs to be declared and mapped according to the Servlet specification by using *Java configuration* or in *web.xml*. In turn, the DispatcherServlet uses Spring configuration to discover the delegate components it needs for request mapping, view resolution, exception handling, and more.

The following example registers and initializes the DispatcherServlet which is auto-detected by the Servlet container. Alternatively one could exted the `AbstractAnnotationConfigDispatcherServletInitializer` and override specific methods:

```java
public class MyWebApplicationInitializer implements WebApplicationInitializer {

 @Override
 public void onStartup(ServletContext servletCxt) {

  // Load Spring web application configuration
  AnnotationConfigWebApplicationContext ac = new AnnotationConfigWebApplicationContext();
  ac.register(AppConfig.class);
  ac.refresh();

  // Create and register the DispatcherServlet
  DispatcherServlet servlet = new DispatcherServlet(ac);
  ServletRegistration.Dynamic registration = servletCxt.addServlet("app", servlet);
  registration.setLoadOnStartup(1);
  registration.addMapping("/app/*");
 }
}
```

As an alternative the Servlet can also be registered and initialized with the following `web.xml`

```xml
<web-app>

 <listener>
  <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
 </listener>

 <context-param>
  <param-name>contextConfigLocation</param-name>
  <param-value>/WEB-INF/app-context.xml</param-value>
 </context-param>

 <servlet>
  <servlet-name>app</servlet-name>
  <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
  <init-param>
   <param-name>contextConfigLocation</param-name>
   <param-value></param-value>
  </init-param>
  <load-on-startup>1</load-on-startup>
 </servlet>

 <servlet-mapping>
  <servlet-name>app</servlet-name>
  <url-pattern>/app/*</url-pattern>
 </servlet-mapping>

</web-app>
```

### Configuring the Servlet with Spring Boot

Spring Boot allows us to configure the Servlet a lot easier than the methods we've seen above. It allows you to use:

* Java configuration
* Commandline arguments
* Java System properties
* OS environment variables
* Applicaiton properties

#### Examples

**Java Configuration**

```java
```

**Commandline arguments**

```bash
```

**Java System properties**

```bash
```

**OS Environmetn variables**

```bash
```

**Application properties**

```bash
```

---

## Sources

* [Spring Docs](https://spring.getdocs.org/en-US/spring-framework-docs/docs/spring-web/spring-web.html)
