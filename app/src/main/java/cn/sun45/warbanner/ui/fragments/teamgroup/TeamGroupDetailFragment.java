package cn.sun45.warbanner.ui.fragments.teamgroup;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.document.db.clanwar.TeamModel;
import cn.sun45.warbanner.document.db.source.CharacterModel;
import cn.sun45.warbanner.framework.ui.BaseActivity;
import cn.sun45.warbanner.framework.ui.BaseFragment;
import cn.sun45.warbanner.ui.shared.SharedViewModelSource;
import cn.sun45.warbanner.ui.views.teamgrouplist.TeamGroupListModel;
import cn.sun45.warbanner.ui.views.teamlist.TeamList;

/**
 * Created by Sun45 on 2021/6/2
 * 分刀详情Fragment
 */
public class TeamGroupDetailFragment extends BaseFragment {
    private TeamGroupListModel teamGroupListModel;

    private SharedViewModelSource sharedSource;

    private TeamList mTeamList;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_teamgroupdetail;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        teamGroupListModel = (TeamGroupListModel) bundle.getSerializable("teamGroupListModel");
        mTeamList = mRoot.findViewById(R.id.teamlist);
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
    }

    @Override
    protected void dataRequest() {
        sharedSource = new ViewModelProvider(requireActivity()).get(SharedViewModelSource.class);

        List<TeamModel> teamModels = new ArrayList<>();
        teamModels.add(teamGroupListModel.getTeamone());
        teamModels.add(teamGroupListModel.getTeamtwo());
        teamModels.add(teamGroupListModel.getTeamthree());
        mTeamList.setData(teamModels, true);
        sharedSource.characterlist.observe(requireActivity(), new Observer<List<CharacterModel>>() {
            @Override
            public void onChanged(List<CharacterModel> characterModels) {
                mTeamList.notifyCharacter(characterModels);
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
    public void onDestroy() {
        super.onDestroy();
        sharedSource.characterlist.removeObservers(requireActivity());
    }
}
