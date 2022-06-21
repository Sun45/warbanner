package cn.sun45.warbanner.logic.caimogu;

import java.util.List;

import cn.sun45.warbanner.document.database.source.models.BossModel;
import cn.sun45.warbanner.document.database.source.models.CharacterModel;

/**
 * Created by Sun45 on 2022/6/9
 * 踩蘑菇基础数据
 */
public class CaimoguBaseData {
    private List<CharacterModel> characterModels;

    private List<BossModel> bossModels;

    public List<CharacterModel> getCharacterModels() {
        return characterModels;
    }

    public void setCharacterModels(List<CharacterModel> characterModels) {
        this.characterModels = characterModels;
    }

    public List<BossModel> getBossModels() {
        return bossModels;
    }

    public void setBossModels(List<BossModel> bossModels) {
        this.bossModels = bossModels;
    }

    public boolean isEmpty() {
        return characterModels == null || characterModels.isEmpty() || bossModels == null || bossModels.isEmpty();
    }
}
