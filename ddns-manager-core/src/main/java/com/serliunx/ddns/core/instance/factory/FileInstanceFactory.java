package com.serliunx.ddns.core.instance.factory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serliunx.ddns.api.constant.SystemConstants;
import com.serliunx.ddns.api.instance.Instance;
import com.serliunx.ddns.api.instance.InstanceType;

import java.io.File;
import java.io.FileFilter;
import java.util.*;
import java.util.stream.Collectors;

import static com.serliunx.ddns.core.InstanceTypes.match;

/**
 * 加载已文件形式存储的实例信息
 * @see JsonFileInstanceFactory
 * @author SerLiunx
 * @since 1.0
 */
public abstract class FileInstanceFactory extends AbstractInstanceFactory{

    protected String instanceDir;

    public FileInstanceFactory(String instanceDir) {
        this.instanceDir = instanceDir;
    }

    @Override
    protected Set<Instance> load() {
        Set<File> files = loadFiles();
        if(files != null && !files.isEmpty()){
            return files.stream()
                    .map(this::loadInstance)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toCollection(HashSet::new));
        }
        return Collections.emptySet();
    }

    /**
     * 交由具体的子类去加载实例, 比如: json格式的实例信息、xml格式的实例信息
     * @param file 文件信息
     * @return 实例
     */
    protected abstract Instance loadInstance(File file);

    /**
     * 子类要设置自己可以加载的文件后缀名
     * <li> 后缀名仅仅是一个标记符, 文件不一定要有后缀名哦
     * @return 文件后缀名
     */
    protected abstract String[] fileSuffix();

    private Set<File> loadFiles(){
        FileFilter fileFilter = new InnerFileFilter(fileSuffix());
        File pathFile = new File(instanceDir);
        if(!pathFile.exists()){
            boolean result = pathFile.mkdirs();
            if(!result){
                throw new IllegalArgumentException("create path failed");
            }
        }
        if(!pathFile.isDirectory()){
            throw new IllegalArgumentException("path is not a directory");
        }
        File[] files = pathFile.listFiles(fileFilter);
        if(files == null || files.length == 0){
            return Collections.emptySet();
        }
        return Arrays.stream(files).collect(Collectors.toCollection(HashSet::new));
    }

    /**
     * 仅Jackson适用
     */
    protected Instance jacksonFileLoad(ObjectMapper objectMapper, File file){
        try {
            JsonNode root = objectMapper.readTree(file);
            String rootName = root.get(SystemConstants.TYPE_FIELD).asText(); //根据类型去装配实例信息
            InstanceType instanceType = InstanceType.valueOf(rootName);
            return objectMapper.treeToValue(root, match(instanceType));
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private static class InnerFileFilter implements FileFilter {
        private final String[] fileSuffix;

        public InnerFileFilter(String[] fileSuffix) {
            this.fileSuffix = fileSuffix;
        }

        @Override
        public boolean accept(File pathname) {
            if(!pathname.isFile())
                return false;
            for (String suffix : fileSuffix) {
                if(pathname.getName().endsWith(suffix)){
                    return true;
                }
            }
            return false;
        }
    }
}
