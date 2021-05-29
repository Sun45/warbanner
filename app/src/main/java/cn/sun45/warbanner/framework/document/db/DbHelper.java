package cn.sun45.warbanner.framework.document.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.framework.document.db.annotation.DbTableParamConfigure;

/**
 * Created by Sun45 on 2019/10/28.
 * 数据库操作方法类
 */
public class DbHelper {
    /**
     * 将model中的数据读出转化为ContentValues
     *
     * @param model model
     * @return ContentValues
     */
    private static ContentValues modeltodata(BaseDbTableModel model) {
        ContentValues values = new ContentValues();
        Field[] declaredFields = model.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            DbTableParamConfigure dbTableParamConfigure = field.getAnnotation(DbTableParamConfigure.class);
            if (dbTableParamConfigure != null) {
                String name = field.getName();
                field.setAccessible(true);
                try {
                    Object value = field.get(model);
                    if (dbTableParamConfigure.isbytearray()) {
                        values.put(name, (byte[]) value);
                    } else {
                        values.put(name, String.valueOf(value));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return values;
    }

    /**
     * 将cursor中的数据转化为model列表
     *
     * @param cursor cursor
     * @param clazz  model的class
     * @param <T>    model类型
     * @return model列表
     */
    private static <T extends BaseDbTableModel> List<T> datatomodel(Cursor cursor, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Field[] declaredFields = clazz.getDeclaredFields();
                T m = null;
                try {
                    m = clazz.newInstance();
                    for (Field field : declaredFields) {
                        DbTableParamConfigure dbTableParamConfigure = field.getAnnotation(DbTableParamConfigure.class);
                        if (dbTableParamConfigure != null) {
                            String name = field.getName();
                            field.setAccessible(true);
                            if (dbTableParamConfigure.isbytearray()) {
                                byte[] value = cursor.getBlob(cursor.getColumnIndex(name));
                                field.set(m, value);
                            } else {
                                String value = cursor.getString(cursor.getColumnIndex(name));
                                Class type = field.getType();
                                if (type == String.class) {
                                    field.set(m, value);
                                } else if (type == int.class) {
                                    field.set(m, Integer.valueOf(value));
                                } else if (type == boolean.class) {
                                    field.set(m, Boolean.valueOf(value));
                                } else {
                                    throw new IllegalStateException("without handle type \"" + type.getName() + "\"");
                                }
                            }
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
                if (m == null) {
                    throw new IllegalStateException("model \"" + clazz.getName() + "\" cannot initialize check default constructor");
                }
                list.add(m);
            } while (cursor.moveToNext());
        }
        return list;
    }

    /**
     * 获得model对应的表主键名
     *
     * @param clazz model的class
     * @param <T>   model类型
     * @return 主键名
     */
    private static <T extends BaseDbTableModel> String getkeyparamName(Class<T> clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        Field f = null;
        for (Field field : declaredFields) {
            DbTableParamConfigure configure = field.getAnnotation(DbTableParamConfigure.class);
            if (configure != null) {
                if (configure.iskeyparm()) {
                    f = field;
                    break;
                }
            }
        }
        if (f == null) {
            throw new IllegalStateException("there is no keyparam in \"" + clazz.getSimpleName() + "\"");
        }
        String name = f.getName();
        return name;
    }

    /**
     * 获得model对应的表的字段名列表
     *
     * @param clazz model的class
     * @param <T>   model类型
     * @return 字段名列表
     */
    private static <T extends BaseDbTableModel> List<String> getParamNames(Class<T> clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        List<String> list = new ArrayList<>();
        for (Field field : declaredFields) {
            DbTableParamConfigure configure = field.getAnnotation(DbTableParamConfigure.class);
            if (configure != null) {
                String name = field.getName();
                list.add(name);
            }
        }
        return list;
    }

    /**
     * 判断字段名是否在字段名列表中，如果不存在就会抛出异常
     *
     * @param clazz model的class
     * @param names 字段名列表
     * @param param 字段名
     * @param <T>   model类型
     */
    private static <T extends BaseDbTableModel> void containsParam(Class<T> clazz, List<String> names, String param) {
        boolean contain = false;
        for (String name : names) {
            if (name.equals(param)) {
                contain = true;
                break;
            }
        }
        if (!contain) {
            throw new IllegalArgumentException("no such param \"" + param + "\" in " + clazz.getSimpleName());
        }
    }

    /**
     * 插入一条新的数据
     *
     * @param context context
     * @param model   model
     */
    public static void insert(Context context, BaseDbTableModel model) {
        ContentValues values = modeltodata(model);
        ContentResolver contentResolver = context.getContentResolver();
        contentResolver.insert(model.getUri(), values);
    }

    /**
     * 删除model对应的表数据
     *
     * @param context context
     * @param clazz   model的class
     * @param <T>     model类型
     */
    public static <T extends BaseDbTableModel> void delete(Context context, Class<T> clazz) {
        ContentResolver contentResolver = context.getContentResolver();
        try {
            contentResolver.delete(clazz.newInstance().getUri(), null, null);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除model对应的表中主键(keyparam)值与value相等的数据
     *
     * @param context context
     * @param clazz   model的class
     * @param <T>     model类型
     * @param value   value
     */
    public static <T extends BaseDbTableModel> void delete(Context context, Class<T> clazz, String value) {
        String name = getkeyparamName(clazz);
        ContentResolver contentResolver = context.getContentResolver();
        try {
            contentResolver.delete(clazz.newInstance().getUri(), name + "= ?", new String[]{value});
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除model对应的表中params与values分别同时对应的数据
     *
     * @param context context
     * @param clazz   model的class
     * @param <T>     model类型
     * @param params  字段名数组
     * @param values  字段值数组
     */
    public static <T extends BaseDbTableModel> void delete(Context context, Class<T> clazz, String[] params, String[] values) {
        List<String> names = getParamNames(clazz);
        for (String param : params) {
            containsParam(clazz, names, param);
        }
        ContentResolver contentResolver = context.getContentResolver();
        String where = "";
        boolean first = true;
        for (String param : params) {
            if (first) {
                first = false;
            } else {
                where += " and ";
            }
            where += param + "= ?";
        }
        try {
            contentResolver.delete(clazz.newInstance().getUri(), where, values);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除model对应的表中param与value对应的数据
     *
     * @param context context
     * @param clazz   model的class
     * @param <T>     model类型
     * @param param   字段名
     * @param value   字段值
     */
    public static <T extends BaseDbTableModel> void delete(Context context, Class<T> clazz, String param, String value) {
        delete(context, clazz, new String[]{param}, new String[]{value});
    }

    /**
     * 修改model对应的表中主键(keyparam)值与value相等的数据为model中的数据
     *
     * @param context context
     * @param model   model
     * @param value   value
     */
    public static void modify(Context context, BaseDbTableModel model, String value) {
        ContentResolver contentResolver = context.getContentResolver();
        String name = getkeyparamName(model.getClass());
        contentResolver.update(model.getUri(), modeltodata(model), name + "= ?", new String[]{value});
    }

    /**
     * 修改model对应的表中params与values分别同时对应的数据为model中的数据
     *
     * @param context context
     * @param model   model
     * @param params  字段名数组
     * @param values  字段值数组
     */
    public static void modify(Context context, BaseDbTableModel model, String[] params, String[] values) {
        List<String> names = getParamNames(model.getClass());
        for (String param : params) {
            containsParam(model.getClass(), names, param);
        }
        ContentResolver contentResolver = context.getContentResolver();
        String where = "";
        boolean first = true;
        for (String param : params) {
            if (first) {
                first = false;
            } else {
                where += " and ";
            }
            where += param + "= ?";
        }
        contentResolver.update(model.getUri(), modeltodata(model), where, values);
    }

    /**
     * 修改model对应的表中param与value对应的数据为model中的数据
     *
     * @param context context
     * @param model   model
     * @param param   字段名
     * @param value   字段值
     */
    public static void modify(Context context, BaseDbTableModel model, String param, String value) {
        modify(context, model, new String[]{param}, new String[]{value});
    }

    /**
     * 查找model对应的表的所有数据
     *
     * @param context context
     * @param clazz   model的class
     * @param <T>     model类型
     * @return 数据列表
     */
    public static <T extends BaseDbTableModel> List<T> query(Context context, Class<T> clazz) {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(clazz.newInstance().getUri(), null, null, null, null);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        List<T> baseDbTableModels = datatomodel(cursor, clazz);
        if (cursor != null) {
            cursor.close();
        }
        return baseDbTableModels;
    }

    /**
     * 查找model对应的表的中主键(keyparam)值与value相等的第一条数据，不存在返回null
     *
     * @param context context
     * @param clazz   model的class
     * @param <T>     model类型
     * @param value   value
     * @return 数据列表
     */
    public static <T extends BaseDbTableModel> T query(Context context, Class<T> clazz, String value) {
        ContentResolver contentResolver = context.getContentResolver();
        String param = getkeyparamName(clazz);
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(clazz.newInstance().getUri(), null, param + "= ?", new String[]{value}, null);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        List<T> list = datatomodel(cursor, clazz);
        if (cursor != null) {
            cursor.close();
        }
        return list.size() > 0 ? list.get(0) : null;
    }

    /**
     * 查找model对应的表的中param与value对应的所有数据
     *
     * @param context context
     * @param clazz   model的class
     * @param <T>     model类型
     * @param param   字段名
     * @param value   字段值
     * @return
     */
    public static <T extends BaseDbTableModel> List<T> query(Context context, Class<T> clazz, String param, String value) {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(clazz.newInstance().getUri(), null, param + "= ?", new String[]{value}, null);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        List<T> list = datatomodel(cursor, clazz);
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    /**
     * 查找model对应的表的中param列表与value列表对应的第一条数据
     *
     * @param context context
     * @param clazz   model的class
     * @param <T>     model类型
     * @param params  字段名列表
     * @param values  字段值列表
     * @return
     */
    public static <T extends BaseDbTableModel> T queryFirst(Context context, Class<T> clazz, String[] params, String[] values) {
        List<T> list = query(context, clazz, params, values);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 查找model对应的表的中param列表与value列表对应的所有数据
     *
     * @param context context
     * @param clazz   model的class
     * @param <T>     model类型
     * @param params  字段名列表
     * @param values  字段值列表
     * @return
     */
    public static <T extends BaseDbTableModel> List<T> query(Context context, Class<T> clazz, String[] params, String[] values) {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = null;
        String selection = "";
        for (int i = 0; i < params.length; i++) {
            String param = params[i];
            if (i != 0) {
                selection += " AND ";
            }
            selection += param + "= ?";
        }
        try {
            cursor = contentResolver.query(clazz.newInstance().getUri(), null, selection, values, null);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        List<T> list = datatomodel(cursor, clazz);
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }
}
