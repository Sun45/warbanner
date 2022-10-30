package cn.sun45.warbanner.ui.views.character.characterlist;

import cn.sun45.warbanner.document.database.source.models.CharacterModel;
import cn.sun45.warbanner.document.statics.charactertype.CharacterScreenType;

/**
 * Created by Sun45 on 2022/10/27
 * 角色列表容器监听
 */
public interface CharacterListLayListener {
    void changeState(CharacterModel characterModel, CharacterScreenType characterScreenType);
}
