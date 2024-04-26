package com.serliunx.ddns.core.instance.factory;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.serliunx.ddns.api.instance.Instance;

import java.io.File;

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
        return jacksonFileLoad(jsonMapper, file);
    }

    @Override
    protected String[] fileSuffix() {
        return new String[]{".json"};
    }
}
