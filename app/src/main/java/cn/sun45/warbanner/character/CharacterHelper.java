package cn.sun45.warbanner.character;

import java.util.List;

import cn.sun45.warbanner.document.db.setup.ScreenCharacterModel;
import cn.sun45.warbanner.document.db.source.CharacterModel;
import cn.sun45.warbanner.framework.MyApplication;
import cn.sun45.warbanner.framework.document.db.DbHelper;
import cn.sun45.warbanner.user.UserManager;

/**
 * Created by Sun45 on 2021/6/18
 * 角色帮助类
 */
public class CharacterHelper {
    /**
     * 根据id查找角色信息
     *
     * @param id              id
     * @param characterModels 角色信息列表
     */
    public static CharacterModel findCharacterById(int id, List<CharacterModel> characterModels) {
        CharacterModel characterModel = null;
        if (characterModels != null && !characterModels.isEmpty()) {
            for (CharacterModel model : characterModels) {
                if (model.getId() == (id)) {
                    characterModel = model;
                    break;
                }
            }
        }
        return characterModel;
    }

    /**
     * 根据昵称查找角色信息
     *
     * @param nickname        昵称
     * @param characterModels 角色信息列表
     */
    public static CharacterModel findCharacterByNickname(String nickname, List<CharacterModel> characterModels) {
        CharacterModel characterModel = null;
        boolean find = false;
        if (characterModels != null && !characterModels.isEmpty()) {
            for (CharacterModel model : characterModels) {
                for (String str : model.getNicknames()) {
                    if (nickname.equals(str)) {
                        find = true;
                        break;
                    }
                }
                if (find) {
                    characterModel = model;
                    break;
                }
            }
        }
        return characterModel;
    }

    /**
     * 获取筛选角色列表
     */
    public static List<ScreenCharacterModel> getScreenCharacterList() {
        return DbHelper.query(MyApplication.application, ScreenCharacterModel.class, "userId", UserManager.getInstance().getCurrentUserId() + "");
    }

    /**
     * 角色筛选
     *
     * @param characterModel 角色信息
     * @param type           类型
     */
    public static void screenCharacter(CharacterModel characterModel, int type) {
        int userId = UserManager.getInstance().getCurrentUserId();
        int characterId = characterModel.getId();
        if (type == 0) {
            DbHelper.delete(MyApplication.application, ScreenCharacterModel.class, new String[]{"userId", "characterId"}, new String[]{userId + "", characterId + ""});
        } else {
            ScreenCharacterModel screenCharacterModel = null;
            List<ScreenCharacterModel> screenCharacterModels = DbHelper.query(MyApplication.application, ScreenCharacterModel.class, new String[]{"userId", "characterId"}, new String[]{userId + "", characterId + ""});
            if (screenCharacterModels != null && !screenCharacterModels.isEmpty()) {
                screenCharacterModel = screenCharacterModels.get(0);
            }
            if (screenCharacterModel == null) {
                screenCharacterModel = new ScreenCharacterModel();
                screenCharacterModel.setUserId(userId);
                screenCharacterModel.setCharacterId(characterId);
                screenCharacterModel.setType(type);
                DbHelper.insert(MyApplication.application, screenCharacterModel);
            } else {
                screenCharacterModel.setType(type);
                DbHelper.modify(MyApplication.application, screenCharacterModel, new String[]{"userId", "characterId"}, new String[]{userId + "", characterId + ""});
            }
        }
    }
}
