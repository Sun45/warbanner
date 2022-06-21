package cn.sun45.warbanner.document.preference;

import cn.sun45.warbanner.framework.document.preference.BasePreference;

/**
 * Created by Sun45 on 2022/6/10
 * 数据信息
 */
public class DataPreference extends BasePreference {
    //上次更新时间
    private long lastupdate;

    public long getLastupdate() {
        return load("lastupdate");
    }

    public void setLastupdate(long lastupdate) {
        save("lastupdate", lastupdate);
    }

    @Override
    public String getName() {
        return "data";
    }
}
