package cn.sun45.warbanner.document.preference;

import cn.sun45.warbanner.framework.document.preference.BasePreference;

/**
 * Created by Sun45 on 2021/5/27
 * 会战信息
 */
public class ClanwarPreference extends BasePreference {
    //上次更新时间
    private long lastupdate;

    //链接展示
    private boolean linkshow = true;

    //自动刀筛选
    private int teamlistautoscreen = 0;

    //阶段筛选
    private int teamlistshowtype = 0;

    public long getLastupdate() {
        return load("lastupdate");
    }

    public void setLastupdate(long lastupdate) {
        save("lastupdate", lastupdate);
    }

    public boolean isLinkshow() {
        return load("linkshow");
    }

    public void setLinkshow(boolean linkshow) {
        save("linkshow", linkshow);
    }

    public int getTeamlistautoscreen() {
        return load("teamlistautoscreen");
    }

    public void setTeamlistautoscreen(int teamlistautoscreen) {
        save("teamlistautoscreen", teamlistautoscreen);
    }

    public int getTeamlistshowtype() {
        return load("teamlistshowtype");
    }

    public void setTeamlistshowtype(int teamlistshowtype) {
        save("teamlistshowtype", teamlistshowtype);
    }

    @Override
    public String getName() {
        return "clanwar";
    }
}
