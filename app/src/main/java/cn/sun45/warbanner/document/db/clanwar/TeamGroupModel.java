package cn.sun45.warbanner.document.db.clanwar;

import cn.sun45.warbanner.framework.document.db.BaseDbTableModel;
import cn.sun45.warbanner.framework.document.db.annotation.DbTableConfigure;
import cn.sun45.warbanner.framework.document.db.annotation.DbTableParamConfigure;

/**
 * Created by Sun45 on 2021/5/23
 * 分刀信息数据模型
 */
@DbTableConfigure(tablename = "teamgroup")
public class TeamGroupModel extends BaseDbTableModel {

    @DbTableParamConfigure
    private String teamone;
    @DbTableParamConfigure
    private int borrowindexone;

    @DbTableParamConfigure
    private String teamtwo;
    @DbTableParamConfigure
    private int borrowindextwo;

    @DbTableParamConfigure
    private String teamthree;
    @DbTableParamConfigure
    private int borrowindexthree;

    public String getTeamone() {
        return teamone;
    }

    public void setTeamone(String teamone) {
        this.teamone = teamone;
    }

    public int getBorrowindexone() {
        return borrowindexone;
    }

    public void setBorrowindexone(int borrowindexone) {
        this.borrowindexone = borrowindexone;
    }

    public String getTeamtwo() {
        return teamtwo;
    }

    public void setTeamtwo(String teamtwo) {
        this.teamtwo = teamtwo;
    }

    public int getBorrowindextwo() {
        return borrowindextwo;
    }

    public void setBorrowindextwo(int borrowindextwo) {
        this.borrowindextwo = borrowindextwo;
    }

    public String getTeamthree() {
        return teamthree;
    }

    public void setTeamthree(String teamthree) {
        this.teamthree = teamthree;
    }

    public int getBorrowindexthree() {
        return borrowindexthree;
    }

    public void setBorrowindexthree(int borrowindexthree) {
        this.borrowindexthree = borrowindexthree;
    }

    @Override
    protected Class getProviderClass() {
        return ClanWarProvider.class;
    }
}
