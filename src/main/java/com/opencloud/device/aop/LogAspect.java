package com.opencloud.device.aop;

import com.opencloud.device.annotation.OperationLog;
import com.opencloud.device.entity.SystemAccessLogs;
import com.opencloud.device.security.BaseUserDetails;
import com.opencloud.device.security.SecurityHelper;
import com.opencloud.device.service.SystemAccessLogsService;
import com.opencloud.device.utils.ipUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jihao
 * @version 1.0
 * 声明切面
 * @date 2020-08-10 14:51
 */

@Aspect
@Component
public class LogAspect {

    @Autowired
    private SystemAccessLogsService systemAccessLogsService;

    /**
     * 定义切点 @Pointcut。是面前注解类的地址。
     */
    @Pointcut("@annotation(com.opencloud.device.annotation.OperationLog)")
    public void methodPointCut() {

    }

    @Around(value = "methodPointCut()  && @annotation(operationLog)")
    public Object doBeforeAdvice(ProceedingJoinPoint joinPoint, OperationLog operationLog) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();

//        String url = request.getRequestURL().toString();

        String optionName = operationLog.value();
        String method = request.getMethod();
        /*-------------------------------------通过uri获取aApiId---------------------------*/
        String uri = request.getRequestURI();
//        int num = uri.length() - uri.replace("/","").length();
//        if(num >= 4){
//            //截取/a/b/c三层后面的字符
//            uri = getStr(uri,3);
//        }
//        Integer aApiId = oauthDao.getAApiId(uri);
        /*---------------------------------------------end---------------------------------*/
        String queryString = request.getQueryString();
        Object[] args = joinPoint.getArgs();
        String params = "";

        SystemAccessLogs systemAccessLogs = new SystemAccessLogs();
        try {
//            //获取请求参数集合并进行遍历拼接
//            if (args.length > 0) {
//                if ("POST".equals(method)) {
//                    if (args.length > 1) {
//                        params = args.toString();
//                    } else {
//                        Object object = args[0];
//                        Map map = getKeyAndValue(object);
//                        params = JSON.toJSONString(map);
//                    }
//
//                } else if ("GET".equals(method)) {
//                    params = queryString;
//                    if (params == null) {
//                        params = "";
//                        for (int i = 0; i < args.length; i++) {
//                            params = params + "+" + args[i];
//                        }
//                        params = params.substring(1);
//                    }
//                }
//            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            systemAccessLogs.setMethod(method);
            long beginTime = System.currentTimeMillis();
            BaseUserDetails user = SecurityHelper.getUser();
            if(user!=null){
                systemAccessLogs.setUserId(user.getUserId());
                systemAccessLogs.setUserName(user.getNickName());
            }
            systemAccessLogs.setServerName("device-manage");
            systemAccessLogs.setOptionName(optionName);
            systemAccessLogs.setRequestTime(simpleDateFormat.format(new Date(System.currentTimeMillis())));
//            systemAccessLogs.setAApiId(aApiId);
            systemAccessLogs.setPath(uri);
            String ip = ipUtils.getIPAddress(request);
            systemAccessLogs.setIp(ip);
            systemAccessLogs.setMethod(method);
            systemAccessLogs.setParams(params);
            // result的值就是被拦截方法的返回值
            Object result = joinPoint.proceed();

//            systemAccessLogs.setResult(JSON.toJSONString(result));
            systemAccessLogs.setResponseTime(simpleDateFormat.format(new Date(System.currentTimeMillis())));

            long endTime = System.currentTimeMillis();
            long betweenTime = endTime - beginTime;
            systemAccessLogs.setUseTime(String.valueOf(betweenTime));
            systemAccessLogsService.save(systemAccessLogs);
            return result;
        } catch (Exception e) {
            systemAccessLogs.setError(e.getMessage());
            systemAccessLogsService.save(systemAccessLogs);
            throw new RuntimeException(e.getMessage());
        }
    }

    public static Map<String, Object> getKeyAndValue(Object obj) {
        Map<String, Object> map = new HashMap<>();
        // 得到类对象
        Class userCla = (Class) obj.getClass();
        /* 得到类中的所有属性集合 */
        Field[] fs = userCla.getDeclaredFields();
        for (int i = 0; i < fs.length; i++) {
            Field f = fs[i];
            f.setAccessible(true); // 设置些属性是可以访问的
            Object val = new Object();
            try {
                val = f.get(obj);
                // 得到此属性的值
                map.put(f.getName(), val);// 设置键值
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
    public static String getStr(String str, int n) {
        int i = 0;
        int s = 0;
        while (i++ < n) {
            s = str.indexOf("/", s + 1);
            if (s == -1) {
                return str;
            }
        }
        return str.substring(0, s);
    }
}
