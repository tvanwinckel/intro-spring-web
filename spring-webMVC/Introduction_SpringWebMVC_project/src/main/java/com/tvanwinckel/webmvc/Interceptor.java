package com.tvanwinckel.webmvc;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Interceptor implements HandlerInterceptor {

    private final static Logger LOGGER = LoggerFactory.getLogger(Interceptor.class);

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        LOGGER.info("Pre handling the request url: " + request.getRequestURL().toString());

        if (request.getRequestURL().toString().contains("/inventory")) {
            LOGGER.info(">> We applied a condition to the request url, it appeared to be ok. " +
                    "Therefore we return true, allowing us to continue processing the request.");
            return true;
        } else {
            LOGGER.warn(">> We applied a condition to the request url, it appeared not to be ok. " +
                    "Therefore we return false, allowing us to abort processing the request.");
            return false;
        }
    }

    @Override
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView modelAndView) throws Exception {
        LOGGER.info("Post handling the request.");
    }

    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception ex) throws Exception {
        LOGGER.info("Everything appears to be done, request handling is finished and a view should be presented to the user.");
    }


}
