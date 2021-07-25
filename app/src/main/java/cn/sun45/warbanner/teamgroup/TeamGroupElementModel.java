package cn.sun45.warbanner.teamgroup;

import java.util.List;

import cn.sun45.warbanner.document.db.clanwar.TeamCustomizeModel;
import cn.sun45.warbanner.document.db.clanwar.TeamModel;

/**
 * Created by Sun45 on 2021/5/30
 * 分刀元素数据
 */
public class TeamGroupElementModel {
    private List<Integer> idlist;
    private int screencharacter;
    private TeamModel teamModel;
    private TeamCustomizeModel teamCustomizeModel;

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

    public TeamCustomizeModel getTeamCustomizeModel() {
        return teamCustomizeModel;
    }

    public void setTeamCustomizeModel(TeamCustomizeModel teamCustomizeModel) {
        this.teamCustomizeModel = teamCustomizeModel;
    }

    public int getDamage() {
        if (teamCustomizeModel != null) {
            return teamCustomizeModel.getEllipsisdamage();
        } else {
            return teamModel.getEllipsisdamage();
        }
    }
}
