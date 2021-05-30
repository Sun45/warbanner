package cn.sun45.warbanner.ui.views.characterlist;

import cn.sun45.warbanner.document.db.source.CharacterModel;

/**
 * Created by Sun45 on 2021/5/30
 * 角色列表数据模型
 */
public class CharacterListModel {
    private CharacterModel characterModel;
    private boolean select;

    public CharacterListModel(CharacterModel characterModel, boolean select) {
        this.characterModel = characterModel;
        this.select = select;
    }

    public CharacterModel getCharacterModel() {
        return characterModel;
    }

    public void setCharacterModel(CharacterModel characterModel) {
        this.characterModel = characterModel;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
