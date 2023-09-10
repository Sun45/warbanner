package cn.sun45.warbanner.ui.fragments;

import android.view.View;

import androidx.navigation.Navigation;

import com.google.android.material.appbar.MaterialToolbar;

import cn.sun45.bcrloglib.BcrLog;
import cn.sun45.bcrloglib.BossConfig;
import cn.sun45.warbanner.R;
import cn.sun45.warbanner.document.preference.BossConfigPreference;
import cn.sun45.warbanner.framework.ui.BaseActivity;
import cn.sun45.warbanner.framework.ui.BaseFragment;

/**
 * Created by Sun45 on 2023/9/10
 */
public class BossDataFragment extends BaseFragment {
    private BcrLog mBcrLog;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_bossdata;
    }

    @Override
    protected void initData() {

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

        mBcrLog = mRoot.findViewById(R.id.bcrlog);
        BossConfigPreference preference = new BossConfigPreference();
        BossConfig bossConfig = new BossConfig();
        bossConfig.setBossOneStageOneHp(preference.getBossOneStageOneHp());
        bossConfig.setBossOneStageTwoHp(preference.getBossOneStageTwoHp());
        bossConfig.setBossOneStageThreeHp(preference.getBossOneStageThreeHp());
        bossConfig.setBossOneStageFourHp(preference.getBossOneStageFourHp());
        bossConfig.setBossOneStageFiveHp(preference.getBossOneStageFiveHp());
        bossConfig.setBossTwoStageOneHp(preference.getBossTwoStageOneHp());
        bossConfig.setBossTwoStageTwoHp(preference.getBossTwoStageTwoHp());
        bossConfig.setBossTwoStageThreeHp(preference.getBossTwoStageThreeHp());
        bossConfig.setBossTwoStageFourHp(preference.getBossTwoStageFourHp());
        bossConfig.setBossTwoStageFiveHp(preference.getBossTwoStageFiveHp());
        bossConfig.setBossThreeStageOneHp(preference.getBossThreeStageOneHp());
        bossConfig.setBossThreeStageTwoHp(preference.getBossThreeStageTwoHp());
        bossConfig.setBossThreeStageThreeHp(preference.getBossThreeStageThreeHp());
        bossConfig.setBossThreeStageFourHp(preference.getBossThreeStageFourHp());
        bossConfig.setBossThreeStageFiveHp(preference.getBossThreeStageFiveHp());
        bossConfig.setBossFourStageOneHp(preference.getBossFourStageOneHp());
        bossConfig.setBossFourStageTwoHp(preference.getBossFourStageTwoHp());
        bossConfig.setBossFourStageThreeHp(preference.getBossFourStageThreeHp());
        bossConfig.setBossFourStageFourHp(preference.getBossFourStageFourHp());
        bossConfig.setBossFourStageFiveHp(preference.getBossFourStageFiveHp());
        bossConfig.setBossFiveStageOneHp(preference.getBossFiveStageOneHp());
        bossConfig.setBossFiveStageTwoHp(preference.getBossFiveStageTwoHp());
        bossConfig.setBossFiveStageThreeHp(preference.getBossFiveStageThreeHp());
        bossConfig.setBossFiveStageFourHp(preference.getBossFiveStageFourHp());
        bossConfig.setBossFiveStageFiveHp(preference.getBossFiveStageFiveHp());
        mBcrLog.setBossConfig(bossConfig);
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
