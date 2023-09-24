package cn.sun45.warbanner.ui.fragments.combination;

import java.util.List;

import cn.sun45.warbanner.document.database.source.models.TeamModel;

/**
 * Created by Sun45 on 2023/9/23
 * 套餐元素模型
 */
public class CombinationElementModel {
    private TeamModel teamModel;

    private List<TeamModel.TimeLine> timeLines;

    private List<Integer> idlist;

    private int screencharacter;

    private int returnTime;

    public TeamModel getTeamModel() {
        return teamModel;
    }

    public void setTeamModel(TeamModel teamModel) {
        this.teamModel = teamModel;
    }

    public List<TeamModel.TimeLine> getTimeLines() {
        return timeLines;
    }

    public void setTimeLines(List<TeamModel.TimeLine> timeLines) {
        this.timeLines = timeLines;
    }

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

    public int getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(int returnTime) {
        this.returnTime = returnTime;
    }
}
