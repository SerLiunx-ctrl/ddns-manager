package com.serliunx.ddns.core.instance.sql.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.serliunx.ddns.core.instance.sql.entity.InstanceSqlEntity;
import com.serliunx.ddns.core.instance.sql.mapper.InstanceMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 实例相关操作实现
 * @author SerLiunx
 * @since 1.0
 */
@Deprecated
@Service
public class InstanceServiceImpl implements InstanceService{

    private final InstanceMapper instanceMapper;

    public InstanceServiceImpl(InstanceMapper instanceMapper) {
        this.instanceMapper = instanceMapper;
    }

    @Override
    public List<InstanceSqlEntity> getAllInstances(){
        return instanceMapper.selectList(new LambdaQueryWrapper<InstanceSqlEntity>()
                .gt(InstanceSqlEntity::getId, 0)
        );
    }
}
