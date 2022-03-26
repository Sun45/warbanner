package cn.sun45.warbanner.document.db.clanwar;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.framework.document.db.BaseDbTableModel;
import cn.sun45.warbanner.framework.document.db.BaseProvider;
import cn.sun45.warbanner.framework.document.db.annotation.ProviderConfigure;

/**
 * Created by Sun45 on 2021/5/23
 * 会战数据库数据提供器
 */
@ProviderConfigure(version = 5, dbname = "clanwar")
public class ClanWarProvider extends BaseProvider {
    @Override
    protected List<BaseDbTableModel> getTableClassList() {
        List<BaseDbTableModel> list = new ArrayList<>();
        list.add(new TeamModel());
        list.add(new TeamCustomizeModel());
        list.add(new TeamGroupCollectionModel());
        list.add(new TeamGroupScreenModel());
        return list;
    }
}
