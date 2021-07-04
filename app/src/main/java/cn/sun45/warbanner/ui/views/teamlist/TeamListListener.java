package cn.sun45.warbanner.ui.views.teamlist;

import cn.sun45.warbanner.document.db.clanwar.TeamModel;

/**
 * Created by Sun45 on 2021/7/4
 * 阵容列表监听
 */
public interface TeamListListener {
    void select(TeamModel teamModel);
}