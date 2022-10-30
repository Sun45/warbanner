package cn.sun45.warbanner.document.database.setup.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

/**
 * Created by Sun45 on 2022/10/27
 * 分刀信息筛选已用角色数据模型
 */
@Entity(tableName = "teamgroupscreenusedcharacter", primaryKeys = {"userId", "characterId"})
public class TeamGroupScreenUsedCharacterModel {
    @NonNull
    @ColumnInfo
    private int userId;

    @NonNull
    @ColumnInfo
    private int characterId;

    @ColumnInfo
    private int type;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
