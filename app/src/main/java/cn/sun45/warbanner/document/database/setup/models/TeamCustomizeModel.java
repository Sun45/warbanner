package cn.sun45.warbanner.document.database.setup.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by Sun45 on 2022/6/9
 * 阵容自定义信息数据模型
 */
@Entity(tableName = "teamcustomize")
public class TeamCustomizeModel {
    @PrimaryKey
    @ColumnInfo
    private int teamId;

    //阵容屏蔽
    @ColumnInfo
    private boolean block;

    //自定轴伤
    @ColumnInfo
    private int damage;

    public TeamCustomizeModel() {
        block = false;
        damage = -1;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public boolean isBlock() {
        return block;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    //自定轴伤生效
    public boolean damageEffective() {
        return damage != -1;
    }
}
