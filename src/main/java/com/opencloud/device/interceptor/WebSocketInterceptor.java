//package com.opencloud.device.interceptor;
//
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.http.server.ServletServerHttpRequest;
//import org.springframework.web.socket.server.HandshakeInterceptor;
//import org.springframework.web.socket.WebSocketHandler;
//import java.util.Map;
//
///**
// * @author jihao
// * @version 1.0
// * @date 2020-07-23 16:22
// */
//public class WebSocketInterceptor implements HandshakeInterceptor {
//
//    /**
//     * handler处理前调用,attributes属性最终在WebSocketSession里,可能通过webSocketSession.getAttributes().get(key值)获得
//     */
//    @Override
//    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse serverHttpResponse,WebSocketHandler webSocketHandler, Map<String, Object> attributes) throws Exception {
//        if (request instanceof ServletServerHttpRequest) {
//            ServletServerHttpRequest serverHttpRequest = (ServletServerHttpRequest) request;
//            // 获取请求路径携带的参数
//            String user = serverHttpRequest.getServletRequest().getParameter("user");
//            attributes.put("user", user);
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    @Override
//    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse,WebSocketHandler webSocketHandler, Exception e) {
//
//    }
//}
