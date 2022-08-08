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
    return container -> container.setContextPath("/spring-web-mvc");
}
```

Spring Boot version 2:

```java
@Bean
public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>
  webServerFactoryCustomizer() {
    return factory -> factory.setContextPath("/spring-web-mvc");
}
```

##### Commandline arguments

```bash
java -jar app.jar --server.servlet.context-path=/spring-mvc-intro-context-path
```

##### Java System properties

```java
public static void main(String[] args) {
    System.setProperty("server.servlet.context-path", "/spring-web-mvc");
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

* `preHandle`, before the actual handler is executed
* `postHandle`, after the handler is executed
* `afterCompletion`, after the complete request has finished

The `prehandle(..)` method returns a boolean value. You can use this method to break or continue the processing of the execution chain. When this method returns true, the handler execution chain continues. When it returns false, the DispatcherServlet assumes the interceptor itself has taken care of requests (and, for example, rendered an appropriate view) and does not continue executing the other interceptors and the actual handler in the execution chain

![Interceptions](https://example.com "Interceptions")

Note that `postHandle(..)` is less useful with `@ResponseBody` and `ResponseEntity` methods for which the response is written and committed within the `HandlerAdapter` and before `postHandle(..)`. That means it is too late to make any changes to the response, such as adding an extra header.

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

### Exception Handling

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

> [Exercise 1](https://github.com/tvanwinckel/intro-spring-web/blob/main/spring-webMVC/Introduction_SpringWebMVC_exercises/exercise-1.md "Exercise 1"): Setting up an application with a custom context path.
[Exercise 2](https://example.com "Exercise 2"): Intercepting requests.

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

> [Exercise 3](https://example.com "Exercise 3"): Creating controllers and apply request mappings to them.
[Exercise 3b](https://example.com "Exercise 3b"): Expanding reaquest mappings with wildcard patterns.

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

> [Exercise 4](https://example.com "Exercise 4"): Consuming and producing.

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

> [Exercise 5](https://example.com "Exercise 5"): Accepting variables into your handler methods.

### Handler Methods

`@RequestMapping` handler methods have a flexible signature and can choose from a range of supported controller method arguments and return values.

#### Method Arguments

| Controller method argument | Description |
| --- | --- |
| @RequestParam | For access to the Servlet request parameters, including multipart files. Parameter values are converted to the declared method argument type. |
| @RequestHeader | For access to request headers. Header values are converted to the declared method argument type. |
| @RequestBody | For access to the HTTP request body. |
| @ModelAttribute | For access to an existing attribute in the model (instantiated if not present) with data binding and validation applied. |
| @PathVariable | For access to URI template variables. |
| HttpMethod | The HTTP method of the request. |

##### @RequestParam

You can use the @RequestParam annotation to bind Servlet request parameters (that is, query parameters or form data) to a method argument in a controller.

The following example shows how to do so:

```java
@Controller
@RequestMapping("/pets")
public class EditPetForm {

 @GetMapping
 public String setupForm(@RequestParam("petId") int petId, Model model) { 
  Pet pet = this.clinic.loadPet(petId);
  model.addAttribute("pet", pet);
  return "petForm";
 }
}
```

By default, method parameters that use this annotation are required, but you can specify that a method parameter is optional by setting the `@RequestParam` annotation’s required flag to false or by declaring the argument with an java.util.Optional wrapper.

##### @RequestHeader

You can use the @RequestHeader annotation to bind a request header to a method argument in a controller.

```txt
Host                    localhost:8080
Accept                  text/html,application/xhtml+xml,application/xml;q=0.9
Accept-Language         fr,en-gb;q=0.7,en;q=0.3
Accept-Encoding         gzip,deflate
Accept-Charset          ISO-8859-1,utf-8;q=0.7,*;q=0.7
Keep-Alive              300
```

The following example gets the value of the Accept-Encoding and Keep-Alive headers:

```java
@GetMapping("/demo")
public void handle(
  @RequestHeader("Accept-Encoding") String encoding, 
  @RequestHeader("Keep-Alive") long keepAlive) { 
 //...
}
```

##### @Requestbody

You can use the @RequestBody annotation to have the request body read and deserialized into an Object through an HttpMessageConverter. The following example uses a @RequestBody argument:

```java
@PostMapping("/accounts")
public void handle(@RequestBody Account account) {
 // ...
}
```

##### @ModelAttribute

You can use the `@ModelAttribute` annotation on a method argument to access an attribute from the model or have it be instantiated if not present. The model attribute is also overlain with values from HTTP Servlet request parameters whose names match to field names. This is referred to as data binding, and it saves you from having to deal with parsing and converting individual query parameters and form fields.

```java
@PostMapping("/owners/{ownerId}/pets/{petId}/edit")
public String processSubmit(@ModelAttribute Pet pet) { }
```

#### Return Values

| Controller method return value | Description |
| --- | --- |
| @ResponseBody | The return value is converted through HttpMessageConverter implementations and written to the response. |
| HttpEntity<B>, ResponseEntity<B> | The return value that specifies the full response (including HTTP headers and body) |
| HttpHeaders | For returning a response with headers and no body. |
| String | A view name to be resolved with ViewResolver implementations and used together with the implicit model. |
| Model & View (ModelAndView) | The view and model attributes to use and, optionally, a response status. |

##### @ResponseBody

You can use the @ResponseBody annotation on a method to have the return serialized to the response body through an HttpMessageConverter. The following listing shows an example:

```java
@GetMapping("/accounts/{id}")
@ResponseBody
public Account handle() {
 // ...
}
```

##### HttpEntity

HttpEntity is more or less identical to using mvc-ann-requestbody but is based on a container object that exposes request headers and body.

```java
@PostMapping("/accounts")
public void handle(HttpEntity<Account> entity) {
 // ...
}
```

##### Jackson JSON

CHECK IF WE WANT TO INCLUDE THIS

### Models and Views

#### Model

Simply put, the `Model` is an object allowing you to supply attributes used for rendering `views`. To provide a view with usable data, we simply add this data to its Model object. Additionally, maps with attributes can be merged with `Model` instances:

```java
@GetMapping("/showViewPage")
public String passParametersWithModel(Model model) {
    Map<String, String> map = new HashMap<>();
    map.put("spring", "mvc");
    model.addAttribute("message", "Baeldung");
    model.mergeAttributes(map);
    return "viewPage";
}
```

#### ModelMap

```java
@GetMapping("/printViewPage")
public String passParametersWithModelMap(ModelMap map) {
    map.addAttribute("welcomeMessage", "welcome");
    map.addAttribute("message", "Baeldung");
    return "viewPage";
}
```

The advantage of `ModelMap` is that it gives us the ability to pass a collection of values and treat these values as if they were within a `Map`.

#### ModelAndView

```java
@GetMapping("/goToViewPage")
public ModelAndView passParametersWithModelAndView() {
    ModelAndView modelAndView = new ModelAndView("viewPage");
    modelAndView.addObject("message", "Baeldung");
    return modelAndView;
}
```

This interface allows us to pass all the information required by Spring MVC in one return.

#### Model Attributes

A controller can have any number of @ModelAttribute methods. All such methods are invoked before @RequestMapping methods in the same controller. A @ModelAttribute method can also be shared across controllers through @ControllerAdvice.
@ModelAttribute methods have flexible method signatures. They support many of the same arguments as @RequestMapping methods, except for @ModelAttribute itself or anything related to the request body.

You can use the @ModelAttribute annotation:

* On a method argument in @RequestMapping methods to create or access an Object from the model
* As a method-level annotation in @Controller or @ControllerAdvice classes that help to initialize the model prior to any @RequestMapping method invocation.
* On a @RequestMapping method to mark its return value is a model attribute.

```java
@Controller
public class EmployeeController {

    @RequestMapping(value = "/addEmployee", method = RequestMethod.POST)
    public String submit(@ModelAttribute("employee") Employee employee, ModelMap model) {
        // ...
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("msg", "Hello World!");
    }
}
```

> During exercises extend the postHandleMethod of an interceptor with adding data to the model/view

### Exceptions

`@Controller` and `@ControllerAdvice` classes can have `@ExceptionHandler` methods to handle exceptions from controller methods, as the following example shows:

```java
@Controller
public class SimpleController {

 // ...

 @ExceptionHandler
 public ResponseEntity<String> handle(IOException ex) {
  // ...
 }
}
```

The exception may match against a top-level exception being propagated (that is, a direct IOException being thrown) or against the immediate cause within a top-level wrapper exception (for example, an IOException wrapped inside an IllegalStateException).
For matching exception types, preferably declare the target exception as a method argument, as the preceding example shows. When multiple exception methods match, a root exception match is generally preferred to a cause exception match. More specifically, the ExceptionDepthComparator is used to sort exceptions based on their depth from the thrown exception type.

Alternatively, the annotation declaration may narrow the exception types to match, as the following example shows:

```java
@ExceptionHandler({FileSystemException.class, RemoteException.class})
public ResponseEntity<String> handle(IOException ex) {
 // ...
}

Alternatively you can use a 'generic' exception type that matches both exceptions.

@ExceptionHandler({FileSystemException.class, RemoteException.class})
public ResponseEntity<String> handle(Exception ex) {
 // ...
}
```

It is generally recommended that you are as specific as possible in the argument signature, reducing the potential for mismatches between root and cause exception types. Consider breaking a multi-matching method into individual `@ExceptionHandler` methods, each matching a single specific exception type through its signature.

### Controller Advice

Typically `@ExceptionHandler` and `@ModelAttribute` methods apply within the `@Controller` class (or class hierarchy) in which they are declared. If you want such methods to apply more globally (across controllers), you can declare them in a class annotated with `@ControllerAdvice` or `@RestControllerAdvice`.
`@ControllerAdvice` is annotated with @Component, which means such classes can be registered as Spring beans through component scanning.

On startup, the infrastructure classes for `@RequestMapping` and `@ExceptionHandler` methods detect Spring beans annotated with `@ControllerAdvice` and then apply their methods at runtime. Global `@ExceptionHandler` methods (from a `@ControllerAdvice`) are applied after local ones (from the `@Controller`). By contrast global `@ModelAttribute` methods are applied before local ones.

By default, `@ControllerAdvice` methods apply to every request (that is, all controllers), but you can narrow that down to a subset of controllers by using attributes on the annotation, as the following example shows:

```java
// Target all Controllers annotated with @RestController
@ControllerAdvice(annotations = RestController.class)
public class ExampleAdvice1 {}

// Target all Controllers within specific packages
@ControllerAdvice("org.example.controllers")
public class ExampleAdvice2 {}

// Target all Controllers assignable to specific classes
@ControllerAdvice(assignableTypes = {ControllerInterface.class, AbstractController.class})
public class ExampleAdvice3 {}
```

---

## View Technologies

The use of view technologies in Spring MVC is pluggable, whether you decide to use Thymeleaf, Groovy Markup Templates, JSPs, or other technologies, is primarily a matter of a configuration change.

For this document we will be focussing on the Thymeleaf view technology.

WHAT DO WE NEED TO DO TO GET STARTED WITH THYMELEAF

```java

Code examples for working with Thymeleaf

```

[Getting with Thymeleaf and Spring](https://spring.io/guides/gs/handling-form-submission/)
[Creating a form](https://www.baeldung.com/spring-mvc-form-tutorial)

---

## Functional Endpoints

TBD

---

## URI Links

This section describes various options available in the Spring Framework to work with URI’s.

### UriComponents

`UriComponentsBuilder` helps to build URI’s from URI templates with variables, as the following example shows:

```java
UriComponents uriComponents = UriComponentsBuilder
  .fromUriString("https://example.com/hotels/{hotel}")  
  .queryParam("q", "{q}")  
  .encode() 
  .build(); 

URI uri = uriComponents.expand("Westin", "123").toUri();  
```

1. Static factory method with a URI template.
2. Add or replace URI components.
3. Request to have the URI template and URI variables encoded.
4. Build a UriComponents.
5. Expand variables and obtain the URI.

The example can be shortened to:

```java
URI uri = UriComponentsBuilder
  .fromUriString("https://example.com/hotels/{hotel}")
  .queryParam("q", "{q}")
  .build("Westin", "123"); 

Or even

URI uri = UriComponentsBuilder
  .fromUriString("https://example.com/hotels/{hotel}?q={q}")
  .build("Westin", "123");
```

### UriBuilder

`UriComponentsBuilder` implements `UriBuilder`. You can create a `UriBuilder`, in turn, with a `UriBuilderFactory`. Together, `UriBuilderFactory` and UriBuilder provide a pluggable mechanism to build URIs from URI templates, based on shared configuration, such as a base URL, encoding preferences, and other details.

```java
String baseUrl = "https://example.com";
DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory(baseUrl);

URI uri = uriBuilderFactory.uriString("/hotels/{hotel}")
  .queryParam("q", "{q}")
  .build("Westin", "123");
```

### UriEncoding

UriComponentsBuilder exposes encoding options at two levels:

* UriComponentsBuilder#encode(): Pre-encodes the URI template first and then strictly encodes URI variables when expanded.
* UriComponents#encode(): Encodes URI components after URI variables are expanded.

Both options replace non-ASCII and illegal characters with escaped octets. However, the first option also replaces characters with reserved meaning that appear in URI variables.

The following example uses the first option:

```java
URI uri = UriComponentsBuilder.fromPath("/hotel list/{city}")
  .queryParam("q", "{q}")
  .encode()
  .buildAndExpand("New York", "foo+bar")
  .toUri();

OR

URI uri = UriComponentsBuilder.fromPath("/hotel list/{city}?q={q}")
  .build("New York", "foo+bar")

// Result is "/hotel%20list/New%20York?q=foo%2Bbar"
```

You can also create URIs to your own controllers:

```java
@Controller
@RequestMapping("/hotels/{hotel}")
public class BookingController {

    @GetMapping("/bookings/{booking}")
    public ModelAndView getBooking(@PathVariable Long booking) {
        // ...
    }
}



UriComponents uriComponents = MvcUriComponentsBuilder
    .fromMethodName(BookingController.class, "getBooking", 21).buildAndExpand(42);

URI uri = uriComponents.encode().toUri();

```

---

## Cors

TBD

---

## Sources and Information

* [Spring Documentation](https://spring.getdocs.org/en-US/spring-framework-docs/docs/spring-web/spring-web.html)
* [Context Path](https://www.baeldung.com/spring-boot-context-path)
* [Interceptions](https://www.baeldung.com/spring-mvc-handlerinterceptor)
* [Multipart Resolving](https://www.baeldung.com/spring-file-upload)
* [Models and Views](https://www.baeldung.com/spring-mvc-model-model-map-model-view)
