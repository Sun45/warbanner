package cn.sun45.warbanner.ui.views.teamlist;

/**
 * Created by Sun45 on 2021/6/28
 * 阵容列表boss信息数据模型
 */
public class TeamListBossModel {
    private String name;
    private String iconUrl;

    public TeamListBossModel(String name, String iconUrl) {
        this.name = name;
        this.iconUrl = iconUrl;
    }

    public String getName() {
        return name;
    }

    public String getIconUrl() {
        return iconUrl;
    }
}
