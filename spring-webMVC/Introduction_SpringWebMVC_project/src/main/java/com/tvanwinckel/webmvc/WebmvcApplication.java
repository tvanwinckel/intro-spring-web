package com.tvanwinckel.webmvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebmvcApplication {

	public static void main(String[] args) {
//		TODO Exercise 1: Using Java System properties
		System.setProperty("server.servlet.context-path", "/spring-web-mvc");

		SpringApplication.run(WebmvcApplication.class, args);
	}

}