package com.serliunx.ddns.core.instance.sql.support.annotation;

import com.baomidou.mybatisplus.annotation.TableField;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指定为对象的额外参数
 * @author SerLiunx
 * @since 1.0
 */
@Deprecated
@TableField(exist = false)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExpandParam {

    /**
     * 额外参数名
     * <li> 不指定参数名时代表对象中的属性名与额外参数中的参数名相等 => {@link String#equals(Object)}
     */
    String value() default "";
}
