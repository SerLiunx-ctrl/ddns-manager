package com.serliunx.ddns.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * 反射相关工具类
 * @author SerLiunx
 * @since 1.0
 */
@Slf4j
public final class ReflectionUtils {

    private ReflectionUtils(){throw new UnsupportedOperationException();}

    /**
     * 获取当前类声明的所有字段
     * <li> 包括父类
     * @param clazz 类对象
     * @param setAccessible 是否将字段的可访问性
     * @return 字段列表
     */
    public static Field[] getDeclaredFields(Class<?> clazz, boolean setAccessible){
        if(clazz == null){
            return null;
        }
        Field[] declaredFields = clazz.getDeclaredFields();
        Field[] declaredFieldsInSuper = getDeclaredFields(clazz.getSuperclass(), setAccessible);
        if(declaredFieldsInSuper != null){
            Field[] newFields = new Field[declaredFields.length + declaredFieldsInSuper.length];
            System.arraycopy(declaredFields, 0, newFields, 0, declaredFields.length);
            System.arraycopy(declaredFieldsInSuper, 0, newFields, declaredFields.length, declaredFieldsInSuper.length);
            declaredFields = newFields;
        }
        if(setAccessible){
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(true);
            }
        }
        return declaredFields;
    }

    /**
     * 获取当前类声明的所有字段
     * <li> 包括父类
     * @param clazz 类对象
     * @param setAccessible 是否将字段的可访问性
     * @return 字段列表
     */
    public static List<Field> getDeclaredFieldList(Class<?> clazz, boolean setAccessible){
        return Arrays.asList(getDeclaredFields(clazz, setAccessible));
    }

    /**
     * 复制两个对象的同名属性
     * @param src 源对象
     * @param dest 目标对象
     * @param onlyNull 是否仅复制源对象不为空的属性
     */
    public static void copyField(Object src, Object dest,boolean onlyNull){
        Class<?> srcClass = src.getClass();
        Class<?> destClass = dest.getClass();
        List<Field> srcField = getDeclaredFieldList(srcClass, true);
        List<Field> destField = getDeclaredFieldList(destClass, true);
        for (Field field : destField) {
            if(onlyNull){
                try {
                    if(field.get(dest) != null){
                        continue;
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            for (Field sf : srcField) {
                if(sf.getName().equals(field.getName())){
                    try {
                        field.set(dest, sf.get(src));
                    }catch (Exception e){
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}
