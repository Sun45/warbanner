package cn.sun45.warbanner.ui.views.characterlist;

import cn.sun45.warbanner.document.db.source.CharacterModel;

/**
 * Created by Sun45 on 2021/5/30
 * 角色列表监听
 */
public interface CharacterListListener {
    void changeState(boolean select, CharacterModel characterModel);
}
