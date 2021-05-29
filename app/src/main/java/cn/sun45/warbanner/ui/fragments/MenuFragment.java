package cn.sun45.warbanner.ui.fragments;

import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.framework.ui.BaseFragment;

/**
 * Created by Sun45 on 2021/5/22
 * 菜单Fragment
 */
public class MenuFragment extends BaseFragment {
    private View mContiner;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_menu;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {
        mContiner=mRoot.findViewById(R.id.continer);


        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.continer,new MenuPreferecefragment()).commit();
    }

    @Override
    protected void dataRequest() {

    }

    @Override
    protected void onShow() {

    }

    @Override
    protected void onHide() {

    }
}
