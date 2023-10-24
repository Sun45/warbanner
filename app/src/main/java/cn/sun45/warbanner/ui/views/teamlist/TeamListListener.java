package cn.sun45.warbanner.ui.views.teamlist;

import java.util.List;

import cn.sun45.warbanner.document.database.source.models.TeamModel;
import cn.sun45.warbanner.ui.views.listselectbar.ListSelectItem;

/**
 * Created by Sun45 on 2021/7/4
 * 阵容列表监听
 */
public interface TeamListListener {
    void dataSet(int count, List<ListSelectItem> listSelectItems);

    void onScrolled(int first, int last);

    void select(TeamModel teamModel);

    void reCalucate(TeamListReCalucateModel teamListReCalucateModel);
}