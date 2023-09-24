package cn.sun45.warbanner.ui.views.combinationlist;

import java.util.List;

import cn.sun45.warbanner.ui.views.combinationlist.selectbar.CombinationListSelectItem;

/**
 * Created by Sun45 on 2023/9/24
 * 套餐列表监听
 */
public interface CombinationListListener {
    void dataSet(int count, List<CombinationListSelectItem> listSelectItems);

    void onScrolled(int first, int last);

    void open(CombinationListModel combinationListModel);
}
