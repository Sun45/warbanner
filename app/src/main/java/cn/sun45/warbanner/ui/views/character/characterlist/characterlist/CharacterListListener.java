package cn.sun45.warbanner.ui.views.character.characterlist.characterlist;

import cn.sun45.warbanner.document.database.source.models.CharacterModel;
import cn.sun45.warbanner.document.statics.charactertype.CharacterScreenType;

/**
 * Created by Sun45 on 2021/5/30
 * 角色列表监听
 */
public interface CharacterListListener {
    void changeState(CharacterModel characterModel, CharacterScreenType characterScreenType);
}
