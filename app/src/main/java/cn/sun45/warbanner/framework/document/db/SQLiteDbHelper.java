package cn.sun45.warbanner.framework.document.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Field;
import java.util.List;

import cn.sun45.warbanner.framework.document.db.annotation.DbTableConfigure;
import cn.sun45.warbanner.framework.document.db.annotation.DbTableParamConfigure;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2019/10/28.
 * 数据库帮助类
 */
public class SQLiteDbHelper extends SQLiteOpenHelper {
    private static final String TAG = "SQLiteDbHelper";

    private static String id = "_id";

    private List<BaseDbTableModel> list;

    public SQLiteDbHelper(Context context, String dbname, int version, List<BaseDbTableModel> list) {
        super(context, dbname, null, version);
        this.list = list;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (BaseDbTableModel model : list) {
            db.execSQL(createTable(model));
        }
    }

    private String createTable(BaseDbTableModel model) {
        String str = "create table " + model.getClass().getAnnotation(DbTableConfigure.class).tablename() + "(" + id + " integer primary key autoincrement not null";
        Field[] declaredFields = model.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            DbTableParamConfigure dbTableParamConfigure = field.getAnnotation(DbTableParamConfigure.class);
            if (dbTableParamConfigure != null) {
                String name = field.getName();
                String s = "," + name;
                if (dbTableParamConfigure.isbytearray()) {
                    s += " blob";
                } else {
                    s += " text not null";
                }
                str += s;
            }
        }
        str += ");";
        Utils.logD(TAG, "createTable str:" + str);
        return str;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (BaseDbTableModel model : list) {
            String sql="DROP TABLE IF EXISTS " + model.getClass().getAnnotation(DbTableConfigure.class).tablename();
            db.execSQL(sql);
        }
        onCreate(db);
    }
}
