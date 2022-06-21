package cn.sun45.warbanner.document.database.source;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import cn.sun45.warbanner.document.database.source.models.BossModel;
import cn.sun45.warbanner.document.database.source.models.CharacterModel;
import cn.sun45.warbanner.document.database.source.models.TeamModel;

/**
 * Created by Sun45 on 2022/6/9
 * 资源数据DAO
 */
@Dao
public interface SourceDao {
    //CharacterModel
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCharacter(List<CharacterModel> characterModelList);

    @Query("delete from character where lang=:lang")
    void deleteAllCharacter(String lang);

    @Query("select * from character where lang=:lang")
    List<CharacterModel> queryAllCharacter(String lang);

    //BossModel
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBoss(List<BossModel> bossModelList);

    @Query("delete from boss where lang=:lang")
    void deleteAllBoss(String lang);

    @Query("select * from boss where lang=:lang ORDER BY bossIndex ASC")
    List<BossModel> queryAllBoss(String lang);

    //TeamModel
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTeam(List<TeamModel> teamModelList);

    @Query("delete from team where lang=:lang")
    void deleteAllTeam(String lang);

    @Query("select * from team where lang=:lang")
    List<TeamModel> queryAllTeam(String lang);

    @Query("select * from team where lang=:lang AND id=:id")
    TeamModel queryTeam(String lang, int id);
}
