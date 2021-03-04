package com.org.devg.ding.platform.utils;

import java.util.Map;

public class CommonUtil {

    public static String getMapString(Map<String, Object> map, String string) {
        return map == null ? null : map.get(string) == null ? null : String.valueOf(map.get(string));
    }

    public static String getMapString(Map<String, Object> map, String string, String defaultValue) {
        return map == null ? defaultValue : map.get(string) == null ? defaultValue : String.valueOf(map.get(string));
    }

    /**
     * 判断是否为null或空字符串。如果不为null，在判断是否为空字符串之前会调用trim()。
     *
     * @param str
     * @return
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().equals("");
    }

    /**
     * 判断是否为null或空字符串。如果不为null，在判断是否为空字符串之前会调用trim()。
     *
     * @param object
     * @return
     */
    public static boolean isNullOrEmpty(Object object) {

        if (object == null) {
            return true;
        }
        return isNullOrEmpty(object.toString());
    }
}
