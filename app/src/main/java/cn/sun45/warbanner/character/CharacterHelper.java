package cn.sun45.warbanner.character;

import java.util.List;

import cn.sun45.warbanner.document.database.setup.SetupDataBase;
import cn.sun45.warbanner.document.database.setup.models.ScreenCharacterModel;
import cn.sun45.warbanner.document.database.setup.models.TeamGroupScreenUsedCharacterModel;
import cn.sun45.warbanner.document.database.source.models.CharacterModel;
import cn.sun45.warbanner.document.statics.charactertype.CharacterOwnType;
import cn.sun45.warbanner.document.statics.charactertype.CharacterScreenType;
import cn.sun45.warbanner.document.statics.charactertype.CharacterUseType;
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
     * 获取筛选角色列表
     */
    public static List<ScreenCharacterModel> getScreenCharacterList() {
        return SetupDataBase.getInstance().setupDao().queryAllScreenCharacter(UserManager.getInstance().getCurrentUserId());
    }

    /**
     * 角色拥有筛选
     *
     * @param characterModel      角色信息
     * @param characterScreenType 角色筛选类型
     */
    public static void saveCharacterOwnType(CharacterModel characterModel, CharacterScreenType characterScreenType) {
        int userId = UserManager.getInstance().getCurrentUserId();
        int characterId = characterModel.getId();
        int type = characterScreenType.getType();
        if (type == CharacterOwnType.TYPE_OWN.getScreenType().getType()) {
            SetupDataBase.getInstance().setupDao().deleteScreenCharacter(userId, characterId);
        } else {
            ScreenCharacterModel screenCharacterModel = SetupDataBase.getInstance().setupDao().queryScreenCharacter(userId, characterId);
            if (screenCharacterModel == null) {
                screenCharacterModel = new ScreenCharacterModel();
                screenCharacterModel.setUserId(userId);
                screenCharacterModel.setCharacterId(characterId);
                screenCharacterModel.setType(type);
                SetupDataBase.getInstance().setupDao().insertScreenCharacter(screenCharacterModel);
            } else {
                screenCharacterModel.setType(type);
                SetupDataBase.getInstance().setupDao().updateScreenCharacter(screenCharacterModel);
            }
        }
    }

    /**
     * 获取已用角色列表
     */
    public static List<TeamGroupScreenUsedCharacterModel> getUsedCharacterList() {
        return SetupDataBase.getInstance().setupDao().queryAllTeamGroupScreenUsedCharacter(UserManager.getInstance().getCurrentUserId());
    }

    /**
     * 角色使用筛选
     *
     * @param characterModel      角色信息
     * @param characterScreenType 角色筛选类型
     */
    public static void saveCharacterUseType(CharacterModel characterModel, CharacterScreenType characterScreenType) {
        int userId = UserManager.getInstance().getCurrentUserId();
        int characterId = characterModel.getId();
        int type = characterScreenType.getType();
        if (type == CharacterUseType.TYPE_USEABLE.getScreenType().getType()) {
            SetupDataBase.getInstance().setupDao().deleteTeamGroupScreenUsedCharacter(userId, characterId);
        } else {
            TeamGroupScreenUsedCharacterModel teamGroupScreenUsedCharacterModel =
                    SetupDataBase.getInstance().setupDao().queryTeamGroupScreenUsedCharacter(userId, characterId);
            if (teamGroupScreenUsedCharacterModel == null) {
                teamGroupScreenUsedCharacterModel = new TeamGroupScreenUsedCharacterModel();
                teamGroupScreenUsedCharacterModel.setUserId(userId);
                teamGroupScreenUsedCharacterModel.setCharacterId(characterId);
                teamGroupScreenUsedCharacterModel.setType(type);
                SetupDataBase.getInstance().setupDao().insertTeamGroupScreenUsedCharacter(teamGroupScreenUsedCharacterModel);
            } else {
                teamGroupScreenUsedCharacterModel.setType(type);
                SetupDataBase.getInstance().setupDao().updateTeamGroupScreenUsedCharacter(teamGroupScreenUsedCharacterModel);
            }
        }
    }

    /**
     * 清空已用角色列表
     */
    public static void clearUsedCharacterList() {
        SetupDataBase.getInstance().setupDao().deleteTeamGroupScreenUsedCharacter(UserManager.getInstance().getCurrentUserId());
    }
}
