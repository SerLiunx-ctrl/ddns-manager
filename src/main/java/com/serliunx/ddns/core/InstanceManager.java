package com.serliunx.ddns.core;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.serliunx.ddns.core.instance.reader.JsonInstanceReader;
import com.serliunx.ddns.core.instance.reader.XmlInstanceReader;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.serliunx.ddns.constant.SystemConstants.*;

/**
 * @author SerLiunx
 * @since 1.0
 */
@Slf4j
@Getter
@Component
public final class InstanceManager {

    private static final XmlMapper xmlMapper = new XmlMapper();
    private static final JsonMapper jsonMapper = new JsonMapper();

    /**
     * 从JSON文件中读取实例
     */
    private final JsonInstanceReader jsonInstanceReader;
    /**
     * 从XML文件中读取实例
     */
    private final XmlInstanceReader xmlInstanceReader;

    /**
     * 保存实例
     */
    private final Map<String, Instance> instanceMap = new ConcurrentHashMap<>();
    /**
     * 已保存的实例数量
     */
    private int instanceCount;

    public InstanceManager(JsonInstanceReader jsonInstanceReader, XmlInstanceReader xmlInstanceReader) {
        this.jsonInstanceReader = jsonInstanceReader;
        this.xmlInstanceReader = xmlInstanceReader;
    }

    public InstanceLoadContext load(){
        // 从instances文件中读取实例文件信息
        Map<String, Set<File>> instanceFiles = readFromInstancesFolder();
        if(instanceFiles == null || instanceFiles.isEmpty()){
            log.info("未读取到任何实例信息...");
            return null;
        }
        // 读取实例
        readInstance(instanceFiles);

        return new InstanceLoadContext(instanceCount, new HashSet<>(instanceMap.values()));
    }

    public Instance getInstance(String instanceName){
        return instanceMap.get(instanceName);
    }

    private void readInstance(Map<String, Set<File>> instances){
        //读取JSON实例
        readJsonInstance(instances.get(JSON_FILE));

        //读取XML实例
        readXmlInstance(instances.get(XML_FILE));
    }

    private void readJsonInstance(Set<File> files){
        files.forEach(f -> {
            Instance instance = jsonInstanceReader.read(f);
            instanceMap.put(instance.getName(), instance);
            instanceCount++;
        });
    }

    private void readXmlInstance(Set<File> files){

    }

    private Map<String, Set<File>> readFromInstancesFolder(){
        String path = USER_DIR + File.separator + INSTANCE_FOLDER_NAME;
        File file = new File(path);
        boolean isSuccess = true;
        // 不存在则创建该文件夹
        if(!file.exists()){
            boolean mkdir = file.mkdir();
            if(!mkdir){
                log.error("实例文件夹创建失败, 请检查是否有权限创建文件夹! 服务已终止.");
                isSuccess = false;
            }else {
                log.debug("成功创建实例文件夹 -> {}", INSTANCE_FOLDER_NAME);
            }
        }
        if(!isSuccess){
            return null;
        }
        //创建的文件夹 最后竟然不是文件夹吗？ 这不合理
        if(!file.isDirectory()){
            return null;
        }

        return generateResult(file);
    }

    private Map<String, Set<File>> generateResult(File file) {
        Map<String, Set<File>> result = new HashMap<>();
        File[] instanceFiles = file.listFiles();
        if(instanceFiles != null){
            Set<File> jsonFiles = new HashSet<>();
            Set<File> xmlFiles = new HashSet<>();
            for (File instanceFile : instanceFiles) {
                if(!instanceFile.isFile()){
                    continue;
                }
                if(instanceFile.getName().endsWith(JSON_FILE)){
                    jsonFiles.add(instanceFile);
                    continue;
                }
                if(instanceFile.getName().endsWith(XML_FILE)){
                    xmlFiles.add(instanceFile);
                }
            }
            result.put(JSON_FILE, jsonFiles);
            result.put(XML_FILE, xmlFiles);
        }
        return result;
    }


}
