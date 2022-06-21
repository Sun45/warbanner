package cn.sun45.warbanner.document.preference;

import cn.sun45.warbanner.document.statics.Locale;
import cn.sun45.warbanner.framework.document.preference.BasePreference;

/**
 * Created by Sun45 on 2021/5/30
 * 设置信息
 */
public class SetupPreference extends BasePreference {
    //连点间隔
    private int tapinterval = 10;
    //自动连点启用
    private boolean autoclick = false;

    //角色筛选生效
    private boolean characterscreenenable = false;

    //服务器
    private int server;

    //自动更新开启
    private boolean autoupdate = true;

    //链接打开方式
    private int linkopentype;

    public int getTapinterval() {
        return load("tapinterval");
    }

    public void setTapinterval(int tapinterval) {
        save("tapinterval", tapinterval);
    }

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

    public int getServer() {
        return load("server");
    }

    public void setServer(int server) {
        save("server", server);
    }

    public boolean isAutoupdate() {
        return load("autoupdate");
    }

    public void setAutoupdate(boolean autoupdate) {
        save("autoupdate", autoupdate);
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
