package cn.sun45.warbanner.ui.views.character.characterlist.characterlist;

import cn.sun45.warbanner.document.database.source.models.CharacterModel;
import cn.sun45.warbanner.document.statics.charactertype.CharacterScreenType;

/**
 * Created by Sun45 on 2021/5/30
 * 角色列表数据模型
 */
public class CharacterListModel {
    private CharacterModel characterModel;
    private CharacterScreenType characterScreenType;

    public CharacterListModel(CharacterModel characterModel, CharacterScreenType characterScreenType) {
        this.characterModel = characterModel;
        this.characterScreenType = characterScreenType;
    }

    public CharacterModel getCharacterModel() {
        return characterModel;
    }

    public void setCharacterModel(CharacterModel characterModel) {
        this.characterModel = characterModel;
    }

    public CharacterScreenType getCharacterScreenType() {
        return characterScreenType;
    }

    public void setCharacterScreenType(CharacterScreenType characterScreenType) {
        this.characterScreenType = characterScreenType;
    }
}
