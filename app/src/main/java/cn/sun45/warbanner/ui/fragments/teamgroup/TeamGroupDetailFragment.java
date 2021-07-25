package cn.sun45.warbanner.ui.fragments.teamgroup;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.appbar.MaterialToolbar;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.document.db.clanwar.TeamCustomizeModel;
import cn.sun45.warbanner.document.db.clanwar.TeamModel;
import cn.sun45.warbanner.document.db.source.CharacterModel;
import cn.sun45.warbanner.framework.ui.BaseActivity;
import cn.sun45.warbanner.framework.ui.BaseFragment;
import cn.sun45.warbanner.ui.shared.SharedViewModelClanwar;
import cn.sun45.warbanner.ui.shared.SharedViewModelSource;
import cn.sun45.warbanner.ui.views.teamgrouplist.TeamGroupListModel;
import cn.sun45.warbanner.ui.views.teamlist.TeamList;
import cn.sun45.warbanner.ui.views.teamlist.TeamListListener;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2021/6/2
 * 分刀详情Fragment
 */
public class TeamGroupDetailFragment extends BaseFragment implements TeamListListener {
    private TeamGroupListModel teamGroupListModel;

    private SharedViewModelSource sharedSource;
    private SharedViewModelClanwar sharedClanwar;

    private TeamList mTeamList;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_teamgroupdetail;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        teamGroupListModel = (TeamGroupListModel) bundle.getSerializable("teamGroupListModel");
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
        mTeamList = mRoot.findViewById(R.id.teamlist);

        mTeamList.setListener(this);
    }

    @Override
    protected void dataRequest() {
        sharedSource = new ViewModelProvider(requireActivity()).get(SharedViewModelSource.class);
        sharedClanwar = new ViewModelProvider(requireActivity()).get(SharedViewModelClanwar.class);

        mTeamList.setData(teamGroupListModel);
        sharedClanwar.teamCustomizeList.observe(requireActivity(), new Observer<List<TeamCustomizeModel>>() {
            @Override
            public void onChanged(List<TeamCustomizeModel> teamCustomizeModels) {
                mTeamList.notifyCustomize(teamCustomizeModels);
            }
        });
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
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.fragment_teamgroupdetail_drop_toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_share:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, teamGroupListModel.getTeamone().getShare() + "\n\n" + teamGroupListModel.getTeamtwo().getShare() + "\n\n" + teamGroupListModel.getTeamthree().getShare());
                intent.putExtra(Intent.EXTRA_SUBJECT, "share");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, Utils.getString(R.string.app_name)));
                break;
        }
        return true;
    }

    @Override
    public void select(TeamModel teamModel) {
        NavController controller = Navigation.findNavController(getView());
        Bundle bundle = new Bundle();
        bundle.putSerializable("teamModel", teamModel);
        controller.navigate(R.id.action_nav_teamgroupdetail_to_nav_teamdetail, bundle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sharedClanwar.teamCustomizeList.removeObservers(requireActivity());
        sharedSource.characterlist.removeObservers(requireActivity());
    }
}
