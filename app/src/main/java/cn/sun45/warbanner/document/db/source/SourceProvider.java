package cn.sun45.warbanner.document.db.source;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.framework.document.db.BaseDbTableModel;
import cn.sun45.warbanner.framework.document.db.BaseProvider;
import cn.sun45.warbanner.framework.document.db.annotation.ProviderConfigure;

/**
 * Created by Sun45 on 2021/5/23
 * 数据源数据库数据提供器
 */
@ProviderConfigure(version = 2, dbname = "source")
public class SourceProvider extends BaseProvider {
    @Override
    protected List<BaseDbTableModel> getTableClassList() {
        List<BaseDbTableModel> list = new ArrayList<>();
        list.add(new CharacterModel());
        list.add(new ClanWarModel());
        return list;
    }
}