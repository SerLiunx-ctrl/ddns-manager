package com.serliunx.ddns.core.instance.factory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.serliunx.ddns.api.constant.SystemConstants;
import com.serliunx.ddns.api.instance.Instance;
import com.serliunx.ddns.api.instance.InstanceType;

import java.io.File;

import static com.serliunx.ddns.core.InstanceTypes.match;

/**
 * Json文件实例工厂
 * @author SerLiunx
 * @since 1.0
 */
public class JsonFileInstanceFactory extends FileInstanceFactory{

    private final JsonMapper jsonMapper;

    public JsonFileInstanceFactory(String instanceDir, JsonMapper jsonMapper) {
        super(instanceDir);
        this.jsonMapper = jsonMapper;
    }

    public JsonFileInstanceFactory(String instanceDir) {
        this(instanceDir, new JsonMapper());
    }

    @Override
    protected Instance loadInstance(File file) {
        try {
            JsonNode root = jsonMapper.readTree(file);
            String rootName = root.get(SystemConstants.TYPE_FIELD).asText(); //根据类型去装配实例信息
            InstanceType instanceType = InstanceType.valueOf(rootName);
            return jsonMapper.treeToValue(root, match(instanceType));
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    protected String[] fileSuffix() {
        return new String[]{".json"};
    }
}
