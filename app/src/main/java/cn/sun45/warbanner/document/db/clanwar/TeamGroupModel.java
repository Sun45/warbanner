package cn.sun45.warbanner.document.db.clanwar;

import cn.sun45.warbanner.framework.document.db.BaseDbTableModel;
import cn.sun45.warbanner.framework.document.db.annotation.DbTableConfigure;

/**
 * Created by Sun45 on 2021/5/23
 * 分刀信息数据模型
 */
@DbTableConfigure(tablename = "teamgroup")
public class TeamGroupModel extends BaseDbTableModel {
    @Override
    protected Class getProviderClass() {
        return ClanWarProvider.class;
    }
}
