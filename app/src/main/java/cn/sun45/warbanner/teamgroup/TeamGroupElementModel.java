package cn.sun45.warbanner.teamgroup;

import java.util.List;
import java.util.stream.Collectors;

import cn.sun45.warbanner.document.database.setup.models.TeamCustomizeModel;
import cn.sun45.warbanner.document.database.source.models.TeamModel;

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
        if (teamCustomizeModel != null && teamCustomizeModel.damageEffective()) {
            return teamCustomizeModel.getDamage();
        } else {
            return teamModel.getDamage();
        }
    }

    public TeamGroupElementModel getCopy() {
        TeamGroupElementModel model = new TeamGroupElementModel();
        model.setIdlist(idlist.stream().collect(Collectors.toList()));
        model.setScreencharacter(screencharacter);
        model.setTeamModel(getTeamModel());
        model.setTeamCustomizeModel(teamCustomizeModel);
        return model;
    }

    @Override
    public String toString() {
        return "{" +
                "idlist=" + idlist +
                ", screencharacter=" + screencharacter +
                '}';
    }
}
