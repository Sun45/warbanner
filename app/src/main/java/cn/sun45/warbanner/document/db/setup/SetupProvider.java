package cn.sun45.warbanner.document.db.setup;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.framework.document.db.BaseDbTableModel;
import cn.sun45.warbanner.framework.document.db.BaseProvider;
import cn.sun45.warbanner.framework.document.db.annotation.ProviderConfigure;

/**
 * Created by Sun45 on 2021/5/30
 * 设置数据库数据提供器
 */
@ProviderConfigure(version = 1, dbname = "setup")
public class SetupProvider extends BaseProvider {
    @Override
    protected List<BaseDbTableModel> getTableClassList() {
        List<BaseDbTableModel> list = new ArrayList<>();
        list.add(new UserModel());
        list.add(new ScreenCharacterModel());
        return list;
    }
}
