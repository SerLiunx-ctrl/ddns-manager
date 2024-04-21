package com.serliunx.ddns.core.instance.reader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.serliunx.ddns.api.instance.InstanceType;
import com.serliunx.ddns.api.constant.SystemConstants;
import com.serliunx.ddns.api.instance.Instance;
import com.serliunx.ddns.core.instance.AliyunInstance;
import com.serliunx.ddns.core.instance.DefaultInstance;
import com.serliunx.ddns.core.instance.TencentInstance;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 实例信息加载: Json文件
 * @author SerLiunx
 * @since 1.0
 */
public class JsonFileInstanceReader extends AbstractFileInstanceReader{

    private final JsonMapper jsonMapper;
    private static final Map<InstanceType, Class<? extends Instance>> instanceTypeMap;

    static {
        instanceTypeMap = new HashMap<InstanceType, Class<? extends Instance>>(){
            {
                put(InstanceType.ALI_YUN, AliyunInstance.class);
                put(InstanceType.INHERITED, DefaultInstance.class);
                put(InstanceType.TENCENT_CLOUD, TencentInstance.class);
            }
        };
    }

    public JsonFileInstanceReader(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    public JsonFileInstanceReader() {
        this(new JsonMapper());
    }

    @Override
    protected Set<Instance> preLoad(Collection<File> files) {
        return files.stream().map(f -> {
            try {
                JsonNode root = jsonMapper.readTree(f);
                String rootName = root.get(SystemConstants.TYPE_FIELD).asText(); //根据类型去装配实例信息
                InstanceType instanceType = InstanceType.valueOf(rootName);
                return jsonMapper.treeToValue(root, matchInstanceType(instanceType));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toCollection(HashSet::new));
    }

    private Class<? extends Instance> matchInstanceType(InstanceType instanceType) {
        return instanceTypeMap.get(instanceType);
    }
}
