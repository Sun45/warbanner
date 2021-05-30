package cn.sun45.warbanner.document.db.setup;

import cn.sun45.warbanner.framework.document.db.BaseDbTableModel;
import cn.sun45.warbanner.framework.document.db.annotation.DbTableConfigure;
import cn.sun45.warbanner.framework.document.db.annotation.DbTableParamConfigure;

/**
 * Created by Sun45 on 2021/5/30
 * 筛选角色信息数据模型
 */
@DbTableConfigure(tablename = "screencharacter")
public class ScreenCharacterModel extends BaseDbTableModel {
    //唯一值
    @DbTableParamConfigure(iskeyparm = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    protected Class getProviderClass() {
        return SetupProvider.class;
    }
}
