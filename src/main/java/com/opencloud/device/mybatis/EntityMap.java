package com.opencloud.device.mybatis;


import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自定义Map
 * @author LYD
 */
public class EntityMap extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;
    public static EnumConvertInterceptor interceptors = null;
   // private RedisUtil redisUtils = SpringContextHolder.getBean("redisUtils");
    //private Map<Object, Object> dataMaps = redisUtils.getMap("DICTDATA_MAPS");

    public EntityMap() {

    }

    private static String getField(String field) {
        String str = "";
        int s = field.indexOf(".");
        if (s > -1) {
            str = field.substring(s + 1);
        } else {
            str = field;
        }
        return str;
    }

    public static void setEnumConvertInterceptor(EnumConvertInterceptor interceptor) {
        interceptors = interceptor;
    }


    public EntityMap put(String key, Object value) {
        if (ObjectUtils.isNotEmpty(interceptors)) {
            interceptors.convert(this, key, value);
        }
        key = lineToHump(key);
        if (ObjectUtils.isNotNull(value)) {
            super.put(key, value);
        } else {
            super.put(key, "");
        }
        return this;
    }

    private static Pattern linePattern = Pattern.compile("_(\\w)");

    /** 下划线转驼峰 */
    public static String lineToHump(String str) {
        if(str.indexOf("_")>0) {
            str = str.toLowerCase();
            Matcher matcher = linePattern.matcher(str);
            StringBuffer sb = new StringBuffer();
            while (matcher.find()) {
                matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
            }
            matcher.appendTail(sb);
            return sb.toString();
        }else{
            return str;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        T t = null;
        Object obj = super.get(key);
        if (ObjectUtils.isNotEmpty(obj)) {
            t = (T) obj;
        }
        return t;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, T def) {
        Object obj = super.get(key);
        if (ObjectUtils.isNotEmpty(obj)) {
            return (T) obj;
        } else {
            return def;
        }
    }

}
