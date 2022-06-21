package cn.sun45.warbanner.ui.views.characterlist;

import cn.sun45.warbanner.document.database.source.models.CharacterModel;

/**
 * Created by Sun45 on 2021/5/30
 * 角色列表数据模型
 */
public class CharacterListModel {
    private CharacterModel characterModel;
    private int type;

    public CharacterListModel(CharacterModel characterModel, int type) {
        this.characterModel = characterModel;
        this.type = type;
    }

    public CharacterModel getCharacterModel() {
        return characterModel;
    }

    public void setCharacterModel(CharacterModel characterModel) {
        this.characterModel = characterModel;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
