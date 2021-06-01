package cn.sun45.warbanner.document.preference;

import cn.sun45.warbanner.framework.document.preference.BasePreference;

/**
 * Created by Sun45 on 2021/5/30
 * 设置信息
 */
public class SetupPreference extends BasePreference {
    //筛选阶段一
    private boolean stageonescreen = true;
    //筛选阶段二
    private boolean stagetwoscreen = true;
    //筛选阶段三
    private boolean stagethreescreen = true;

    //角色筛选生效
    private boolean characterscreenenable = false;

    //BOSS筛选
    private boolean bossonescreen = true;
    private boolean bosstwoscreen = true;
    private boolean bossthreescreen = true;
    private boolean bossfourscreen = false;
    private boolean bossfivescreen = false;

    //auto筛选
    private boolean notautoscreen = true;
    private boolean autoscreen = true;

    public boolean isStageonescreen() {
        return load("stageonescreen");
    }

    public void setStageonescreen(boolean stageonescreen) {
        save("stageonescreen", stageonescreen);
    }

    public boolean isStagetwoscreen() {
        return load("stagetwoscreen");
    }

    public void setStagetwoscreen(boolean stagetwoscreen) {
        save("stagetwoscreen", stagetwoscreen);
    }

    public boolean isStagethreescreen() {
        return load("stagethreescreen");
    }

    public void setStagethreescreen(boolean stagethreescreen) {
        save("stagethreescreen", stagethreescreen);
    }

    public boolean isCharacterscreenenable() {
        return load("characterscreenenable");
    }

    public void setCharacterscreenenable(boolean characterscreenenable) {
        save("characterscreenenable", characterscreenenable);
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

    public boolean isNotautoscreen() {
        return load("notautoscreen");
    }

    public void setNotautoscreen(boolean notautoscreen) {
        save("notautoscreen", notautoscreen);
    }

    public boolean isAutoscreen() {
        return load("autoscreen");
    }

    public void setAutoscreen(boolean autoscreen) {
        save("autoscreen", autoscreen);
    }

    @Override
    public String getName() {
        return "setup";
    }
}
