package cn.sun45.warbanner.document.preference;

import cn.sun45.warbanner.framework.document.preference.BasePreference;

/**
 * Created by Sun45 on 2021/5/27
 * 会战信息
 */
public class ClanwarPreference extends BasePreference {
    //上次更新时间
    private long lastupdate;

    //作业下载方式
    private int way = 0;

    //链接展示
    private boolean linkshow = true;

    //阶段筛选 0:全部,1:一阶段,2:二阶段,3:三阶段,4:四阶段
    private int teamliststage = 0;

    //BOSS筛选 0:全部,1:一王,2:二王,3:三王,4:四王,5:五王
    private int teamlistboss = 0;

    //刀型筛选 0:不限,1:AUTO,2:非AUTO,3:尾刀
    private int teamlisttype = 0;

    public long getLastupdate() {
        return load("lastupdate");
    }

    public void setLastupdate(long lastupdate) {
        save("lastupdate", lastupdate);
    }

    public int getWay() {
        return load("way");
    }

    public void setWay(int way) {
        save("way", way);
    }

    public boolean isLinkshow() {
        return load("linkshow");
    }

    public void setLinkshow(boolean linkshow) {
        save("linkshow", linkshow);
    }

    public int getTeamliststage() {
        return load("teamliststage");
    }

    public void setTeamliststage(int teamliststage) {
        save("teamliststage", teamliststage);
    }

    public int getTeamlistboss() {
        return load("teamlistboss");
    }

    public void setTeamlistboss(int teamlistboss) {
        save("teamlistboss", teamlistboss);
    }

    public int getTeamlisttype() {
        return load("teamlisttype");
    }

    public void setTeamlisttype(int teamlisttype) {
        save("teamlisttype", teamlisttype);
    }

    @Override
    public String getName() {
        return "clanwar";
    }
}
