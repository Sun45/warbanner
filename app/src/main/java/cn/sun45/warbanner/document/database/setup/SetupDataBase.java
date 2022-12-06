package cn.sun45.warbanner.document.database.setup;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import cn.sun45.warbanner.document.database.setup.models.ScreenCharacterModel;
import cn.sun45.warbanner.document.database.setup.models.TeamCustomizeModel;
import cn.sun45.warbanner.document.database.setup.models.TeamGroupCollectionModel;
import cn.sun45.warbanner.document.database.setup.models.TeamGroupScreenModel;
import cn.sun45.warbanner.document.database.setup.models.TeamGroupScreenUsedCharacterModel;
import cn.sun45.warbanner.document.database.setup.models.TeamListShowModel;
import cn.sun45.warbanner.document.database.setup.models.UserModel;
import cn.sun45.warbanner.framework.MyApplication;

/**
 * Created by Sun45 on 2022/6/9
 * 设置数据库
 */
@Database(entities = {
        UserModel.class,
        ScreenCharacterModel.class,
        TeamListShowModel.class,
        TeamGroupScreenModel.class,
        TeamGroupScreenUsedCharacterModel.class,
        TeamCustomizeModel.class,
        TeamGroupCollectionModel.class}, version = 8)
public abstract class SetupDataBase extends RoomDatabase {
    private static SetupDataBase databaseInstance;

    public static synchronized SetupDataBase getInstance() {
        if (databaseInstance == null) {
            databaseInstance = Room.databaseBuilder(MyApplication.application, SetupDataBase.class, "setup")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return databaseInstance;
    }

    public abstract SetupDao setupDao();
}
