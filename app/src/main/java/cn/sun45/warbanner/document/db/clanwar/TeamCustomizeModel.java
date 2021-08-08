package cn.sun45.warbanner.document.db.clanwar;

import cn.sun45.warbanner.framework.document.db.BaseDbTableModel;
import cn.sun45.warbanner.framework.document.db.annotation.DbTableConfigure;
import cn.sun45.warbanner.framework.document.db.annotation.DbTableParamConfigure;

/**
 * Created by Sun45 on 2021/7/19
 * 阵容自定义信息数据模型
 */
@DbTableConfigure(tablename = "teamcustomize")
public class TeamCustomizeModel extends BaseDbTableModel {
    //会战日期 202107
    @DbTableParamConfigure
    private String date;
    //阵容编号
    @DbTableParamConfigure
    private String number;

    //阵容屏蔽
    @DbTableParamConfigure
    private boolean block;

    //自定轴伤 伤害省略数值(550)
    @DbTableParamConfigure
    private int ellipsisdamage;

    public TeamCustomizeModel() {
        block = false;
        ellipsisdamage = -1;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean isBlock() {
        return block;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }

    //自定轴伤生效
    public boolean damageEffective() {
        return ellipsisdamage != -1;
    }

    public int getEllipsisdamage() {
        return ellipsisdamage;
    }

    public void setEllipsisdamage(int ellipsisdamage) {
        this.ellipsisdamage = ellipsisdamage;
    }

    @Override
    protected Class getProviderClass() {
        return ClanWarProvider.class;
    }
}
