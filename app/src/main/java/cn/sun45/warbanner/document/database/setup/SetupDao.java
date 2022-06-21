package cn.sun45.warbanner.document.database.setup;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cn.sun45.warbanner.document.database.setup.models.ScreenCharacterModel;
import cn.sun45.warbanner.document.database.setup.models.TeamCustomizeModel;
import cn.sun45.warbanner.document.database.setup.models.TeamGroupCollectionModel;
import cn.sun45.warbanner.document.database.setup.models.TeamGroupScreenModel;
import cn.sun45.warbanner.document.database.setup.models.TeamListShowModel;
import cn.sun45.warbanner.document.database.setup.models.UserModel;

/**
 * Created by Sun45 on 2022/6/9
 * 设置数据DAO
 */
@Dao
public interface SetupDao {
    //UserModel
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(UserModel userModel);

    @Query("delete from user where id=:id")
    void deleteUser(int id);

    @Query("select * from user where lang=:lang")
    List<UserModel> queryAllUser(String lang);

    @Query("select * from user where id=:id")
    UserModel queryUser(int id);

    @Query("select * from user where lang=:lang And defaultUser='1'")
    UserModel queryDefaultUser(String lang);

    //ScreenCharacterModel
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertScreenCharacter(ScreenCharacterModel screenCharacterModel);

    @Query("delete from screencharacter where userId=:userId")
    void deleteScreenCharacter(int userId);

    @Query("delete from screencharacter where userId=:userId AND characterId=:characterId")
    void deleteScreenCharacter(int userId, int characterId);

    @Update
    void updateScreenCharacter(ScreenCharacterModel screenCharacterModel);

    @Query("select * from screencharacter where userId=:userId")
    List<ScreenCharacterModel> queryAllScreenCharacter(int userId);

    @Query("select * from screencharacter where userId=:userId AND characterId=:characterId")
    ScreenCharacterModel queryScreenCharacter(int userId, int characterId);

    //TeamListShowModel
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTeamListShow(TeamListShowModel teamListShowModel);

    @Query("delete from teamlistshow where userId=:userId")
    void deleteTeamListShow(int userId);

    @Query("select * from teamlistshow where userId=:userId")
    TeamListShowModel quaryTeamListShow(int userId);

    //TeamGroupScreenModel
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTeamGroupScreen(TeamGroupScreenModel teamGroupScreenModel);

    @Query("delete from teamgroupscreen where userId=:userId")
    void deleteTeamGroupScreen(int userId);

    @Query("select * from teamgroupscreen where userId=:userId")
    TeamGroupScreenModel queryAllTeamGroupScreen(int userId);

    //TeamCustomizeModel
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTeamCustomize(TeamCustomizeModel teamCustomizeModel);

    @Query("delete from teamcustomize where teamId=:teamId")
    void deleteTeamCustomize(int teamId);

    @Query("select * from teamcustomize")
    List<TeamCustomizeModel> queryAllTeamCustomize();

    @Query("select * from teamcustomize where teamId=:teamId")
    TeamCustomizeModel queryTeamCustomize(int teamId);

    //TeamGroupCollectionModel
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTeamGroupCollection(TeamGroupCollectionModel teamGroupCollectionModel);

    @Query("delete from teamgroupcollection where userId=:userId")
    void deleteTeamGroupCollection(int userId);

    @Query("delete from teamgroupcollection where userId=:userId AND teamoneId=:teamoneId AND teamtwoId=:teamtwoId AND teamthreeId=:teamthreeId")
    void deleteTeamGroupCollection(int userId, int teamoneId, int teamtwoId, int teamthreeId);

    @Query("select * from teamgroupcollection where userId=:userId")
    List<TeamGroupCollectionModel> queryAllTeamGroupCollection(int userId);
}
