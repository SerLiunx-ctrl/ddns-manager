package com.serliunx.ddns.core.instance.factory;

import com.serliunx.ddns.api.instance.Instance;
import com.serliunx.ddns.core.instance.sql.entity.InstanceSqlEntity;
import com.serliunx.ddns.core.instance.sql.service.InstanceService;

import java.util.List;
import java.util.Set;

/**
 * @author SerLiunx
 * @since 1.0
 */
public class DatabaseInstanceFactory extends AbstractInstanceFactory{

    private final InstanceService instanceService;

    public DatabaseInstanceFactory(InstanceService instanceService) {
        this.instanceService = instanceService;
    }

    @Override
    protected Set<Instance> load() {
        List<InstanceSqlEntity> instancesInSql = instanceService.getAllInstances();
        return null;
    }
}
