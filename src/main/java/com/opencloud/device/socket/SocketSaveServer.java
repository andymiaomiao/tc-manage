package com.opencloud.device.socket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.opencloud.device.form.req.DeviceLofInfoRes;
import com.opencloud.device.properties.DeviceManageProperties;
import com.opencloud.device.properties.StorageProperties;
import com.opencloud.device.service.IotDeviceLogService;
import com.opencloud.device.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * @author jihao
 * @version 1.0
 * @date 2020-08-05 09:56
 */
@Slf4j
@Component
public class SocketSaveServer extends Thread {


    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private DeviceManageProperties ossProperties;

    @Autowired
    private StorageProperties properties;

    @Autowired
    private IotDeviceLogService iotDeviceLogService;


//    @Autowired
//    private MqttMessageService mqttMessageService;

    private static String socketSaveRedisKey = "SOCKET_SAVE_";

    public void startaccept() {
        log.info("---LogSocketServer---start---");
        try {
            ServerSocket ss = new ServerSocket(ossProperties.getSave().getPort());
            log.info("---LogSocketServer---accept port---" +ossProperties.getSave().getPort());
            while (true) {
                Socket s = ss.accept();
                new Service(s).start();//创建线程
            }
        } catch (IOException e) {
            log.info("接收日志socket中断！");
        }
    }

    @Override
    public void run() {
        startaccept();
    }

    class Service extends Thread {
        private Socket sckclient = null;
        private BufferedInputStream blocal = null;
        private BufferedInputStream brecv = null;

        public Service(Socket sck) {
            this.sckclient = sck;
        }

        @Override
        public void run() {
            try {
                //创建并关联接收流
                brecv = new BufferedInputStream(sckclient.getInputStream());
                while (true) {
                    byte[] bgetbuf = new byte[200];
                    //没有发具体请求过来断开连接
                    if (brecv.read(bgetbuf) == -1) {
                        log.info("---LogSocketServer---service---no request fileinfo detail--close socket!!---");
                        sckclient.shutdownOutput();
                        return;
                    }
                    String strGet = new String(bgetbuf);
                    log.info("---LogSocketServer---service---strGet:" + strGet);
                    //判断格式是否符合，不符合JSON断开连接
                    if (strGet.indexOf("CLOUD-UPLOAD-LOG-FILE") != -1) {
                        log.info("---LogSocketServer---service---6003-OAM-REQUEST-LOG-FILE---");
                    } else {
                        log.info("---LogSocketServer---service---invalid connect，close socket!!---");
                        sckclient.shutdownOutput();
                        return;
                    }
                    String strFullname = "test.zip";
                    String nowFileName = "";
                    String deviceSn = "";
                    int fileReadSize = 0;
                    int fileCountSize = 0;
                    FileOutputStream fos = null;
                    //用json格式的字符串获取一个JSONObject对象
                    JSONObject object = JSON.parseObject(strGet);
                    String strDeviceSn = object.getString("deviceid");
                    String strFileName = object.getString("filename");
                    String strBlockSize = object.getString("filesize");
                    log.info("---LogSocketServer--strFilePath:--:" + strDeviceSn);
                    log.info("---LogSocketServer--strFileName:--:" + strFileName);
                    log.info("---LogSocketServer--strBlockSize:--:" + strBlockSize);
                    strFullname = strFileName;
                    nowFileName = IdWorker.getId() + strFullname.substring(strFullname.lastIndexOf("."));
                    fileCountSize = Integer.valueOf(strBlockSize);
                    deviceSn = strDeviceSn;
                    try {
                        fos = new FileOutputStream(properties.getDisk().getLocation() + File.separator + nowFileName);
                        //根据请求信息，创建一个相应的BufferedInputStream对象读取本地文件
                        blocal = new BufferedInputStream(sckclient.getInputStream());
                        int len = 0;
                        byte[] bytes = new byte[1024];
                        while ((len = brecv.read(bytes)) != -1) {
                            fileReadSize = fileReadSize + len;
                            System.out.println(String.format("传输长读%s,累计长度%s", len, fileReadSize));
                            fos.write(bytes,0,len);
                            fos.flush();
                            System.out.println("fileReadSize:" + fileReadSize + "--------fileCountSize:" + fileCountSize);
                            if (fileReadSize >= fileCountSize) {
                                fos.close();
                                String fileName = IdWorker.getId() + strFullname.substring(strFullname.lastIndexOf("."));
                                DeviceLofInfoRes deviceLofInfoRes = new DeviceLofInfoRes();
    //                            deviceLofInfoRes.setTaskId(id);
                                deviceLofInfoRes.setTaskStatus(1);
                                deviceLofInfoRes.setTaskCode("3001");
                                deviceLofInfoRes.setDeviceSn(deviceSn);
                                deviceLofInfoRes.setUrl(properties.getDownloadurl() + fileName);
                                deviceLofInfoRes.setFileName(strFullname);
                                deviceLofInfoRes.setRealName(nowFileName);
                                deviceLofInfoRes.setTotalSize(fileCountSize+"");
                                deviceLofInfoRes.setCompletedSize(fileReadSize+"");
                                iotDeviceLogService.getDeviceLogToCloud(deviceLofInfoRes);
    //                            mqttMessageService.sendLogTop(deviceSn, strFullname,nowFileName,properties.getDownloadurl() + fileName, (long) fileCountSize, fileReadSize);
                                break;
                                //上传日志下载进度
                            }
                            //上传日志下载进度
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("deviceSn",deviceSn);
                            jsonObject.put("fileCountSize",fileCountSize);
                            jsonObject.put("fileReadSize",fileReadSize);
                            String key = socketSaveRedisKey + deviceSn;
                            log.info("写入redis"+fileReadSize+"总长度："+fileCountSize);
                            redisUtil.set(key, jsonObject.toJSONString(), 5, TimeUnit.MINUTES);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        try {
                            log.info("写入redis"+fileReadSize+"总长度："+fileCountSize);
                            log.info("---LogSocketServer---service---关闭流!---");
                            fos.flush(); //强制刷新输出流
                            fos.close(); //强制关闭输出流
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    //上传日志下载进度
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("deviceSn",deviceSn);
                    jsonObject.put("fileCountSize",fileCountSize);
                    jsonObject.put("fileReadSize",fileReadSize);
                    String key = socketSaveRedisKey + deviceSn;
                    log.info("写入redis"+fileReadSize+"总长度："+fileCountSize);
                    redisUtil.set(key, jsonObject.toJSONString(), 5, TimeUnit.MINUTES);
                    log.info("---LogSocketServer---service---send finish!---");
                    sckclient.shutdownOutput();
                    return;
                }
            } catch (IOException e) {
                // TODO 自动生成的 catch 块
                log.info("---LogSocketServer---service---IOException!!---");
                e.printStackTrace();
            }

        }
    }
}
