package cn.sun45.warbanner.framework.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2021/5/22
 */
public abstract class BaseFragment extends Fragment {
    //root view
    protected View mRoot;

    private boolean isViewInitiated;
    private boolean isDataInitiated;
    private boolean isActivityPause;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRoot = inflater.inflate(getContentViewId(), container, false);
        initData();
        initView();
        return mRoot;
    }

    protected abstract int getContentViewId();

    protected abstract void initData();

    protected abstract void initView();

    protected void logD(String msg) {
        Utils.logD(this.getClass().getName(), msg);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        isDataInitiated = false;
        prepareFetchData();
    }

    @Override
    public void onResume() {
        super.onResume();
        isActivityPause = false;
        if (getUserVisibleHint()) {
            onShow();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isActivityPause = true;
        if (getUserVisibleHint()) {
            onHide();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        prepareFetchData();
        if (isViewInitiated && !isActivityPause) {
            if (isVisibleToUser) {
                onShow();
            } else {
                onHide();
            }
        }
    }

    public boolean prepareFetchData() {
        if (getUserVisibleHint() && isViewInitiated && !isDataInitiated) {
            isDataInitiated = true;
            dataRequest();
            return true;
        }
        return false;
    }

    public boolean onDisplay() {
        return isViewInitiated && getUserVisibleHint() && isDataInitiated && !isActivityPause;
    }

    /**
     * 数据请求初始化
     */
    protected abstract void dataRequest();

    protected abstract void onShow();

    protected abstract void onHide();
}
