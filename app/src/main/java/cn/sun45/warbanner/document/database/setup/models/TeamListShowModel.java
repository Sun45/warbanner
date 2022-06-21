package cn.sun45.warbanner.document.database.setup.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by Sun45 on 2022/6/12
 * 阵容信息展示数据模型
 */
@Entity(tableName = "teamlistshow")
public class TeamListShowModel {
    @PrimaryKey
    @ColumnInfo
    private int userId;

    //链接展示
    @ColumnInfo
    private boolean linkShow;

    //阶段筛选 0:全部...
    @ColumnInfo
    private int teamListStage;

    //BOSS筛选 0:全部,1:一王,2:二王,3:三王,4:四王,5:五王
    @ColumnInfo
    private int teamListBoss;

    //刀型筛选 0:不限,1:AUTO,2:非AUTO,3:尾刀
    @ColumnInfo
    private int teamListType;

    public TeamListShowModel() {
        linkShow = true;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isLinkShow() {
        return linkShow;
    }

    public void setLinkShow(boolean linkShow) {
        this.linkShow = linkShow;
    }

    public int getTeamListStage() {
        return teamListStage;
    }

    public void setTeamListStage(int teamListStage) {
        this.teamListStage = teamListStage;
    }

    public int getTeamListBoss() {
        return teamListBoss;
    }

    public void setTeamListBoss(int teamListBoss) {
        this.teamListBoss = teamListBoss;
    }

    public int getTeamListType() {
        return teamListType;
    }

    public void setTeamListType(int teamListType) {
        this.teamListType = teamListType;
    }

    @Override
    public String toString() {
        return "TeamListShowModel{" +
                "userId=" + userId +
                ", linkShow=" + linkShow +
                ", teamListStage=" + teamListStage +
                ", teamListBoss=" + teamListBoss +
                ", teamListType=" + teamListType +
                '}';
    }
}
