package cn.sun45.warbanner.framework.document.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

import java.util.List;

import cn.sun45.warbanner.framework.document.db.annotation.ProviderConfigure;

/**
 * Created by Sun45 on 2019/10/28.
 * 数据库数据提供器基础类
 */
public abstract class BaseProvider extends ContentProvider {
    SQLiteDbHelper dBlite;

    SQLiteDatabase db;

    protected abstract List<BaseDbTableModel> getTableClassList();

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public boolean onCreate() {
        ProviderConfigure providerConfigure = getClass().getAnnotation(ProviderConfigure.class);
        if (providerConfigure == null) {
            throw new IllegalStateException("need ProviderConfigure to init");
        }
        String dbname = providerConfigure.dbname();
        if (TextUtils.isEmpty(dbname)) {
            throw new IllegalStateException("dbname has not been initialized");
        }
        this.dBlite = new SQLiteDbHelper(this.getContext(), dbname, getClass().getAnnotation(ProviderConfigure.class).version(), getTableClassList());
        return true;
    }

    /**
     * 增
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        db = dBlite.getWritableDatabase();
        String table = uri.getPathSegments().get(0);
        long id = db.insert(table, getClass().getAnnotation(ProviderConfigure.class).nullstring(), values);
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * 删
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        db = dBlite.getWritableDatabase();
        String table = uri.getPathSegments().get(0);
        return db.delete(table, selection, selectionArgs);
    }

    /**
     * 查
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        db = dBlite.getWritableDatabase();
        String table = uri.getPathSegments().get(0);
        return db.query(table, projection, selection, selectionArgs, null, null, sortOrder);
    }

    /**
     * 改
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        db = dBlite.getWritableDatabase();
        String table = uri.getPathSegments().get(0);
        return db.update(table, values, selection, selectionArgs);
    }
}
