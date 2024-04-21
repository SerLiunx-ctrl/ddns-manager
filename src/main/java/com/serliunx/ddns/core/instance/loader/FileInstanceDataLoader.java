package com.serliunx.ddns.core.instance.loader;

import com.serliunx.ddns.core.constant.InstanceFileType;

import java.io.File;
import java.io.FileFilter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 从指定路径中加载以文件形式存储的实例信息
 * @author SerLiunx
 * @since 1.0
 */
public class FileInstanceDataLoader implements InstanceDataLoader<File, String>{

    private static final Set<String> FILE_SUFFIX_SET = new HashSet<>();
    private static final InnerFileFilter FILE_FILTER = new InnerFileFilter();

    // 读取JSON、XML、YML文件
    static {
        FILE_SUFFIX_SET.add(InstanceFileType.YML.getValue());
        FILE_SUFFIX_SET.add(InstanceFileType.YAML.getValue());
        FILE_SUFFIX_SET.add(InstanceFileType.JSON.getValue());
        FILE_SUFFIX_SET.add(InstanceFileType.XML.getValue());
    }

    @Override
    public Set<File> load(String s) {
        File pathFile = new File(s);
        if(!pathFile.exists()){
            boolean result = pathFile.mkdirs();
            if(!result){
                throw new IllegalArgumentException("create path failed");
            }
        }
        if(!pathFile.isDirectory()){
            throw new IllegalArgumentException("path is not a directory");
        }
        File[] files = pathFile.listFiles(FILE_FILTER);
        return files == null ? Collections.emptySet() : new HashSet<>(java.util.Arrays.asList(files));
    }

    private static class InnerFileFilter implements FileFilter {
        @Override
        public boolean accept(File pathname) {
            if(!pathname.isFile())
                return false;
            for (String suffix : FILE_SUFFIX_SET) {
                if(pathname.getName().endsWith(suffix)){
                    return true;
                }
            }
            return false;
        }
    }
}
