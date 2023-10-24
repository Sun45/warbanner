package cn.sun45.warbanner.ui.views.userlist;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import cn.sun45.warbanner.framework.ui.BaseVerticalRecyclerView;

/**
 * Created by Sun45 on 2021/6/16
 * 用户列表
 */
public class UserList extends BaseVerticalRecyclerView {
    private UserListAdapter adapter;

    public UserList(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new UserListAdapter(getContext());
        setAdapter(adapter);
    }

    public void setListener(UserListListener listener) {
        adapter.setListener(listener);
    }

    public void setData(List<UserListModel> list) {
        adapter.setList(list);
        adapter.notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }
}
