package cn.sun45.warbanner.document.statics;

/**
 * Created by Sun45 on 2022/6/9
 * 语言地区
 */
public enum Locale {
    ZH_CN(0, StaticHelper.ZH_CN_LANG, StaticHelper.ZH_CN_SERVERNAME, new int[][]{{1}, {2}, {3}, {4}}),
    ZH_TW(1, StaticHelper.ZH_TW_LANG, StaticHelper.ZH_TW_SERVERNAME, new int[][]{{1, 2}, {3}, {4, 5}});

    private int id;

    private String lang;

    private String serverName;

    private int[][] stage;

    private Locale(int id, String lang, String serverName, int[][] stage) {
        this.id = id;
        this.lang = lang;
        this.serverName = serverName;
        this.stage = stage;
    }

    public static Locale get(int id) {
        if (id == 0) {
            return ZH_CN;
        }
        return ZH_TW;
    }

    public String getLang() {
        return lang;
    }

    public String getServerName() {
        return serverName;
    }

    public int[][] getStage() {
        return stage;
    }
}
