package com.opencloud.device.socket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.opencloud.device.form.req.DeviceInfoRes;
import com.opencloud.device.model.DeviceRedisKey;
import com.opencloud.device.properties.DeviceManageProperties;
import com.opencloud.device.service.IotDeviceService;
import com.opencloud.device.service.impl.FileStorageServiceImpl;
import com.opencloud.device.utils.RedisUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
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
@Data
public class SocketTraceServer extends Thread {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private DeviceManageProperties ossProperties;

    @Autowired
    private FileStorageServiceImpl storageService;

    @Autowired
    private IotDeviceService iotDeviceService;

    public void startaccept() {
        log.info("---LogSocketServer---start---");
        try {
            ServerSocket ss = new ServerSocket(ossProperties.getTrace().getPort());
            while (true) {
                log.info("---LogSocketServer---accept port---" + ossProperties.getTrace().getPort());
                Socket s = ss.accept();
                sendMegSize = 0;
                sengMsgCount = 1;
                log.info("---LogSocketServer---service---start---");
                new Service(s).start();//创建线程
            }
        } catch (IOException e) {
            log.info("设备升级socket中断！");
        }
    }

    private int sendMegSize = 0;

    private int sengMsgCount = 1;

    @Override
    public void run() {
        startaccept();
    }

    class Service extends Thread {
        private Socket sckclient = null;
        private OutputStream bsend = null;
        private BufferedInputStream blocal = null;
        private BufferedInputStream brecv = null;
        // 每次最长发送1M
        private static final int SERVER_MAX_SEND_SIZE = 102400000;

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
                    // 没有发具体请求过来断开连接
                    if (brecv.read(bgetbuf) == -1) {
                        log.info("---LogSocketServer---service---no request fileinfo detail--close socket!!---");
                        sckclient.shutdownOutput();
                        return;
                    }
                    String strGet = new String(bgetbuf);
                    log.info("---LogSocketServer---service---strGet:" + strGet);
                    //判断格式是否符合，不符合JSON断开连接
                    if (strGet.indexOf("DEVICE-REQUEST-OTA-FILE") != -1) {
                        log.info("---LogSocketServer---service---DEVICE-REQUEST-OTA-FILE---");
                    } else {
                        log.info("---LogSocketServer---service---invalid connect，close socket!!---");
                        sckclient.shutdownOutput();
                        return;
                    }
                    String strFullname = "test.zip";
                    String deviceSn = "";
                    int iPos = 0;
                    int iBlocksize = 0;
                    JSONObject object = JSON.parseObject(strGet); //用json格式的字符串获取一个JSONObject对象
                    String strDeviceSn = object.getString("deviceid");
                    String strFileName = object.getString("File-Name");
                    String strStartPos = object.getString("Start-Pos");
                    String strBlockSize = object.getString("Block-Size");
                    log.info("---LogSocketServer--strFilePath:--:" + strDeviceSn);
                    log.info("---LogSocketServer--strFileName:--:" + strFileName);
                    log.info("---LogSocketServer--strStartPos:--:" + strStartPos);
                    log.info("---LogSocketServer--strBlockSize:--:" + strBlockSize);
                    strFullname = strFileName;
                    iPos = Integer.valueOf(strStartPos);
                    iBlocksize = Integer.valueOf(strBlockSize);
                    deviceSn = strDeviceSn;
                    //*/

                    log.info("---LogSocketServer---service---strFullname:---" + strFullname);
                    // 创建并关联发送流
                    bsend = sckclient.getOutputStream();
                    //根据请求信息，创建一个相应的BufferedInputStream对象读取本地文件
                    Resource resource = storageService.loadAsResource(strFullname);
                    blocal = new BufferedInputStream(resource.getInputStream());

                    int sendSize = SERVER_MAX_SEND_SIZE;//记录发送大小
                    if (SERVER_MAX_SEND_SIZE >= iBlocksize) {
                        sendSize = iBlocksize;
                    }
                    //读取
                    byte[] bsendbuf = new byte[sendSize];
                    //达到请求的字节数或者文件末尾结束发送
                    int iTotalsize = 0;
                    int len = 0;
                    if (sendMegSize == 0) {
                        //上传日志下载进度
//                        log.info("发送消息分片最后一次");
//                        mqttMessageService.sendDeviceTop(deviceSn, strFullname, resource.getFile().length(), sendMegSize);
                        // 第一次传进度
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("deviceSn", deviceSn);
                        jsonObject.put("fileCountSize", resource.getFile().length());
                        jsonObject.put("fileReadSize", sendMegSize);
                        String key = DeviceRedisKey.DEVICE_SOCKET_TRACE + deviceSn;
                        redisUtil.set(key, jsonObject.toJSONString(), 10, TimeUnit.MINUTES);
                    }
                    log.info("---LogSocketServer---service---send start--iPos:-" + iPos + "--iBlocksize:--" + iBlocksize);
                    blocal.skip(iPos);

                    while ((len = blocal.read(bsendbuf, 0, sendSize)) != -1) {
                        //bsend.write(getToSendData(bsendbuf,0,len));
                        bsend.write(bsendbuf, 0, len);
                        bsend.flush();
                        iTotalsize = iTotalsize + len;
                        log.info("---LogSocketServer---service---iTotalsize:---" + iTotalsize);
                        //只发送请求的长度，客户端支持断点续传，通常是分块请求的
                        if (iTotalsize >= iBlocksize) {
                            log.info("---LogSocketServer---service---iBlocksize >= iBlocksize---");
                            blocal.close();
                            log.info("---LogSocketServer---service---send finish!---");
                            break;
                        }
                    }
                    sendMegSize = sendMegSize + iTotalsize;
                    log.info("累计总长度" + sendMegSize);

                    //上传日志下载进度
                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put("deviceSn", deviceSn);
                    jsonObject1.put("fileCountSize", resource.getFile().length());
                    jsonObject1.put("fileReadSize", sendMegSize);
                    String key = DeviceRedisKey.DEVICE_SOCKET_TRACE + deviceSn;
                    redisUtil.set(key, jsonObject1.toJSONString(), 5, TimeUnit.MINUTES);
                    log.info("第" + sengMsgCount + "次");
                    log.info(String.format("发送消息分大片片片发消息%s", sendMegSize));
                    sengMsgCount++;

                    // 最后一次
                    if (sendMegSize >= resource.getFile().length()) {
                        log.info("发送消息分片最后一次");
                        DeviceInfoRes deviceInfoRes1 = new DeviceInfoRes();
//                        deviceInfoRes1.setTaskId(id);
                        deviceInfoRes1.setTaskStatus(4);
                        deviceInfoRes1.setTaskCode("2001");
                        deviceInfoRes1.setDeviceSn(deviceSn);
                        deviceInfoRes1.setUri(strFullname);
                        deviceInfoRes1.setTotalSize(resource.getFile().length()+"");
                        deviceInfoRes1.setCompletedSize(sendMegSize+"");
                        iotDeviceService.getDeviceSoftToCloud(deviceInfoRes1);
//                        mqttMessageService.sendDeviceTop(deviceSn, strFullname, resource.getFile().length(), sendMegSize);
                    }
                }
            } catch (IOException e) {
                // TODO 自动生成的 catch 块
                log.info("---LogSocketServer---service---IOException!!---");
                e.printStackTrace();
            }

        }
    }
}
