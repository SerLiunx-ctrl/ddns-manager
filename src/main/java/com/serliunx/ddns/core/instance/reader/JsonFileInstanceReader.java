package com.serliunx.ddns.core.instance.reader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.serliunx.ddns.core.constant.InstanceType;
import com.serliunx.ddns.core.constant.SystemConstants;
import com.serliunx.ddns.core.instance.Instance;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 实例信息加载: Json文件
 * @author SerLiunx
 * @since 1.0
 */
public class JsonFileInstanceReader extends AbstractFileInstanceReader{

    private final JsonMapper jsonMapper;

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
                return jsonMapper.treeToValue(root, instanceType.getClazz());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toCollection(HashSet::new));
    }
}
