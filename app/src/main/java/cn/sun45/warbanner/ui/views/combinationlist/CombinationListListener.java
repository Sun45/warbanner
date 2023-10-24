package cn.sun45.warbanner.ui.views.combinationlist;

import java.util.List;

import cn.sun45.warbanner.ui.views.listselectbar.ListSelectItem;
import cn.sun45.warbanner.ui.views.teamlist.TeamListReCalucateModel;


/**
 * Created by Sun45 on 2023/9/24
 * 套餐列表监听
 */
public interface CombinationListListener {
    void dataSet(int count, List<ListSelectItem> listSelectItems);

    void onScrolled(int first, int last);

    void open(CombinationListModel combinationListModel);

    void reCalucate(CombinationListModel combinationListModel);
}
