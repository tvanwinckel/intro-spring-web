# Exercise 1: Setting up a custom context path

**Make sure you have the SpringWebMvc and Thymeleaf dependencies installed**

We are given a controller returning a basic 'Hello World' view. We would like our controller to be accessible through a custom path, for example:

* /spring-web-mvc
* /spring-web-mvc/my-controller
* /spring-web-mvc/any/path/I/want

Given a basic "HelloWorldController" controller class and "helloWorldView.html" view.

```java
@Controller
public class HelloWorldController {

    private final static Logger LOGGER = LoggerFactory.getLogger(HelloWorldController.class);

    @GetMapping
    public String controllerMethod() {
        LOGGER.info("Received a GET request on the controllerMethod.");
        return "helloWorldView";
    }
}
```

View is required to be put in the correct path: `/src/main/java/resources/templates`

```html
<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Hello World View</title>
</head>
<body>
<h1>Hello World!</h1>
</body>
</html>
```
