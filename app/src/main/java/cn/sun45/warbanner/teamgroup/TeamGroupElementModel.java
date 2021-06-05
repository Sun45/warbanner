package cn.sun45.warbanner.teamgroup;

import java.util.List;

import cn.sun45.warbanner.document.db.clanwar.TeamModel;

/**
 * Created by Sun45 on 2021/5/30
 * 分刀元素数据
 */
public class TeamGroupElementModel {
    private List<Integer> idlist;
    private int screencharacter;
    private TeamModel teamModel;

    public List<Integer> getIdlist() {
        return idlist;
    }

    public void setIdlist(List<Integer> idlist) {
        this.idlist = idlist;
    }

    public int getScreencharacter() {
        return screencharacter;
    }

    public void setScreencharacter(int screencharacter) {
        this.screencharacter = screencharacter;
    }

    public TeamModel getTeamModel() {
        return teamModel;
    }

    public void setTeamModel(TeamModel teamModel) {
        this.teamModel = teamModel;
    }
}
