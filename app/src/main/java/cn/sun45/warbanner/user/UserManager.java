package cn.sun45.warbanner.user;

import java.util.List;
import java.util.Random;
import java.util.logging.Handler;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.document.db.clanwar.TeamGroupCollectionModel;
import cn.sun45.warbanner.document.db.clanwar.TeamGroupScreenModel;
import cn.sun45.warbanner.document.db.setup.ScreenCharacterModel;
import cn.sun45.warbanner.document.db.setup.UserModel;
import cn.sun45.warbanner.document.preference.UserPreference;
import cn.sun45.warbanner.framework.MyApplication;
import cn.sun45.warbanner.framework.document.db.DbHelper;
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
        List<UserModel> list = DbHelper.query(MyApplication.application, UserModel.class);
        return list;
    }

    /**
     * 添加用户
     *
     * @param name
     */
    public void addUser(String name) {
        UserModel userModel = new UserModel();
        int id = 0;
        do {
            id = new Random().nextInt(100000);
        } while (DbHelper.query(MyApplication.application, UserModel.class, id + "") != null);
        userModel.setId(id);
        userModel.setName(name);
        DbHelper.insert(MyApplication.application, userModel);
    }

    /**
     * 删除用户
     *
     * @param userid
     */
    public void deleteUser(int userid) {
        DbHelper.delete(MyApplication.application, UserModel.class, userid + "");
        DbHelper.delete(MyApplication.application, ScreenCharacterModel.class, "userId", userid + "");
        DbHelper.delete(MyApplication.application, TeamGroupCollectionModel.class, "userId", userid + "");
        DbHelper.delete(MyApplication.application, TeamGroupScreenModel.class, "userId", userid + "");
    }

    /**
     * 获取当前用户id
     */
    public int getCurrentUserId() {
        return new UserPreference().getUserid();
    }

    /**
     * 获取当前用户名
     */
    public String getCurrentUserName() {
        int currentUserId = getCurrentUserId();
        if (currentUserId == 0) {
            return Utils.getString(R.string.default_user);
        } else {
            UserModel userModel = DbHelper.query(MyApplication.application, UserModel.class, currentUserId + "");
            return userModel.getName();
        }
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
        return new UserPreference().getUserid() == 0;
    }

    /**
     * 重置为默认用户
     */
    public void resetToDefaultUser() {
        setCurrentUserId(0);
    }
}
