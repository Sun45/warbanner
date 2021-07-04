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
    public static final int TYPE_LACK = 1;
    public static final int TYPE_SKIP = 2;
    @DbTableParamConfigure
    private int userId;

    @DbTableParamConfigure
    private int characterId;

    @DbTableParamConfigure
    private int type;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    protected Class getProviderClass() {
        return SetupProvider.class;
    }
}
