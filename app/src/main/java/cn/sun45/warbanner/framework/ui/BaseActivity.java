package cn.sun45.warbanner.framework.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cn.sun45.warbanner.framework.MyApplication;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2021/5/19
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.setCurrentActivity(this);
        setContentView(getContentViewId());
        initData();
        refreshDB();
        initView();
        initNet();
    }

    @Override
    protected void onResume() {
        MyApplication.setCurrentActivity(this);
        super.onResume();
    }

    protected abstract int getContentViewId();

    protected void refreshDB() {
    }

    protected abstract void initData();

    protected abstract void initView();

    protected abstract void initNet();

    protected void logD(String msg) {
        Utils.logD(this.getClass().getName(), msg);
    }
}
