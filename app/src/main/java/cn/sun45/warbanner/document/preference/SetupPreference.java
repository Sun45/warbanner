package cn.sun45.warbanner.document.preference;

import cn.sun45.warbanner.framework.document.preference.BasePreference;

/**
 * Created by Sun45 on 2021/5/30
 * 设置信息
 */
public class SetupPreference extends BasePreference {
    //自动连点启用
    private boolean autoclick = false;

    //角色筛选生效
    private boolean characterscreenenable = false;

    //自动更新开启
    private boolean autoupdate = true;

    //BOSS筛选
    private boolean bossonescreen = true;
    private boolean bosstwoscreen = true;
    private boolean bossthreescreen = true;
    private boolean bossfourscreen = false;
    private boolean bossfivescreen = false;

    //auto筛选
    private boolean useautoscreen = true;
    private int autocount = 0;

    //链接打开方式
    private int linkopentype;

    public boolean isAutoclick() {
        return load("autoclick");
    }

    public void setAutoclick(boolean autoclick) {
        save("autoclick", autoclick);
    }

    public boolean isCharacterscreenenable() {
        return load("characterscreenenable");
    }

    public void setCharacterscreenenable(boolean characterscreenenable) {
        save("characterscreenenable", characterscreenenable);
    }

    public boolean isAutoupdate() {
        return load("autoupdate");
    }

    public void setAutoupdate(boolean autoupdate) {
        save("autoupdate", autoupdate);
    }

    public boolean isBossonescreen() {
        return load("bossonescreen");
    }

    public void setBossonescreen(boolean bossonescreen) {
        save("bossonescreen", bossonescreen);
    }

    public boolean isBosstwoscreen() {
        return load("bosstwoscreen");
    }

    public void setBosstwoscreen(boolean bosstwoscreen) {
        save("bosstwoscreen", bosstwoscreen);
    }

    public boolean isBossthreescreen() {
        return load("bossthreescreen");
    }

    public void setBossthreescreen(boolean bossthreescreen) {
        save("bossthreescreen", bossthreescreen);
    }

    public boolean isBossfourscreen() {
        return load("bossfourscreen");
    }

    public void setBossfourscreen(boolean bossfourscreen) {
        save("bossfourscreen", bossfourscreen);
    }

    public boolean isBossfivescreen() {
        return load("bossfivescreen");
    }

    public void setBossfivescreen(boolean bossfivescreen) {
        save("bossfivescreen", bossfivescreen);
    }

    public boolean isUseautoscreen() {
        return load("useautoscreen");
    }

    public void setUseautoscreen(boolean useautoscreen) {
        save("useautoscreen", useautoscreen);
    }

    public int getAutocount() {
        return load("autocount");
    }

    public void setAutocount(int autocount) {
        save("autocount", autocount);
    }

    public int getLinkopentype() {
        return load("linkopentype");
    }

    public void setLinkopentype(int linkopentype) {
        save("linkopentype", linkopentype);
    }

    @Override
    public String getName() {
        return "setup";
    }
}
