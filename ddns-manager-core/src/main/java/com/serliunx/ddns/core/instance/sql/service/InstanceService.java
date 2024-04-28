package com.serliunx.ddns.core.instance.sql.service;

import com.serliunx.ddns.core.instance.sql.entity.InstanceSqlEntity;

import java.util.List;

/**
 * 实例相关操作
 * @author SerLiunx
 * @since 1.0
 */
@Deprecated
public interface InstanceService {

    /**
     * 获取数据库中所有实例
     * <li> 仅用于加载实例数据, 还会进一步加工
     * @return 实例数据
     */
    List<InstanceSqlEntity> getAllInstances();
}
