package cn.sun45.warbanner.framework.document.preference;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.Field;

import cn.sun45.warbanner.framework.MyApplication;

/**
 * Created by Sun45 on 2019/10/28.
 * SharedPreferences管理类
 */
public class PreferenceManager {
    /**
     * 数据读取
     *
     * @param clazz
     * @param key
     * @param <T>
     * @param <P>
     * @return
     */
    public static <T, P extends BasePreference> T load(Class<P> clazz, String key) {
        try {
//            Constructor c = clazz.getDeclaredConstructor(Context.class);
//            P preference = (P) c.newInstance();
            P preference = clazz.newInstance();
            String name = preference.getName();
            Field field = preference.getField(key);
            Class type = field.getType();
            field.setAccessible(true);
            Object defValue = field.get(preference);
            if (type == String.class) {
                return (T) getString(key, (String) defValue, name);
            } else if (type == int.class) {
                return (T) getInt(key, (Integer) defValue, name);
            } else if (type == long.class) {
                return (T) getLong(key, (Long) defValue, name);
            } else if (type == float.class) {
                return (T) getFloat(key, (Float) defValue, name);
            } else if (type == boolean.class) {
                return (T) getBoolean(key, (Boolean) defValue, name);
            } else {
                throw new IllegalStateException("without handle type \"" + type + "\"");
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
        }
        throw new IllegalStateException("load \"" + key + "\" from \"" + clazz.getName() + "\" failed");
    }

    /**
     * 数据存储
     *
     * @param clazz
     * @param key
     * @param value
     * @param <P>
     */
    public static <P extends BasePreference> void save(Class<P> clazz, String key, Object value) {
        try {
//            Constructor c = clazz.getDeclaredConstructor(Context.class);
//            P preference = (P) c.newInstance(context);
            P preference = clazz.newInstance();
            String name = preference.getName();
            Field field = preference.getField(key);
            Class type = field.getType();
            if (type == String.class) {
                saveString(key, (String) value, name);
                return;
            } else if (type == int.class) {
                saveInt(key, (Integer) value, name);
                return;
            } else if (type == long.class) {
                saveLong(key, (Long) value, name);
                return;
            } else if (type == boolean.class) {
                saveBoolean(key, (Boolean) value, name);
                return;
            } else {
                throw new IllegalStateException("without handle type \"" + type + "\"");
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
        }
        throw new IllegalStateException("save \"" + key + "\" to \"" + clazz.getName() + "\" failed");
    }

    /**
     * 注册SharedPreference监听
     *
     * @param listener listener
     */
    public static void registListener(SharedPreferences.OnSharedPreferenceChangeListener listener, String name) {
        SharedPreferences sharedPreferences = getSharedPreferences(name);
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    /**
     * 保存string到SharedPreferences
     *
     * @param key
     * @param value
     */
    protected static void saveString(String key, String value, String name) {
        SharedPreferences.Editor editor = getSharedPreferences(name).edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 从SharedPreferences获取string
     *
     * @param key
     * @return
     */
    protected static String getString(String key, String defValue, String name) {
        return getSharedPreferences(name).getString(key, defValue);
    }

    /**
     * 保存int到SharedPreferences
     *
     * @param key
     * @param value
     */
    protected static void saveInt(String key, int value, String name) {
        SharedPreferences.Editor editor = getSharedPreferences(name).edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * 从SharedPreferences获取int
     *
     * @param key
     * @return
     */
    protected static Integer getInt(String key, int defValue, String name) {
        return getSharedPreferences(name).getInt(key, defValue);
    }

    /**
     * 保存long到SharedPreferences
     *
     * @param key
     * @param value
     */
    protected static void saveLong(String key, long value, String name) {
        SharedPreferences.Editor editor = getSharedPreferences(name).edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * 从SharedPreferences获取long
     *
     * @param key
     * @return
     */
    protected static Long getLong(String key, long defValue, String name) {
        return getSharedPreferences(name).getLong(key, defValue);
    }

    /**
     * 保存float到SharedPreferences
     *
     * @param key
     * @param value
     */
    protected static void saveFloat(String key, float value, String name) {
        SharedPreferences.Editor editor = getSharedPreferences(name).edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    /**
     * 从SharedPreferences获取float
     *
     * @param key
     * @return
     */
    protected static Float getFloat(String key, float defValue, String name) {
        return getSharedPreferences(name).getFloat(key, defValue);
    }

    /**
     * 保存boolean到SharedPreferences
     *
     * @param key
     * @param value
     */
    protected static void saveBoolean(String key, boolean value, String name) {
        SharedPreferences.Editor editor = getSharedPreferences(name).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }


    /**
     * 从SharedPreferences获取boolean
     *
     * @param key
     * @return
     */
    protected static Boolean getBoolean(String key, boolean defValue, String name) {
        return getSharedPreferences(name).getBoolean(key, defValue);
    }

    /**
     * 获取SharedPreferences对象
     *
     * @return
     */
    private static SharedPreferences getSharedPreferences(String name) {
        return MyApplication.application.getSharedPreferences(name, Context.MODE_PRIVATE);
    }
}
