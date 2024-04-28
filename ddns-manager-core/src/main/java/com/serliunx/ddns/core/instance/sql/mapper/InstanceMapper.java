package com.serliunx.ddns.core.instance.sql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.serliunx.ddns.core.instance.sql.entity.InstanceSqlEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 实例对象映射
 * @author SerLiunx
 * @since 1.0
 */
@Mapper
@Deprecated
public interface InstanceMapper extends BaseMapper<InstanceSqlEntity> {

}
