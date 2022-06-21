package cn.sun45.warbanner.user;

import java.util.List;
import java.util.Random;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.document.database.setup.SetupDataBase;
import cn.sun45.warbanner.document.database.setup.models.UserModel;
import cn.sun45.warbanner.document.preference.UserPreference;
import cn.sun45.warbanner.framework.MyApplication;
import cn.sun45.warbanner.server.ServerManager;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2021/6/16
 * 本地用户管理
 */
public class UserManager {
    //单例对象
    private static UserManager instance;

    public static UserManager getInstance() {
        if (instance == null) {
            synchronized (UserManager.class) {
                if (instance == null) {
                    instance = new UserManager();
                }
            }
        }
        return instance;
    }

    /**
     * 获取本地用户列表
     */
    public List<UserModel> getUserList() {
        List<UserModel> list = SetupDataBase.getInstance().setupDao().queryAllUser(ServerManager.getInstance().getLang());
        return list;
    }

    /**
     * 添加用户
     *
     * @param name
     */
    public void addUser(String name) {
        UserModel userModel = new UserModel();
        userModel.setLang(ServerManager.getInstance().getLang());
        userModel.setName(name);
        SetupDataBase.getInstance().setupDao().insertUser(userModel);
    }

    /**
     * 删除用户及关联信息
     *
     * @param userid
     */
    public void deleteUser(int userid) {
        SetupDataBase.getInstance().setupDao().deleteUser(userid);
        SetupDataBase.getInstance().setupDao().deleteScreenCharacter(userid);
        SetupDataBase.getInstance().setupDao().deleteTeamListShow(userid);
        SetupDataBase.getInstance().setupDao().deleteTeamGroupScreen(userid);
        SetupDataBase.getInstance().setupDao().deleteTeamGroupCollection(userid);
    }

    /**
     * 获取默认用户
     */
    public UserModel GetDefaultUser() {
        String lang = ServerManager.getInstance().getLang();
        UserModel userModel = SetupDataBase.getInstance().setupDao().queryDefaultUser(lang);
        if (userModel == null) {
            userModel = new UserModel();
            userModel.setLang(lang);
            userModel.setDefaultUser(true);
            userModel.setName(Utils.getString(R.string.default_user));
            SetupDataBase.getInstance().setupDao().insertUser(userModel);
            userModel = SetupDataBase.getInstance().setupDao().queryDefaultUser(lang);
        }
        return userModel;
    }

    /**
     * 获取当前用户id
     */
    public int getCurrentUserId() {
        int userId = new UserPreference().getUserid();
        //sp存0代表默认用户
        if (userId == 0) {
            userId = GetDefaultUser().getId();
        }
        return userId;
    }

    /**
     * 获取当前用户
     */
    public UserModel getCurrentUser() {
        int currentUserId = getCurrentUserId();
        UserModel userModel = SetupDataBase.getInstance().setupDao().queryUser(currentUserId);
        if (userModel == null) {
            resetToDefaultUser();
            userModel = SetupDataBase.getInstance().setupDao().queryUser(getCurrentUserId());
        }
        return userModel;
    }

    /**
     * 获取当前用户名
     */
    public String getCurrentUserName() {
        return getCurrentUser().getName();
    }

    /**
     * 设置当前用户id
     */
    public void setCurrentUserId(int userid) {
        new UserPreference().setUserid(userid);
    }

    /**
     * 当前为默认用户
     *
     * @return
     */
    public boolean isDefaultUser() {
        return getCurrentUser().isDefaultUser();
    }

    /**
     * 重置为默认用户
     */
    public void resetToDefaultUser() {
        //sp存0代表默认用户
        setCurrentUserId(0);
    }
}
