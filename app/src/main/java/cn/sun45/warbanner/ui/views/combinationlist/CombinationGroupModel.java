package cn.sun45.warbanner.ui.views.combinationlist;

import java.util.List;

/**
 * Created by Sun45 on 2023/10/14
 * 套餐列表分组数据模型
 */
public class CombinationGroupModel {
    private int order;

    private String description;

    private int picSrc;

    private List<CombinationListModel> list;

    public CombinationGroupModel(int order, String description, int picSrc, List<CombinationListModel> list) {
        this.order = order;
        this.description = description;
        this.picSrc = picSrc;
        this.list = list;
    }

    public int getOrder() {
        return order;
    }

    public String getDescription() {
        return description;
    }

    public int getPicSrc() {
        return picSrc;
    }

    public List<CombinationListModel> getList() {
        return list;
    }
}
