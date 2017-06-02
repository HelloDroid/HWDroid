package com.hw.hwdroid.foundation.utils.reflect;

import com.hw.hwdroid.foundation.utils.StringUtils;

public class FieldUtils {

    /**
     * 暴力获取name字段的Value
     *
     * @param bean
     * @param name 属性
     * @return
     */
    public static Object getFieldValue(Object bean, String name) {
        Object o = null;
        if (bean == null || StringUtils.isNullOrWhiteSpace(name)) return o;
        try {
            // 暴力获取bean中name字段的Value
            return BSBeanUtils.forceGetProperty(bean, name);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 设置name字段的Value
     *
     * @param bean
     * @param propertyName 属性
     * @param value
     * @return
     */
    public static Object setFieldValue(Object bean, String propertyName, Object value) {
        try {
            BSBeanUtils.forceSetProperty(bean, propertyName, value);
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param clazz
     * @param name
     * @return
     */
    private static String getFields(Class clazz, String name) {
        try {
            return (String) getFieldValue(clazz.newInstance(), name);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param propertyName
     * @return
     */
    private static String getGetterName(String propertyName) {
        String method = "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
        return method;
    }

    /**
     * @param propertyName
     * @return
     */
    private static String getSetterName(String propertyName) {
        String method = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
        return method;
    }

}
