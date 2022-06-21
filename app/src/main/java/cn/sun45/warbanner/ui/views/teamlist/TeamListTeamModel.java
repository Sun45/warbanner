package cn.sun45.warbanner.ui.views.teamlist;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.document.database.source.models.TeamModel;

/**
 * Created by Sun45 on 2021/6/2
 * 阵容列表阵容信息数据模型
 */
public class TeamListTeamModel {
    private TeamModel teamModel;
    private int borrowindex;
    private List<TeamListRemarkModel> remarkModels;

    public TeamListTeamModel(TeamModel teamModel) {
        this(teamModel, -1);
    }

    public TeamListTeamModel(TeamModel teamModel, int borrowindex) {
        this.teamModel = teamModel;
        this.borrowindex = borrowindex;
        remarkModels = new ArrayList<>();
        List<TeamModel.TimeLine> timeLineList = teamModel.getTimeLines();
        for (TeamModel.TimeLine timeLine : timeLineList) {
            TeamListRemarkModel remarkModel = new TeamListRemarkModel();
            remarkModel.setContent(timeLine.getContent());
            remarkModel.setLink(timeLine.getLink());
            remarkModels.add(remarkModel);
        }
    }

    public TeamModel getTeamModel() {
        return teamModel;
    }

    public int getBorrowindex() {
        return borrowindex;
    }

    public List<TeamListRemarkModel> getRemarkModels() {
        return remarkModels;
    }
}
