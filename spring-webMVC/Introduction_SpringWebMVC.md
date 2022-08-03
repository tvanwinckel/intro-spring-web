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

The above mentioned options are sorted by priority Spring Boot uses to select the effective configuration. By default content is served on the root context path `/`.

#### Examples

##### Java Configuration

Spring Boot version 1:

```java
@Bean
public EmbeddedServletContainerCustomizer
  embeddedServletContainerCustomizer() {
    return container -> container.setContextPath("/spring-mvc-intro-context-path");
}
```

Spring Boot version 2:

```java
@Bean
public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>
  webServerFactoryCustomizer() {
    return factory -> factory.setContextPath("/spring-mvc-intro-context-path");
}
```

##### Commandline arguments

```bash
java -jar app.jar --server.servlet.context-path=/spring-mvc-intro-context-path
```

##### Java System properties

```java
public static void main(String[] args) {
    System.setProperty("server.servlet.context-path", "/spring-mvc-intro-context-path");
    SpringApplication.run(Application.class, args);
}
```

##### OS Environmetn variables

```bash
Unix:
$ export SERVER_SERVLET_CONTEXT_PATH=/spring-mvc-intro-context-path

Windows:
$ set SERVER_SERVLET_CONTEXT_PATH=/spring-mvc-intro-context-path
```

##### Application properties

```yml
server.servlet.context-path=/spring-mvc-intro-context-path
```

### Context Hierarchy

For most applicatons, having a single *WebApplicationContext* is sufficient. But it is also possible to have a context hierarchy where one root WebApplicationContext is shared acros multiple *DispatcherServlet* instances, each with its own child *WebApplicationContext* configuration.

The root *WebApplicationContext* typically contains infrastructure beans such as data repositories and/or business services that need to be shared acress multiple *Servlet* instances. Those beans are effectively inherited and can be overridden in the Servlet specific child *WebApplicationContext*, that contains beans local tothe given *Servlet*. It is woth mentioning that we can use different contexts to prevent beans registered in one context from becoming accessible in another one. This facilitates teh creation of loosely coupled modules.

The image underneath gives an overview of how that releationship might look like.

![Context Hierarchy](https://example.com "Context Hierarchy")

An example of how one could configure a *WebApplicationContext* hierarchy:

```java
public class MyWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

 @Override
 protected Class<?>[] getRootConfigClasses() {
  return new Class<?>[] { RootConfig.class };
 }

 @Override
 protected Class<?>[] getServletConfigClasses() {
  return new Class<?>[] { ChildContextConfig.class };
 }

 @Override
 protected String[] getServletMappings() {
  return new String[] { "/child-context/*" };
 }
}
```

```xml
<web-app>

 <listener>
  <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
 </listener>

 <context-param>
  <param-name>contextConfigLocation</param-name>
  <param-value>/WEB-INF/root-context.xml</param-value>
 </context-param>

 <servlet>
  <servlet-name>app1</servlet-name>
  <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
  <init-param>
   <param-name>contextConfigLocation</param-name>
   <param-value>/WEB-INF/child-context.xml</param-value>
  </init-param>
  <load-on-startup>1</load-on-startup>
 </servlet>

 <servlet-mapping>
  <servlet-name>ChildContext</servlet-name>
  <url-pattern>/child-context/*</url-pattern>
 </servlet-mapping>

</web-app>
```

### Special Bean Types

The DispatcherServlet delegates to special beans to process requests and render the appropriate responses. By “special beans” we mean Spring-managed Object instances that implement framework contracts. Those usually come with built-in contracts, but you can customize their properties and extend or replace them.

The following table lists the special beans detected by the DispatcherServlet:

| Bean Type | Explanation |
| --- | --- |
| HandlerMapping|Map a request to a handler along a list of interceptors for pre- and post-processing |
| HandlerAdapter | Help the DispatcherServlet to invoke a handler mapped to a request regardless of how the handler is actually invoked |
| HandlerExceptionResolver | Strategy to resolve exceptions possibly mapping them to handlers or error views |
| ViewResolver | Resolve logical String-based view names return from a handler to an actual View to render to the response with |
| MultipartResolver | Abstraction for paring a multi-part request (browser form file upload) with help of a library |

### HandlerMapping & HandlerAdapter

![HandlerMapping & HandlerAdapter](https://example.com "HandlerMapping & HandlerAdapter")

### Interceptions

All HandlerMapping implementations support handler interceptors that are useful when you want to apply specific functionality to certain requests. Interceptors must implement *HandlerInterceptor* from the `org.springframework.web.servlet` package with three methods that should provide enough flexibility to do all kinds of pre-processing and post-processing:

* preHandle, before the actual handler is executed
* postHandle, after the handler is executed
* afterCompletion, after the complete request has finished

The `prehandle(..)` method returns a boolean value. You can use this method to break or continue the processing of the execution chain. When this method returns true, the handler execution chain continues. When it returns false, the DispatcherServlet assumes the interceptor itself has taken care of requests (and, for example, rendered an appropriate view) and does not continue executing the other interceptors and the actual handler in the execution chain

![Interceptions](https://example.com "Interceptions")

Note that `postHandle(..)` is less useful with `@ResponseBody` and `ResponseEntity` methods for which the response is written and committed within the `HandlerAdapter` and before `postHandle(..)`. That means it is too late to make any changes to the response, such as adding an extra header. For such scenarios, you can implement `ResponseBodyAdvice`.

#### Interception Examples

Example for preHandle

```java
@Override
public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    // Some code execution
    return true;
}
```

Example for postHandle

```java
@Override
public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    // Some code execution
}
```

Example for afterCompletion

```java
@Override
public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,Exception ex) {
    // Some code execution
}
```

### Exceptions

If an exception occurs during request mapping or is thrown from a request handler the `DispatcherServlet` delegates to a chain of `HandlerExceptionResolver` beans to resolve the exception and provide alternative handling, which is typically an error response.

The contract of HandlerExceptionResolver specifies that it can return:

* A `ModelAndView` that points to an error view.
* An empty `ModelAndView` if the exception was handled within the resolver.
* `null` if the exception remains unresolved, for subsequent resolvers to try, and, if the exception remains at the end, it is allowed to bubble up to the Servlet container.

### View Resolution

Spring MVC defines the `ViewResolver` and `View` interfaces that let you render models in a browser without tying you to a specific view technology. ViewResolver provides a mapping between view names and actual views. View addresses the preparation of data before handing over to a specific view technology.

![View Resolution](https://example.com "View Resolution")

### Multipart Resolver

`MultipartResolver` from the `org.springframework.web.multipart` package is a strategy for parsing multipart requests including file uploads. There is one implementation based on Apache Commons FileUpload and another based on Servlet 3.0 multipart request parsing.

To use Apache Commons FileUpload, you can configure a bean of type `CommonsMultipartResolver` with a name of `multipartResolver`. **You also need to have `commons-fileupload` as a dependency on your classpath.**

```java
@Bean(name = "multipartResolver")
public CommonsMultipartResolver multipartResolver() {
    CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
    multipartResolver.setMaxUploadSize(100000);
    return multipartResolver;
}
```

Servlet 3.0 multipart parsing needs to be enabled through Servlet container configuration.

**REQUIRES EXTRA REFINEMENT, SEE BAELDUNG SOURCE**

```java
public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

 @Override
 protected void customizeRegistration(ServletRegistration.Dynamic registration) {

  // Optionally also set maxFileSize, maxRequestSize, fileSizeThreshold
  registration.setMultipartConfig(new MultipartConfigElement("/tmp"));
 }
}
```

### Quick recap overview

![Front Controller Pattern](https://example.com "Front Controller Pattern")

---

## Controllers

Spring MVC provides an annotation-based programming model where `@Controller` and `@RestController` components use annotations to express request mappings, request input, exception handling, and more. Annotated controllers have flexible method signatures and do not have to extend base classes nor implement specific interfaces.

```java
@Controller
public class HelloController {

 @GetMapping("/hello")
 public String handle(Model model) {
  model.addAttribute("message", "Hello World!");
  return "index";
 }
}
```

To enable auto-detection of such controller beans, you can add component scanning to your Java configuration. Alternatively Spring Boot enables component scanning by default.

### Request Mapping

You can use the `@RequestMapping` annotation to map requests to controllers methods. It has various attributes to match by URL, HTTP method, request parameters, headers, and media types. You can use it at the class level to express shared mappings or at the method level to narrow down to a specific endpoint mapping.

There are also HTTP method specific shortcut variants of `@RequestMapping`:

* `@GetMapping`
* `@PostMapping`
* `@PutMapping`
* `@DeleteMapping`
* `@PatchMapping`

Example of class and method level mapping:

```java
@Controller
@RequestMapping("/persons")
class PersonController {

 @GetMapping("/{id}")
 public Person getPerson(@PathVariable Long id) {
  // ...
 }

 @PostMapping
 @ResponseStatus(HttpStatus.CREATED)
 public void add(@RequestBody Person person) {
  // ...
 }
}
```

### URI Patterns

You can map requests by using the following global patterns and wildcards:

* `?` matches one character
* `*` matches zero or more characters within a path segment
* `**` match zero or more path segments

Example for the `?` pattern:

```java
@GetMapping("/t?st")
public String questionMarkPattern(final HttpServletRequest request, final Model model) {
   model.addAttribute("pattern", "Question mark pattern: " + request.getRequestURI());
   return "pattern";
}

Accepted Patterns:
  - test
  - tost
  - tast
  - tist
  - txst
```

Example for the `*` pattern:

```java
@GetMapping("*")
public String starPattern(final HttpServletRequest request, final Model model) {
   model.addAttribute("pattern", "Star pattern: " + request.getRequestURI());
   return "pattern";
}

Accepted Patterns:
  - /path 
  - /another-Path
  - /file.txt
```

Example for the `**` pattern:

```java
@GetMapping("**")
public String doubleStarPattern(final HttpServletRequest request, final Model model) {
   model.addAttribute("pattern", "Double star pattern: " + request.getRequestURI());
   return "pattern";
}

Accepted Patterns:
  - /path 
  - /another/path
  - /a/very/long/path/with/a/file.txt
```

You can also declare URI variables and access their values with `@PathVariable` as shown below:

```java
 @GetMapping("/{id}")
 public Person getPerson(@PathVariable Long id) {
  // ...
 }
```

Note that URI variables can also be declared on class level. URI variables are automatically converted to the appropriate type, or a `TypeMismatchException` is raised. Simple types (`int`, `long`, `Date`, and so on) are supported by default.
You can explicitly name URI variables, but you can leave that detail out if the names are the same.

The example given below shows how an URI variable can be auto-mapped or mapped based on name:

```java
 @GetMapping("/{id}")
 public Person getPerson(@PathVariable Long id) {
  // ...
 }

  @GetMapping("/{obscureVariableName}")
 public Person getPerson(@PathVariable("obscureVariableName") Long id) {
  // ...
 }
```

### Consumable Media Types

You can narrow the request mapping based on the `Content-Type` of the request:

```java
@PostMapping(path = "/pets", consumes = "application/json") 
public void addPet(@RequestBody Pet pet) {
 // ...
}
```

The `consumes` attribute also supports negation expressions — for example, `!text/plain` means any content type other than `text/plain`.
You can declare a shared consumes attribute at the class level. However, when used at the class level, a method-level consumes attribute overrides rather than extends the class-level declaration.

> `MediaType` provides constants for commonly used media types, such as `APPLICATION_JSON_VALUE` and `APPLICATION_XML_VALUE`.

### Producible Media Types

You can narrow the request mapping based on the Accept request header and the list of content types that a controller method produces.

```java
@GetMapping(path = "/pets/{petId}", produces = "application/json") 
@ResponseBody
public Pet getPet(@PathVariable String petId) {
 // ...
}
```

 Negated expressions are also supported — for example, `!text/plain` means any content type other than `text/plain`.
You can declare a shared `produces~ attribute at the class level. However, when used at the class level, a method-level produces attribute overrides rather than extends the class-level declaration.

### Parameters & Headers

You can narrow request mappings based on request parameter conditions. You can test for the presence of a request parameter `myParam`, for the absence of one `!myParam`, or for a specific value `myParam=myValue`.

Testing whether a parameter equals a specific value.

```java
@GetMapping(path = "/pets/{petId}", params = "myParam=myValue") 
public void findPet(@PathVariable String petId) {
 // ...
}
```

You can also use the same with request header conditions, as the following example shows:

```java
@GetMapping(path = "/pets", headers = "myHeader=myValue") 
public void findPet(@PathVariable String petId) {
 // ...
}
```

---

## Sources

* [Spring Docs](https://spring.getdocs.org/en-US/spring-framework-docs/docs/spring-web/spring-web.html)
* [Context Path](https://www.baeldung.com/spring-boot-context-path)
* [Interceptions](https://www.baeldung.com/spring-mvc-handlerinterceptor)
* [Multipart Resolving](https://www.baeldung.com/spring-file-upload)
