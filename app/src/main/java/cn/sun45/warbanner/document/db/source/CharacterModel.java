package cn.sun45.warbanner.document.db.source;

import java.util.List;

import cn.sun45.warbanner.framework.document.db.BaseDbTableModel;
import cn.sun45.warbanner.framework.document.db.annotation.DbTableConfigure;
import cn.sun45.warbanner.framework.document.db.annotation.DbTableParamConfigure;

/**
 * Created by Sun45 on 2021/5/23
 * 角色信息数据模型
 */
@DbTableConfigure(tablename = "character")
public class CharacterModel extends BaseDbTableModel {
    //唯一值
    @DbTableParamConfigure(iskeyparm = true)
    private int id;

    @DbTableParamConfigure
    private String name;

    @DbTableParamConfigure
    private String iconUrl;

    private List<String> nicknames;

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

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public List<String> getNicknames() {
        return nicknames;
    }

    public void setNicknames(List<String> nicknames) {
        this.nicknames = nicknames;
    }

    @Override
    protected Class getProviderClass() {
        return SourceProvider.class;
    }

    @Override
    public String toString() {
        return "id=" + id + ", name='" + name + '\'';
    }
}
