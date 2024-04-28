package com.serliunx.ddns.core.instance.sql.support.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author SerLiunx
 * @since 1.0
 */
@Deprecated
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExpandParamField {

}
