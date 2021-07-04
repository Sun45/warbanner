package cn.sun45.warbanner.ui.fragments.record;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;

import com.google.android.material.appbar.MaterialToolbar;

import org.jetbrains.annotations.NotNull;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.framework.ui.BaseActivity;
import cn.sun45.warbanner.framework.ui.BaseFragment;
import cn.sun45.warbanner.util.FileUtil;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2021/6/4
 * 记录内容Fragment
 */
public class RecordTextFragment extends BaseFragment {
    private String path;
    private String content;

    private TextView mText;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_recordtext;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        path = bundle.getString("path");
        content = FileUtil.ReadFile(path);
        setHasOptionsMenu(true);
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

        mText = mRoot.findViewById(R.id.text);
    }

    @Override
    protected void dataRequest() {
        mText.setText(content);
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
        inflater.inflate(R.menu.fragment_record_text_drop_toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_copy:
                ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("Label", content);
                cm.setPrimaryClip(mClipData);
                Utils.tip(getView(), R.string.record_text_menu_copy_hint);
                break;
        }
        return true;
    }
}
