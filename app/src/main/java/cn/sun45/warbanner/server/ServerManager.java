package cn.sun45.warbanner.server;

import androidx.annotation.StringRes;

import cn.sun45.warbanner.datamanager.data.DataManager;
import cn.sun45.warbanner.document.preference.SetupPreference;
import cn.sun45.warbanner.document.statics.Locale;

/**
 * Created by Sun45 on 2022/6/11
 * 服务管理
 */
public class ServerManager {
    //单例对象
    private static ServerManager instance;

    public static ServerManager getInstance() {
        if (instance == null) {
            synchronized (ServerManager.class) {
                if (instance == null) {
                    instance = new ServerManager();
                }
            }
        }
        return instance;
    }

    private IActivityCallBack iActivityCallBack;

    public void setiActivityCallBack(IActivityCallBack iActivityCallBack) {
        this.iActivityCallBack = iActivityCallBack;
    }

    public Locale getLocal() {
        return Locale.get(new SetupPreference().getServer());
    }

    public String getLang() {
        return getLocal().getLang();
    }

    public String getServerName() {
        return getLocal().getServerName();
    }

    public int getCurrentServer() {
        return new SetupPreference().getServer();
    }

    public void setCurrentServer(int position) {
        int currentServer = getCurrentServer();
        if (currentServer != position) {
            new SetupPreference().setServer(position);
            if (iActivityCallBack != null) {
                iActivityCallBack.serverUpdate();
            }
        }
    }

    public interface IActivityCallBack {
        void showSnackBar(@StringRes int messageRes);

        void serverUpdate();
    }
}
