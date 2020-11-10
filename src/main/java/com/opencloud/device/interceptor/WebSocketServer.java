//package com.opencloud.device.interceptor;
//
//import org.springframework.stereotype.Component;
//
//import javax.websocket.*;
//import javax.websocket.server.PathParam;
//import javax.websocket.server.ServerEndpoint;
//import java.io.IOException;
//import java.util.List;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.atomic.AtomicInteger;
///**
// * @author jihao
// * @version 1.0
// * @date 2020-08-11 10:52
// */
//@ServerEndpoint("/webSocket/{userName}")
//@Component
//public class WebSocketServer {
//    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
//    private static AtomicInteger onlineNum = new AtomicInteger();
//
//    //concurrent包的线程安全Set，用来存放每个客户端对应的WebSocketServer对象。
//    private static ConcurrentHashMap<String, List<Session>> sessionTenantPools = new ConcurrentHashMap<>();
//
//    //concurrent包的线程安全Set，用来存放每个客户端对应的WebSocketServer对象。
//    private static ConcurrentHashMap<String, Session> sessionUserPools = new ConcurrentHashMap<>();
//
//
//    //发送消息
//    public void sendMessage(Session session, String message) throws IOException {
//        if(session != null){
//            synchronized (session) {
////                System.out.println("发送数据：" + message);
//                session.getBasicRemote().sendText(message);
//            }
//        }
//    }
//    //给指定用户发送信息
//    public void sendUserInfo(String userId, String message){
//        Session session = sessionUserPools.get(userId);
//        try {
//            sendMessage(session, message);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    //建立连接成功调用
//    @OnOpen
//    public void onOpen(Session session, @PathParam(value = "userName") String userName){
//        String[] userList = userName.split("$");
//        String tenantId = userList[0];
//        String userId = userList[1];
//        if(tenantId!=null){
//            List<Session> tenantSession = sessionTenantPools.get(tenantId);
//            if(tenantSession!=null){
//                tenantSession.remove(session);
//                tenantSession.add(session);
//                sessionTenantPools.put(tenantId,tenantSession);
////                sessionTenantPools.put(tenantId,session);
//            }
//        }
//        if(userId!=null){
//            Session userSession = sessionUserPools.get(userId);
//            if(userSession!=null){
//                sessionUserPools.put(userId,session);
//            }
//        }
////        sessionPools.put(tenantId, session);
//        addOnlineCount();
//        System.out.println(tenantId + "加入webSocket！当前人数为" + onlineNum);
//        try {
//            sendMessage(session, "欢迎" + tenantId + "加入连接！");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    //关闭连接时调用
//    @OnClose
//    public void onClose(@PathParam(value = "userName") String userName){
//        String[] userList = userName.split("$");
//        String tenantId = userList[0];
//        String userId = userList[1];
//        Session session = sessionUserPools.get(userId);
//        sessionUserPools.remove(userId);
//        List<Session> tenantSession = sessionTenantPools.get(tenantId);
//        if(tenantSession!=null&&tenantSession.size()>0){
//            tenantSession.remove(session);
//        }
//        subOnlineCount();
//        System.out.println(userId + "断开webSocket连接！当前人数为" + onlineNum);
//    }
//
//    //收到客户端信息
//    @OnMessage
//    public void onMessage(String message) throws IOException{
//        message = "客户端：" + message + ",已收到";
//        System.out.println(message);
//        for (Session session: sessionUserPools.values()) {
//            try {
//                sendMessage(session, message);
//            } catch(Exception e){
//                e.printStackTrace();
//                continue;
//            }
//        }
//    }
//
//    //错误时调用
//    @OnError
//    public void onError(Session session, Throwable throwable){
//        System.out.println("发生错误");
//        throwable.printStackTrace();
//    }
//
//    public static void addOnlineCount(){
//        onlineNum.incrementAndGet();
//    }
//
//    public static void subOnlineCount() {
//        onlineNum.decrementAndGet();
//    }
//}
