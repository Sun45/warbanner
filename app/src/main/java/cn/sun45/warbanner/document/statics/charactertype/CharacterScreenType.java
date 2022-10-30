package cn.sun45.warbanner.document.statics.charactertype;

import android.content.Context;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2022/10/27
 * 角色筛选类型
 */
public enum CharacterScreenType {
    TYPE_DEFAULT(0),//默认展示类型
    TYPE_YELLOW(1),//黄色展示类型
    TYPE_RED(2);//红色展示类型

    private int type;

    private CharacterScreenType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public int getBgColor(Context context) {
        int color = 0;
        switch (type) {
            case 0:
                color = Utils.getColor(R.color.theme_dark);
                break;
            case 1:
                color = Utils.getAttrColor(context, R.attr.colorSecondary);
                break;
            case 2:
                color = Utils.getAttrColor(context, R.attr.colorPrimary);
                break;
        }
        return color;
    }

    public int getTextColor() {
        int color = 0;
        switch (type) {
            case 0://默认
                color = Utils.getColor(R.color.white_50);
                break;
            case 1://TYPE_LACK
                color = Utils.getColor(R.color.black);
                break;
            case 2://TYPE_SKIP
                color = Utils.getColor(R.color.black);
                break;
        }
        return color;
    }

    public static CharacterScreenType get(int type) {
        CharacterScreenType characterScreenType = CharacterScreenType.TYPE_DEFAULT;
        switch (type) {
            case 0:
                characterScreenType = CharacterScreenType.TYPE_DEFAULT;
                break;
            case 1:
                characterScreenType = CharacterScreenType.TYPE_YELLOW;
                break;
            case 2:
                characterScreenType = CharacterScreenType.TYPE_RED;
                break;
        }
        return characterScreenType;
    }

    public CharacterScreenType next() {
        return get(type + 1);
    }
}
