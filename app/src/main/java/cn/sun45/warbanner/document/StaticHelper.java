package cn.sun45.warbanner.document;

/**
 * Created by Sun45 on 2021/5/23
 * 静态参数
 */
public class StaticHelper {
    //  App URL
    public static final String APP_RAW_BASE = "https://cdn.jsdelivr.net/gh/";
    public static final String APP_RAW = "https://cdn.jsdelivr.net/gh/Sun45/warbanner@master";
    public static final String APP_UPDATE_LOG = APP_RAW + "/update_log.json";
    public static final String APP_PACKAGE = "https://cdn.jsdelivr.net/gh/Sun45/warbanner@master/apk/warbanner-release.apk";
    public static final String APK_NAME = "update.apk";

    //  API URL
    public static final String API_URL = "https://redive.estertion.win";

    //  database string for use
    public static String DB_FILE_NAME_COMPRESSED = "redive_cn.db.br";
    public static String DB_FILE_NAME = "redive_cn.db";
    public static String LATEST_VERSION_URL = API_URL + "/last_version_cn.json";
    public static String DB_FILE_URL = API_URL + "/db/" + DB_FILE_NAME_COMPRESSED;

    //  Resource URL
    public static final String IMAGE_URL = API_URL + "/card/full/%d.webp@h300";
    public static final String ICON_URL = API_URL + "/icon/unit/%d.webp";
    public static final String SHADOW_ICON_URL = API_URL + "/icon/unit_shadow/%d.webp";
    public static final String SKILL_ICON_URL = API_URL + "/icon/skill/%d.webp";
    public static final String EQUIPMENT_ICON_URL = API_URL + "/icon/equipment/%d.webp";
    public static final String ITEM_ICON_URL = API_URL + "/icon/item/%d.webp";
    public static final String UNKNOWN_ICON = API_URL + "/icon/equipment/999999.webp";

    //ClanWar
    public static final String CLANWAR_OWNER="acaly";
    public static final String CLANWAR_REPOSITORY="PcrGuildLibrary";
    public static final String CLANWAR_PATH_BASE = "data/";
    public static final String CLANWAR_PATH_ONE="/A.json";
    public static final String CLANWAR_PATH_TWO="/B.json";
    public static final String CLANWAR_PATH_THREE="/C.json";

    //昵称数据获取地址
    public static final String NICKNAME_URL = "https://github.com/pcrbot/pcr-nickname/blob/master/nicknames_zh-cn.csv";
}
