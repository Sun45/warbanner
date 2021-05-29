package cn.sun45.warbanner.document.preference;

import cn.sun45.warbanner.framework.document.preference.BasePreference;

/**
 * Created by Sun45 on 2021/5/22
 * 应用信息
 */
public class AppPreference extends BasePreference {
    //异常退出
    private boolean abnormal_exit;

    @Override
    public String getName() {
        return "app";
    }

    public boolean isAbnormal_exit() {
        return load("abnormal_exit");
    }

    public void setAbnormal_exit(boolean abnormal_exit) {
        save("abnormal_exit", abnormal_exit);
    }
}
