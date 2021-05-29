package cn.sun45.warbanner.framework.document.preference;

import android.content.SharedPreferences;
import android.text.TextUtils;

import java.lang.reflect.Field;

/**
 * Created by Sun45 on 2019/10/28.
 * SharedPreferences基础类
 */
public abstract class BasePreference {

    public abstract String getName();

    /**
     * 获取Preference参数信息
     *
     * @param key
     * @return
     */
    public Field getField(String key) {
        Field[] declaredFields = getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.getName().equals(key)) {
                return field;
            }
        }
        throw new IllegalStateException("class \"" + getClass().getName() + "\" without field \"" + key);
    }

    /**
     * 注册监听
     *
     * @param listener
     */
    public void registListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        PreferenceManager.registListener(listener, getName());
    }

    /**
     * 数据读取
     *
     * @param key
     * @param <T>
     * @return
     */
    protected <T> T load(String key) {
        if (TextUtils.isEmpty("key")) {
            throw new IllegalStateException("load empty key from \"" + getClass().getName() + "\"");
        }
        return PreferenceManager.load(getClass(), key);
    }

    /**
     * 数据存储
     *
     * @param key
     * @param value
     */
    protected void save(String key, Object value) {
        if (TextUtils.isEmpty("key")) {
            throw new IllegalStateException("save empty key to \"" + getClass().getName() + "\"");
        }
        PreferenceManager.save(getClass(), key, value);
    }
}
