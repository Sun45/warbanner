package cn.sun45.warbanner.document.statics;

/**
 * Created by Sun45 on 2022/6/9
 * 筛选角色
 */
public enum ScreenCharacter {
    TYPE_LACK(1), TYPE_SKIP(2);

    private int type;

    private ScreenCharacter(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
