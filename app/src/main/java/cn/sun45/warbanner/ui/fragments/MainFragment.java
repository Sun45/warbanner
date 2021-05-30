package cn.sun45.warbanner.ui.fragments;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.framework.ui.BaseFragment;

/**
 * Created by Sun45 on 2021/5/22
 * 首页Fragment
 */
public class MainFragment extends BaseFragment {
    private BottomNavigationView mBottomNavigationView;

    private int currentIndex;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {
        logD("initView");
        mBottomNavigationView = mRoot.findViewById(R.id.bottom_nav);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {
                int index = -1;
                switch (item.getItemId()) {
                    case R.id.collection:
                        index = 0;
                        break;
                    case R.id.teamlist:
                        index = 1;
                        break;
                    case R.id.menu:
                        index = 2;
                        break;
                    default:
                        break;
                }
                switchShow(index);
                return true;
            }
        });
        switchShow(currentIndex);
    }

    @Override
    protected void dataRequest() {

    }

    /**
     * 切换Fragment展示
     *
     * @param index 0~2
     */
    private void switchShow(int index) {
        logD("switchShow index:" + index);
        currentIndex = index;
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment oldfragment = fragmentManager.findFragmentById(R.id.frame_container);
        if (oldfragment != null) {
            fragmentTransaction.detach(oldfragment);
        }
        Fragment newfragment = fragmentManager.findFragmentByTag(index + "");
        if (newfragment == null) {
            switch (index) {
                case 0:
                    newfragment = new CollectionFragment();
                    break;
                case 1:
                    newfragment = new TeamListFragment();
                    break;
                case 2:
                    newfragment = new MenuFragment();
                    break;
                default:
                    break;
            }
            fragmentTransaction.add(R.id.frame_container, newfragment, index + "");
        } else {
            fragmentTransaction.attach(newfragment);
        }
        fragmentTransaction.setPrimaryNavigationFragment(newfragment).setReorderingAllowed(true).commit();
    }

    @Override
    protected void onShow() {
        logD("onShow");
    }

    @Override
    protected void onHide() {
        logD("onHide");
    }
}
