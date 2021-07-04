package cn.sun45.warbanner.ui.fragments.record;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.framework.record.ErrorRecordManager;
import cn.sun45.warbanner.framework.ui.BaseActivity;
import cn.sun45.warbanner.framework.ui.BaseFragment;
import cn.sun45.warbanner.ui.views.recordlist.RecordList;
import cn.sun45.warbanner.ui.views.recordlist.RecordListListener;
import cn.sun45.warbanner.ui.views.recordlist.RecordListModel;
import cn.sun45.warbanner.util.FileUtil;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * Created by Sun45 on 2021/6/4
 * 记录Fragment
 */
public class RecordFragment extends BaseFragment implements RecordListListener {
    private String errorRecordPath;

    private RecordList mRecordList;
    private FloatingActionButton mFloatingButton;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_record;
    }

    @Override
    protected void initData() {
        errorRecordPath = ErrorRecordManager.getInstance().getDirPath();
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

        mRecordList = mRoot.findViewById(R.id.recordlist);
        mRecordList.setListener(this);
        mFloatingButton = mRoot.findViewById(R.id.floating_button);
    }

    @Override
    protected void dataRequest() {
        List<RecordListModel> list = new ArrayList<>();
        String[] filenames = new File(errorRecordPath).list();
        for (String filename : filenames) {
            RecordListModel recordListModel = new RecordListModel();
            recordListModel.setName(filename);
            recordListModel.setPath(errorRecordPath + File.separator + filename);
            list.add(recordListModel);
        }
        mRecordList.setData(list);
        mFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog(getContext(), MaterialDialog.getDEFAULT_BEHAVIOR())
                        .title(R.string.record_clean_dialog_title, null)
                        .message(R.string.record_clean_dialog_text, null, null)
                        .cancelOnTouchOutside(true)
                        .positiveButton(R.string.record_clean_dialog_confirm, null, new Function1<MaterialDialog, Unit>() {
                            @Override
                            public Unit invoke(MaterialDialog materialDialog) {
                                FileUtil.deleteDirectory(new File(errorRecordPath));
                                mRecordList.setData(null);
                                return null;
                            }
                        })
                        .negativeButton(R.string.record_clean_dialog_cancel, null, null)
                        .show();
            }
        });
    }

    @Override
    protected void onShow() {

    }

    @Override
    protected void onHide() {

    }

    @Override
    public void click(RecordListModel recordListModel) {
        NavController controller = Navigation.findNavController(getView());
        Bundle bundle = new Bundle();
        bundle.putSerializable("path", recordListModel.getPath());
        controller.navigate(R.id.action_nav_record_to_nav_recordtext, bundle);
    }
}
