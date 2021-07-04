package cn.sun45.warbanner.datamanager.source;

import android.annotation.SuppressLint;
import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sun45.warbanner.document.StaticHelper;
import cn.sun45.warbanner.util.FileUtil;

/**
 * Created by Sun45 on 2021/5/23
 * 资源数据处理帮助类
 */
public class SourceDataProcessHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;

    private static SourceDataProcessHelper instance;

    public static void init(Application application) {
        if (instance == null) {
            synchronized (SourceDataProcessHelper.class) {
                if (instance == null) {
                    instance = new SourceDataProcessHelper(application);
                }
            }
        }
    }

    public static SourceDataProcessHelper getInstance() {
        return instance;
    }

    public SourceDataProcessHelper(Application application) {
        super(application, StaticHelper.DB_FILE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropAll(db);
        onCreate(db);
    }

    /**
     * 删除所有表
     *
     * @param db
     */
    private void dropAll(SQLiteDatabase db) {
        List<String> sqls = new ArrayList<>();
        String op = "DROP TABLE IF EXISTS ";
        for (Field field : this.getClass().getDeclaredFields()) {
            if (field.getName().startsWith("TABLE_NAME")) {
                try {
                    sqls.add(op + field.get(this));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        for (String sql : sqls) {
            db.execSQL(sql);
        }
    }

    private <T> List<T> cursor2List(Cursor cursor, Class<T> theClass) {
        List<T> result = new ArrayList<>();
        Field[] arrField = theClass.getDeclaredFields();
        try {
            while (cursor.moveToNext()) {
                if (cursor.isBeforeFirst()) {
                    continue;
                }
                T bean = theClass.newInstance();
                for (Field f : arrField) {
                    String columnName = f.getName();
                    int columnIdx = cursor.getColumnIndex(columnName);
                    if (columnIdx != -1) {
                        if (!f.isAccessible()) {
                            f.setAccessible(true);
                        }
                        Class type = f.getType();
                        if (type == Byte.class) {
                            f.set(bean, Short.valueOf(cursor.getShort(columnIdx)).byteValue());
                        } else if (type == Short.class) {
                            f.set(bean, cursor.getShort(columnIdx));
                        } else if (type == int.class) {
                            f.set(bean, cursor.getInt(columnIdx));
                        } else if (type == long.class) {
                            f.set(bean, cursor.getLong(columnIdx));
                        } else if (type == String.class) {
                            f.set(bean, cursor.getString(columnIdx));
                        } else if (type == byte[].class) {
                            f.set(bean, cursor.getBlob(columnIdx));
                        } else if (type == boolean.class) {
                            f.set(bean, cursor.getInt(columnIdx) == 1);
                        } else if (type == float.class) {
                            f.set(bean, cursor.getFloat(columnIdx));
                        } else if (type == double.class) {
                            f.set(bean, cursor.getDouble(columnIdx));
                        }
                    }
                }
                result.add(bean);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        } finally {
            cursor.close();
        }
        return result;
    }

    /***
     * 由SQL语句、SQL中的键值从数据库获取单个实体
     * @param sql SQL语句
     * @param theClass 类名
     * @param <T> theClass的类
     * @return 生成的实体
    </T> */
    @SuppressLint("Recycle")
    private <T> T getBeanByRaw(String sql, Class<T> theClass) {
        if (!FileUtil.checkFile(FileUtil.getDbFilePath(StaticHelper.DB_FILE_NAME))) {
            return null;
        }
        try {
            Cursor cursor = getReadableDatabase().rawQuery(sql, null);
            if (cursor == null) {
                return null;
            }
            List<T> data = cursor2List(cursor, theClass);
            if (data != null && !data.isEmpty()) {
                return data.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /***
     * 由SQL语句无条件从数据库获取实体列表
     * @param sql SQL语句
     * @param theClass 类名
     * @param <T> theClass的类
     * @return 生成的实体列表
    </T> */
    @SuppressLint("Recycle")
    private <T> List<T> getBeanListByRaw(String sql, Class<T> theClass) {
        if (!FileUtil.checkFile(FileUtil.getDbFilePath(StaticHelper.DB_FILE_NAME))) {
            return null;
        }
        try {
            Cursor cursor = getReadableDatabase().rawQuery(sql, null);
            if (cursor == null) {
                return null;
            }
            return cursor2List(cursor, theClass);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /***
     * 获取查询语句的第一行第一列值
     * @param sql
     * @return
     */
    private String getOne(String sql) {
        if (!FileUtil.checkFile(FileUtil.getDbFilePath(StaticHelper.DB_FILE_NAME))) {
            return null;
        }
        Cursor cursor = getReadableDatabase().rawQuery(sql, null);
        cursor.moveToNext();
        String result = cursor.getString(0);
        cursor.close();
        return result;
    }

    /***
     * 获取 int-string map
     * @param sql
     * @return
     */
    private Map<Integer, String> getIntStringMap(String sql, String key, String value) {
        if (!FileUtil.checkFile(FileUtil.getDbFilePath(StaticHelper.DB_FILE_NAME))) {
            return null;
        }
        Cursor cursor = getReadableDatabase().rawQuery(sql, null);
        Map<Integer, String> result = new HashMap<>();
        while (cursor.moveToNext()) {
            result.put(cursor.getInt(cursor.getColumnIndex(key)), cursor.getString(cursor.getColumnIndex(value)));
        }
        cursor.close();
        return result;
    }


    /************************* public field **************************/

    /***
     * 获取角色基础数据
     */
    public List<RawUnitBasic> getCharaBase() {
        return getBeanListByRaw("\n" +
                "                        SELECT ud.unit_id\n" +
                "                        ,ud.unit_name\n" +
                "                        ,ud.kana\n" +
                "                        ,ud.prefab_id\n" +
                "                        ,ud.move_speed\n" +
                "                        ,ud.search_area_width\n" +
                "                        ,ud.atk_type\n" +
                "                        ,ud.normal_atk_cast_time\n" +
                "                        ,ud.guild_id\n" +
                "                        ,ud.comment\n" +
                "                        ,ud.start_time\n" +
                "                        ,up.age\n" +
                "                        ,up.guild\n" +
                "                        ,up.race\n" +
                "                        ,up.height\n" +
                "                        ,up.weight\n" +
                "                        ,up.birth_month\n" +
                "                        ,up.birth_day\n" +
                "                        ,up.blood_type\n" +
                "                        ,up.favorite\n" +
                "                        ,up.voice\n" +
                "                        ,up.catch_copy\n" +
                "                        ,up.self_text\n" +
                "                        ,IFNULL(au.unit_name, ud.unit_name) 'actual_name'\n" +
                "                        FROM unit_data AS ud\n" +
                "                        JOIN unit_profile AS up ON ud.unit_id = up.unit_id\n" +
                "                        LEFT JOIN actual_unit_background AS au ON substr(ud.unit_id,1,4) = substr(au.unit_id,1,4)\n" +
                "                        WHERE ud.comment <> ''\n" +
                "                        AND ud.unit_id < 400000\n" +
                "                        ", RawUnitBasic.class);
    }

    /***
     * 获取会战期次
     * @param
     * @return
     */
    public List<RawClanBattlePeriod> getClanBattlePeriod() {
        //只显示最近13期会战
        boolean limit = false;
        return getBeanListByRaw("\n" +
                "                        SELECT *\n" +
                "                        FROM clan_battle_period\n" +
                "                        ORDER BY clan_battle_id DESC\n" +
                "                        " + (limit ? "LIMIT 13" : ""), RawClanBattlePeriod.class);
    }

    /***
     * 获取会战phase
     * @param
     * @return
     */
    public List<RawClanBattlePhase> getClanBattlePhase(int clanBattleId) {
        return getBeanListByRaw("\n" +
                "                        SELECT DISTINCT \n" +
                "                        phase \n" +
                "                        ,wave_group_id_1 \n" +
                "                        ,wave_group_id_2 \n" +
                "                        ,wave_group_id_3 \n" +
                "                        ,wave_group_id_4 \n" +
                "                        ,wave_group_id_5 \n" +
                "                        FROM clan_battle_2_map_data WHERE clan_battle_id=" + clanBattleId + "\n" +
                "                        ORDER BY phase DESC", RawClanBattlePhase.class);
    }

    /***
     * 获取wave
     * @param
     * @return
     */
    public List<RawWaveGroup> getWaveGroupData(List<Integer> waveGroupList) {
        String in = "";
        for (int i = 0; i < waveGroupList.size(); i++) {
            Integer integer = waveGroupList.get(i);
            if (i != 0) {
                in += ",";
            }
            in += integer;
        }
        return getBeanListByRaw("\n" +
                "                        SELECT * \n" +
                "                        FROM wave_group_data\n" +
                "                        WHERE wave_group_id IN ( " + in +
                "                        )\n", RawWaveGroup.class);
    }

    /***
     * 获取enemyList
     * @param
     * @return
     */
    public List<RawEnemy> getEnemy(List<Integer> enemyIdList) {
        String in = "";
        for (int i = 0; i < enemyIdList.size(); i++) {
            Integer integer = enemyIdList.get(i);
            if (i != 0) {
                in += ",";
            }
            in += integer;
        }
        return getBeanListByRaw("\n" +
                "                    SELECT \n" +
                "                    a.* \n" +
                "                    ,b.union_burst \n" +
                "                    ,b.union_burst_evolution \n" +
                "                    ,b.main_skill_1 \n" +
                "                    ,b.main_skill_evolution_1 \n" +
                "                    ,b.main_skill_2 \n" +
                "                    ,b.main_skill_evolution_2 \n" +
                "                    ,b.ex_skill_1 \n" +
                "                    ,b.ex_skill_evolution_1 \n" +
                "                    ,b.main_skill_3 \n" +
                "                    ,b.main_skill_4 \n" +
                "                    ,b.main_skill_5 \n" +
                "                    ,b.main_skill_6 \n" +
                "                    ,b.main_skill_7 \n" +
                "                    ,b.main_skill_8 \n" +
                "                    ,b.main_skill_9 \n" +
                "                    ,b.main_skill_10 \n" +
                "                    ,b.ex_skill_2 \n" +
                "                    ,b.ex_skill_evolution_2 \n" +
                "                    ,b.ex_skill_3 \n" +
                "                    ,b.ex_skill_evolution_3 \n" +
                "                    ,b.ex_skill_4 \n" +
                "                    ,b.ex_skill_evolution_4 \n" +
                "                    ,b.ex_skill_5 \n" +
                "                    ,b.sp_skill_1 \n" +
                "                    ,b.ex_skill_evolution_5 \n" +
                "                    ,b.sp_skill_2 \n" +
                "                    ,b.sp_skill_3 \n" +
                "                    ,b.sp_skill_4 \n" +
                "                    ,b.sp_skill_5 \n" +
                "                    ,c.child_enemy_parameter_1 \n" +
                "                    ,c.child_enemy_parameter_2 \n" +
                "                    ,c.child_enemy_parameter_3 \n" +
                "                    ,c.child_enemy_parameter_4 \n" +
                "                    ,c.child_enemy_parameter_5 \n" +
                "                    ,u.prefab_id \n" +
                "                    ,u.atk_type \n" +
                "                    ,u.normal_atk_cast_time\n" +
                "                    ,u.search_area_width\n" +
                "                    ,u.comment\n" +
                "                    FROM \n" +
                "                    unit_skill_data b \n" +
                "                    ,enemy_parameter a \n" +
                "                    LEFT JOIN enemy_m_parts c ON a.enemy_id = c.enemy_id \n" +
                "                    LEFT JOIN unit_enemy_data u ON a.unit_id = u.unit_id \n" +
                "                    WHERE \n" +
                "                    a.unit_id = b.unit_id \n" +
                "                    AND a.enemy_id in ( " + in +
                "                    )\n", RawEnemy.class);
    }
}
