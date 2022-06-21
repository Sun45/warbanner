package cn.sun45.warbanner.document.database.setup.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

/**
 * Created by Sun45 on 2022/6/9
 * 筛选角色信息数据模型
 */
@Entity(tableName = "screencharacter", primaryKeys = {"userId", "characterId"})
public class ScreenCharacterModel {
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
