package com.iexpress.spring.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class GlobalControllerInterceptor extends HandlerInterceptorAdapter {

    @Autowired private Environment env;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (("dev").equalsIgnoreCase(env.getProperty("env")) || ("local").equalsIgnoreCase(env.getProperty("env"))) {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "contentType,Content-Type,X-Requested-With,accept,Origin,Authorization");
        }
        return super.preHandle(request, response, handler);
    }

}