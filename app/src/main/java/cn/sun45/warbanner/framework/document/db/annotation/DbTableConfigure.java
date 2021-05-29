package cn.sun45.warbanner.framework.document.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Sun45 on 2019/10/28.
 * 数据库表配置注释
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface DbTableConfigure {
    //表名
    String tablename() default "";
}
