package cn.sun45.warbanner.ui.views.userlist;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.ui.views.teamgrouplist.TeamGroupListListener;
import cn.sun45.warbanner.user.UserManager;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2021/6/16
 * 用户列表Adapter
 */
public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.Holder> {
    private Context context;

    private UserListListener listener;

    private List<UserListModel> list;

    public UserListAdapter(Context context) {
        this.context = context;
    }

    public void setListener(UserListListener listener) {
        this.listener = listener;
    }

    public void setList(List<UserListModel> list) {
        this.list = list;
    }

    @NotNull
    @Override
    public Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.userlist_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UserListAdapter.Holder holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class Holder extends RecyclerView.ViewHolder {
        private CheckBox mName;

        private UserListModel model;

        public Holder(@NonNull @NotNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mName.isChecked()) {
                        if (listener != null) {
                            listener.select(model);
                        }
                    }
                }
            });
        }

        public void setData(UserListModel userListModel) {
            model = userListModel;
            int id = userListModel.getId();
            String name = userListModel.getName();
            if (id == UserManager.getInstance().getCurrentUserId()) {
                mName.setChecked(true);
            } else {
                mName.setChecked(false);
            }
            if (TextUtils.isEmpty(name)) {
                name = Utils.getString(R.string.default_user);
            }
            mName.setText(name);
        }
    }
}
