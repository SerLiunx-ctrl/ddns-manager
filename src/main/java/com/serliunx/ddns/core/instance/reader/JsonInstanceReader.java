package com.serliunx.ddns.core.instance.reader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.serliunx.ddns.constant.InstanceType;
import com.serliunx.ddns.constant.SystemConstants;
import com.serliunx.ddns.core.Instance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author SerLiunx
 * @since 1.0
 */
@Slf4j
@Component
public class JsonInstanceReader extends AbstractInstanceReader {

    private static final JsonMapper jsonMapper = new JsonMapper();

    @Override
    @SuppressWarnings("all")
    public Instance read(File file) {
        try {
            JsonNode typeNode = jsonMapper.readTree(file);
            String root = typeNode.get(SystemConstants.TYPE_FIELD).asText();
            InstanceType instanceType = InstanceType.valueOf(root);
            return jsonMapper.treeToValue(typeNode, instanceType.getClazz());
        } catch (Exception e) {
            log.error("文件 {} 读取失败! => {}", file.getName(), e.getMessage());
        }
        return null;
    }
}
