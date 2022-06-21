package cn.sun45.warbanner.document.database.source;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import cn.sun45.warbanner.document.database.source.models.BossModel;
import cn.sun45.warbanner.document.database.source.models.CharacterModel;
import cn.sun45.warbanner.document.database.source.models.TeamModel;
import cn.sun45.warbanner.framework.MyApplication;

/**
 * Created by Sun45 on 2022/6/9
 * 资源数据库
 */
@Database(entities = {CharacterModel.class, BossModel.class, TeamModel.class}, version = 5)
public abstract class SourceDataBase extends RoomDatabase {
    private static SourceDataBase databaseInstance;

    public static synchronized SourceDataBase getInstance() {
        if (databaseInstance == null) {
            databaseInstance = Room.databaseBuilder(MyApplication.application, SourceDataBase.class, "source")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return databaseInstance;
    }

    public abstract SourceDao sourceDao();
}
