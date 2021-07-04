package cn.sun45.warbanner.ui.fragments.user;

import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.input.DialogInputExtKt;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.document.db.setup.UserModel;
import cn.sun45.warbanner.framework.ui.BaseActivity;
import cn.sun45.warbanner.framework.ui.BaseFragment;
import cn.sun45.warbanner.ui.views.userlist.UserList;
import cn.sun45.warbanner.ui.views.userlist.UserListListener;
import cn.sun45.warbanner.ui.views.userlist.UserListModel;
import cn.sun45.warbanner.user.UserManager;
import cn.sun45.warbanner.util.Utils;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;

/**
 * Created by Sun45 on 2021/6/16
 * 本地用户管理Fragment
 */
public class UserManagerFragment extends BaseFragment implements UserListListener {
    private UserManager userManager;

    private FloatingActionButton mFloatingButton;

    private UserList mUserList;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_usermanager;
    }

    @Override
    protected void initData() {
        setHasOptionsMenu(true);
        userManager = UserManager.getInstance();
    }

    @Override
    protected void initView() {
        MaterialToolbar toolbar = mRoot.findViewById(R.id.drop_toolbar);
        ((BaseActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigateUp();
            }
        });

        mUserList = mRoot.findViewById(R.id.userlist);
        mUserList.setListener(this);
        mFloatingButton = mRoot.findViewById(R.id.floating_button);
    }

    @Override
    protected void dataRequest() {
        mFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDialog dialog = new MaterialDialog(getContext(), MaterialDialog.getDEFAULT_BEHAVIOR());
                dialog.title(R.string.user_manager_add_dialog_title, null);
                DialogInputExtKt.input(dialog, null, R.string.user_manager_add_dialog_hint, null, null, InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS, null, true, false, new Function2<MaterialDialog, CharSequence, Unit>() {
                    @Override
                    public Unit invoke(MaterialDialog materialDialog, CharSequence charSequence) {
                        userManager.addUser(charSequence.toString());
                        showUserList();
                        return null;
                    }
                });
                dialog.positiveButton(R.string.user_manager_add_dialog_confirm, null, null);
                dialog.show();
            }
        });
        showUserList();
    }

    private void showUserList() {
        List<UserModel> userList = userManager.getUserList();
        List<UserListModel> list = new ArrayList<>();
        list.add(new UserListModel(0, ""));
        for (UserModel userModel : userList) {
            list.add(new UserListModel(userModel.getId(), userModel.getName()));
        }
        mUserList.setData(list);
    }

    @Override
    protected void onShow() {

    }

    @Override
    protected void onHide() {

    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.fragment_usermanager_drop_toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                if (userManager.isDefaultUser()) {
                    Utils.tip(getView(), R.string.user_manager_menu_delete_default_hint);
                } else {
                    Utils.tip(getView(), Utils.getStringWithPlaceHolder(R.string.user_manager_menu_delete_hint, userManager.getCurrentUserName()));
                    userManager.deleteUser(userManager.getCurrentUserId());
                    userManager.resetToDefaultUser();
                    showUserList();
                }
                break;
        }
        return true;
    }

    @Override
    public void select(UserListModel model) {
        UserManager.getInstance().setCurrentUserId(model.getId());
        mUserList.notifyDataSetChanged();
    }
}
