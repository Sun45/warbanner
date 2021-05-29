package cn.sun45.warbanner.framework.document.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Sun45 on 2019/10/28.
 * 数据提供器配置注释
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface ProviderConfigure {
    //版本号
    int version() default 1;

    //库名
    String dbname() default "";

    //空字符串
    String nullstring() default "";
}
