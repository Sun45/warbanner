package cn.sun45.warbanner.document.statics.charactertype;

/**
 * Created by Sun45 on 2022/10/27
 * 角色使用类型
 */
public enum CharacterUseType {
    TYPE_USEABLE(CharacterScreenType.TYPE_DEFAULT),//未使用
    TYPE_USING(CharacterScreenType.TYPE_YELLOW),//已进行实战
    TYPE_USED(CharacterScreenType.TYPE_RED);//使用限制

    private CharacterScreenType screenType;

    private CharacterUseType(CharacterScreenType screenType) {
        this.screenType = screenType;
    }

    public CharacterScreenType getScreenType() {
        return screenType;
    }

    public static CharacterUseType get(int type) {
        CharacterScreenType characterScreenType = CharacterScreenType.get(type);
        CharacterUseType characterUseType = TYPE_USEABLE;
        switch (characterScreenType) {
            case TYPE_DEFAULT:
                characterUseType = TYPE_USEABLE;
                break;
            case TYPE_YELLOW:
                characterUseType = TYPE_USING;
                break;
            case TYPE_RED:
                characterUseType = TYPE_USED;
                break;
        }
        return characterUseType;
    }
}
