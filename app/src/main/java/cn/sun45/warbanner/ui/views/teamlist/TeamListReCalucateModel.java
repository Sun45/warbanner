package cn.sun45.warbanner.ui.views.teamlist;

import java.util.List;

/**
 * Created by Sun45 on 2023/10/19
 * 阵容列表再生成信息数据模型
 */
public class TeamListReCalucateModel {
    private List<Integer> usedCharacterList;

    public TeamListReCalucateModel(List<Integer> usedCharacterList) {
        this.usedCharacterList = usedCharacterList;
    }

    public List<Integer> getUsedCharacterList() {
        return usedCharacterList;
    }
}
