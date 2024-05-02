package com.serliunx.ddns.core.instance.factory;

import com.serliunx.ddns.api.instance.Instance;
import com.serliunx.ddns.api.instance.InstanceSource;
import com.serliunx.ddns.api.instance.InstanceType;
import com.serliunx.ddns.core.InstanceTypes;
import com.serliunx.ddns.util.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

import static com.serliunx.ddns.api.constant.SystemConstants.TYPE_FIELD;

/**
 * Yaml文件实例工厂
 * @author SerLiunx
 * @since 1.0
 */
@Slf4j
public class YamlFileInstanceFactory extends FileInstanceFactory{

    public YamlFileInstanceFactory(String instanceDir) {
        super(instanceDir);
    }

    @Override
    protected Instance loadInstance(File file) {
        FileInputStream instanceInputStream = null;
        try {
            instanceInputStream = new FileInputStream(file);
            Yaml yaml = new Yaml();
            Map<String, Object> valueMap = yaml.load(instanceInputStream);
            InstanceType type = null;
            if(valueMap.get(TYPE_FIELD) != null){
                type = InstanceType.valueOf((String) valueMap.get(TYPE_FIELD));
            }
            if(type == null){
                log.error("文件 {} 读取失败, 可能是缺少关键参数.", file.getName());
                return null;
            }
            Class<? extends Instance> clazz = InstanceTypes.match(type);
            if(clazz != null){
                Constructor<? extends Instance> constructor = clazz.getConstructor();
                Instance instance = buildInstance(constructor.newInstance(), valueMap);
                instance.setInstanceSource(InstanceSource.FILE_YML);
                return instance;
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if(instanceInputStream != null){
                    instanceInputStream.close();
                }
            } catch (IOException e) {
                log.error("文件读取出现异常.");
            }
        }
    }

    @Override
    protected String[] fileSuffix() {
        return new String[]{".yml", ".yaml"};
    }

    @SuppressWarnings(value = {"unchecked", "rawtypes"})
    protected Instance buildInstance(Instance instance, Map<String, Object> valueMap){
        Field[] declaredFields = ReflectionUtils.getDeclaredFields(instance.getClass(), true);
        for (Field f : declaredFields) {
            if(Modifier.isStatic(f.getModifiers())){
                continue;
            }
            Object value = valueMap.get(f.getName());
            f.setAccessible(true);
            try {
                //设置枚举类
                Class<?> clazz = f.getType();
                if(clazz.isEnum() && value != null){
                    f.set(instance, Enum.valueOf((Class<? extends Enum>) clazz, (String) value));
                    continue;
                }
                f.set(instance, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            f.setAccessible(false);
        }
        return instance;
    }
}
