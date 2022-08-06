package com.tvanwinckel.webmvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloWorldController {

    private final static Logger LOGGER = LoggerFactory.getLogger(HelloWorldController.class);

    @GetMapping
    public String controllerMethod() {
        LOGGER.info("Received a GET request on the controllerMethod.");
        return "helloWorldView";
    }
}
