package cn.sun45.warbanner.document.statics.charactertype;

/**
 * Created by Sun45 on 2022/10/27
 * 角色拥有类型
 */
public enum CharacterOwnType {
    TYPE_OWN(CharacterScreenType.TYPE_DEFAULT),//拥有角色
    TYPE_LACK(CharacterScreenType.TYPE_YELLOW),//缺失但可借
    TYPE_SKIP(CharacterScreenType.TYPE_RED);//缺失并不可借

    private CharacterScreenType screenType;

    private CharacterOwnType(CharacterScreenType screenType) {
        this.screenType = screenType;
    }

    public CharacterScreenType getScreenType() {
        return screenType;
    }
}
