package cn.sun45.warbanner.document.statics;

/**
 * Created by Sun45 on 2022/6/9
 * 阵容类型
 */
public enum TeamType {
    AUTO(1), NORMAL(2), FINISH(3);

    private int type;

    private TeamType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
