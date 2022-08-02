# Introduction to Spring Boot

## What is Spring Boot?

Spring Boot is a project which is built on top of the Spring framework. It is a tool that provides a much simpler and faster way to set up, configure and run both simple and web based applications. Spring Boot itself is not a framework.

![Spring Ecosystem](https://github.com/tvanwinckel/intro-spring-web/blob/main/images/SpringBootEcosystem.png?raw=true "Spring Ecosystem")

Spring Boot will focus on these main features:

* Auto-configuration
* Standalone
* Opinioneted
* Production-ready features

### Auto-configuration

Spring Boot will attempt to set up your application based on the surrounding environment as well as dependencies added by the developers.

*For example, if you add a database related dependency to you dependency file (`.pom`, `.gradle`), the Spring framework will automatically assume you want to do something with databases. Hence it will auto-configure your application for database access.
Furthermore, if the dependency appears to be for a very specific database (Oracle, MySQL, Mongo, ...), Spring will be able to make an even more certain assumption and configure the specified database directly.*

![Spring Auto-Config](https://github.com/tvanwinckel/intro-spring-web/blob/main/images/SpringBootAutoConfig.png?raw=true "Spring Auto-Config")

Setting up your application for auto-configuration can be done with the following annotation: `@EnableAutoConfiguration`

### Standalone

Usually when you want to deploy a Java (web) application you need to:

1. Package your application
2. Choose a type of webserver you want to use
3. Configure the chosen webserver
4. Deploy the application to the webserver
5. Start the webserver

With Spring Boot this process can be simplified as it only requires you to:

1. Package your application
2. Run the application with a simple command like `java -jar MyApplication.jar`

Spring Boot will take care of the rest by starting and configuring an embeddes webserver and deploy your application directly in it. Usually a Tomcat webserver is chosen unless specified otherwise.

![Spring Standalone](https://github.com/tvanwinckel/intro-spring-web/blob/main/images/SpringBootStandalone.png?raw=true "Spring Standalone")

### Opinionated

When writing an application, in this case a Java application, you will be presented with a lot of choices. Everything starting from deciding the weg-, logging- and collecitn frameworks, to the build tools you use. In many cases developers tend to you the same most popular libraries.
Therefore all that Spring Boot does is to bundle, load and configure these dependencies int he most standard way. Preventing developers from spending a lot of time configuring the same things over and over again.

### Production-ready

Spring Boot will provide you with certain features like metrics, health checks and externalized configuration out of the box.

## Getting started with Spring boot

The easiest way to get started with Spring boot is through the [Spring Initializr](https://start.spring.io/). You will be able to select a dependency manager, language, project metadata, ... and the Spring Boot version. In addition you can directly add several dependencies to your project.

---

## Sources

* [Spring Boot Project](https://spring.io/projects/spring-boot)
