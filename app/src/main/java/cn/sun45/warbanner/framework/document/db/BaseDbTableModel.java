package cn.sun45.warbanner.framework.document.db;

import android.net.Uri;

import java.io.Serializable;
import java.lang.reflect.Field;

import cn.sun45.warbanner.framework.document.db.annotation.DbTableConfigure;
import cn.sun45.warbanner.framework.document.db.annotation.DbTableParamConfigure;

/**
 * Created by Sun45 on 2019/10/28.
 * 数据库表模型基础类
 */
public abstract class BaseDbTableModel implements Serializable {
    protected abstract Class getProviderClass();

    /**
     * 获取表地址uri
     *
     * @return 表地址Uri
     */
    public Uri getUri() {
        return Uri.parse("content://" + getProviderClass().getName() + "/" + getClass().getAnnotation(DbTableConfigure.class).tablename());
    }

    /**
     * 表内容打印
     *
     * @return
     */
    @Override
    public String toString() {
        Class c = getClass();
        String str = c.getSimpleName() + "{";
        Field[] declaredFields = c.getDeclaredFields();
        boolean first = true;
        for (Field field : declaredFields) {
            if (field.getAnnotation(DbTableParamConfigure.class) != null) {
                String s = "";
                if (first) {
                    first = false;
                } else {
                    s += ", ";
                }
                String name = field.getName();
                s += name + "=[";
                field.setAccessible(true);
                try {
                    s += field.get(this);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                s += "]";
                str += s;
            }
        }
        str += "}";
        return str;
    }
}
