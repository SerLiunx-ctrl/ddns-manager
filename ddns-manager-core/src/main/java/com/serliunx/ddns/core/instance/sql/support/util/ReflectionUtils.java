package com.serliunx.ddns.core.instance.sql.support.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 反射相关工具类
 * @author SerLiunx
 * @since 1.0
 */
@Deprecated
public final class ReflectionUtils {

    private ReflectionUtils(){throw new UnsupportedOperationException();}

    public static Map<Class<?>, List<Field>> fieldsCache = new ConcurrentHashMap<>(16);
    public static Map<Class<?>, Map<String, Field>> namedFieldCache = new ConcurrentHashMap<>(16);

    /**
     * 获取一个类中标记了指定注解的属性
     * <li> 自带缓存
     * @param objectClass 类
     * @param annotationClass 注解类
     * @return 属性集合
     */
    public static List<Field> getFieldsByClassAndAnnotation(Class<?> objectClass,
                                                            Class<? extends Annotation> annotationClass){
        if(fieldsCache.containsKey(objectClass)){
            return fieldsCache.get(objectClass);
        }
        List<Field> fields = Arrays.stream(objectClass.getDeclaredFields())
                .peek(f -> f.setAccessible(true))
                .filter(f -> f.getAnnotation(annotationClass) != null)
                .collect(Collectors.toList());
        fieldsCache.put(objectClass, fields);
        return fields;
    }

    /**
     * 获取指定类中指定名称的属性
     * <li> 自带缓存
     * @param objectClass 类
     * @param fieldName 属性名
     * @return 属性
     */
    public static Field getFieldByClassAndName(Class<?> objectClass, String fieldName){
        if(namedFieldCache.containsKey(objectClass)){
            Map<String, Field> cache = namedFieldCache.get(objectClass);
            if(cache.containsKey(fieldName)){
                return cache.get(fieldName);
            }
        }

        Field field = Arrays.stream(objectClass.getDeclaredFields())
                .filter(f -> f.getName().equals(fieldName))
                .findAny()
                .orElse(null);
        if(field != null){
            field.setAccessible(true);
        }
        //设置缓存
        if(namedFieldCache.containsKey(objectClass)){
            Map<String, Field> cacheOwned = namedFieldCache.get(objectClass);
            cacheOwned.put(fieldName, field);
        }else{
            Map<String, Field> fieldMap = new ConcurrentHashMap<>(16);
            fieldMap.put(fieldName, field);
            namedFieldCache.put(objectClass, fieldMap);
        }
        return field;
    }
}

