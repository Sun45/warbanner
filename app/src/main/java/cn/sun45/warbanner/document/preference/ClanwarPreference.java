package cn.sun45.warbanner.document.preference;

import cn.sun45.warbanner.framework.document.preference.BasePreference;

/**
 * Created by Sun45 on 2021/5/27
 * 会战信息
 */
public class ClanwarPreference extends BasePreference {
    //上次更新时间
    private long lastupdate;

    private int teamlistshowtype;

    public long getLastupdate() {
        return load("lastupdate");
    }

    public void setLastupdate(long lastupdate) {
        save("lastupdate", lastupdate);
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
