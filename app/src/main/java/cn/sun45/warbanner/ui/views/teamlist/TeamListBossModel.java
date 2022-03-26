package cn.sun45.warbanner.ui.views.teamlist;

import java.util.List;

/**
 * Created by Sun45 on 2021/6/28
 * 阵容列表boss信息数据模型
 */
public class TeamListBossModel {
    private int stage;
    private int boss;
    private String name;
    private String iconUrl;

    private List<TeamListTeamModel> teamModels;

    public TeamListBossModel(int stage, int boss, String name, String iconUrl) {
        this.stage = stage;
        this.boss = boss;
        this.name = name;
        this.iconUrl = iconUrl;
    }

    public void setTeamModels(List<TeamListTeamModel> teamModels) {
        this.teamModels = teamModels;
    }

    public String getName() {
        return name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public int getModelCount(int type) {
        if (teamModels == null) {
            return 0;
        } else {
            if (type == 0) {
                return teamModels.size();
            } else {
                int count = 0;
                for (TeamListTeamModel teamModel : teamModels) {
                    if (teamModel.getTeamModel().getType() == type) {
                        count++;
                    }
                }
                return count;
            }
        }
    }

    public TeamListTeamModel getModel(int type, int position) {
        for (TeamListTeamModel teamModel : teamModels) {
            if (type == 0 || teamModel.getTeamModel().getType() == type) {
                if (position == 0) {
                    return teamModel;
                }
                position--;
            }
        }
        return null;
    }
}
