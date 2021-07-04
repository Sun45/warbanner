package cn.sun45.warbanner.document.preference;

import cn.sun45.warbanner.framework.document.preference.BasePreference;

/**
 * Created by Sun45 on 2021/6/16
 * 用户信息
 */
public class UserPreference extends BasePreference {
    private int userid;

    public int getUserid() {
        return load("userid");
    }

    public void setUserid(int userid) {
        save("userid", userid);
    }

    @Override
    public String getName() {
        return "user";
    }
}
