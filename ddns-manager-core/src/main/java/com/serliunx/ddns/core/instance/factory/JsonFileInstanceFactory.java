package com.serliunx.ddns.core.instance.factory;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.serliunx.ddns.api.instance.Instance;
import com.serliunx.ddns.api.instance.InstanceSource;

/**
 * Jackson-Json文件实例工厂
 * @author SerLiunx
 * @since 1.0
 */
public class JsonFileInstanceFactory extends JacksonFileInstanceFactory{

    public JsonFileInstanceFactory(String instanceDir, JsonMapper jsonMapper) {
        super(instanceDir, jsonMapper);
    }

    public JsonFileInstanceFactory(String instanceDir) {
        this(instanceDir, new JsonMapper());
    }

    @Override
    protected String[] fileSuffix() {
        return new String[]{".json"};
    }

    @Override
    protected Instance post(Instance instance) {
        instance.setInstanceSource(InstanceSource.FILE_JSON);
        return instance;
    }
}
