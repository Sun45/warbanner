package cn.sun45.warbanner.framework.document.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Sun45 on 2019/10/28.
 * 数据库表参数配置注释
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DbTableParamConfigure {
    //是关键参数
    boolean iskeyparm() default false;

    //是字节数组
    boolean isbytearray() default false;
}
