package cn.sun45.warbanner.document.database.setup.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by Sun45 on 2022/6/9
 * 分刀信息收藏数据模型
 */
@Entity(tableName = "teamgroupcollection", primaryKeys = {"userId", "teamoneId", "teamtwoId", "teamthreeId"})
public class TeamGroupCollectionModel {
    @NonNull
    @ColumnInfo
    private int userId;

    @NonNull
    @ColumnInfo
    private int teamoneId;
    @ColumnInfo
    private int borrowindexone;

    @NonNull
    @ColumnInfo
    private int teamtwoId;
    @ColumnInfo
    private int borrowindextwo;

    @NonNull
    @ColumnInfo
    private int teamthreeId;
    @ColumnInfo
    private int borrowindexthree;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTeamoneId() {
        return teamoneId;
    }

    public void setTeamoneId(int teamoneId) {
        this.teamoneId = teamoneId;
    }

    public int getBorrowindexone() {
        return borrowindexone;
    }

    public void setBorrowindexone(int borrowindexone) {
        this.borrowindexone = borrowindexone;
    }

    public int getTeamtwoId() {
        return teamtwoId;
    }

    public void setTeamtwoId(int teamtwoId) {
        this.teamtwoId = teamtwoId;
    }

    public int getBorrowindextwo() {
        return borrowindextwo;
    }

    public void setBorrowindextwo(int borrowindextwo) {
        this.borrowindextwo = borrowindextwo;
    }

    public int getTeamthreeId() {
        return teamthreeId;
    }

    public void setTeamthreeId(int teamthreeId) {
        this.teamthreeId = teamthreeId;
    }

    public int getBorrowindexthree() {
        return borrowindexthree;
    }

    public void setBorrowindexthree(int borrowindexthree) {
        this.borrowindexthree = borrowindexthree;
    }
}
