package cn.sun45.warbanner.ui.views.userlist;

/**
 * Created by Sun45 on 2021/6/16
 * 用户列表数据模型
 */
public class UserListModel {
    private int id;
    private String name;

    public UserListModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
