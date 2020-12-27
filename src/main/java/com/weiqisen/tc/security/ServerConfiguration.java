package com.weiqisen.tc.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * @author LYD
 */
@Slf4j
public class ServerConfiguration implements ApplicationListener<WebServerInitializedEvent> {

    private int serverPort;

    private String hostAddress;

    private String contextPath;

    public String getHostAddress() {
        return hostAddress;
    }

    public String getContextPath() {
        return contextPath;
    }

    public String getUrl() {
        return "http://" + this.hostAddress + ":" + this.serverPort + this.contextPath;
    }

    public int getPort() {
        return this.serverPort;
    }


    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        ServerProperties serverProperties = event.getApplicationContext().getBean(ServerProperties.class);
        this.serverPort = event.getWebServer().getPort();
        this.contextPath = serverProperties.getServlet().getContextPath()!=null?serverProperties.getServlet().getContextPath():"";
        try {
            InetAddress address = getIpAddress();
            if(address==null){
                address = Inet4Address.getLocalHost();
            }
            this.hostAddress = address.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        log.info("hostAddress[{}] serverPort[{}] contextPath[{}]",hostAddress,serverPort,contextPath);
    }


    public InetAddress getIpAddress() {
        InetAddress ip = null;
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
                    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        ip = addresses.nextElement();
                        log.info(ip.getHostAddress());
                    }
                    continue;
                } else {
                    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        ip = addresses.nextElement();
                        log.info(ip.getHostAddress());
                        if (ip != null && ip instanceof Inet4Address) {
                            return ip;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("IP地址获取失败" + e.toString());
        }
        return ip;
    }
}
