package cn.sun45.warbanner.document.db.setup;

import cn.sun45.warbanner.framework.document.db.BaseDbTableModel;
import cn.sun45.warbanner.framework.document.db.annotation.DbTableConfigure;
import cn.sun45.warbanner.framework.document.db.annotation.DbTableParamConfigure;

/**
 * Created by Sun45 on 2021/6/16
 * 本地用户信息数据模型
 */
@DbTableConfigure(tablename = "user")
public class UserModel extends BaseDbTableModel {
    //唯一值
    @DbTableParamConfigure(iskeyparm = true)
    private int id;

    @DbTableParamConfigure
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected Class getProviderClass() {
        return SetupProvider.class;
    }
}
