//package com.opencloud.device.configuration;//package com.opencloud.health.configuration;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///**
// * @author: WangJie
// * @date: 2019/8/16 0016
// **/
//@Slf4j
//@Component
//@WebFilter(urlPatterns = "/*", filterName = "CorsFilter")
//public class WebCorsFilter implements Filter {
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        if (log.isDebugEnabled()) {
//            log.debug("CorsFilter init success");
//        }
//    }
//
//    @Override
//    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
//        HttpServletResponse response = (HttpServletResponse) res;
//        HttpServletRequest reqs = (HttpServletRequest) req;
//        String curOrigin = reqs.getHeader("Origin");
//        response.setHeader("Access-Control-Allow-Origin", curOrigin == null ? "true" : curOrigin);
//        response.setHeader("Access-Control-Allow-Credentials", "true");
//        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PATCH, DELETE, PUT, OPTIONS");
//        String allowHeaders = "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With, Authorization";
//        response.addHeader("Access-Control-Allow-Headers", allowHeaders);
//        chain.doFilter(req, res);
//    }
//
//    @Override
//    public void destroy() {
//
//    }
//
//}
