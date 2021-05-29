package cn.sun45.warbanner.ui.fragments;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.document.db.clanwar.TeamModel;
import cn.sun45.warbanner.document.db.source.CharacterModel;
import cn.sun45.warbanner.document.preference.ClanwarPreference;
import cn.sun45.warbanner.framework.ui.BaseActivity;
import cn.sun45.warbanner.framework.ui.BaseFragment;
import cn.sun45.warbanner.ui.shared.SharedViewModelCharacterModelList;
import cn.sun45.warbanner.ui.shared.SharedViewModelTeamList;
import cn.sun45.warbanner.ui.views.teamlist.TeamList;
import cn.sun45.warbanner.ui.views.teamlist.TeamListAdapter;

/**
 * Created by Sun45 on 2021/5/22
 * 阵容列表Fragment
 */
public class TeamListFragment extends BaseFragment {
    private SharedViewModelCharacterModelList sharedCharacterModelList;
    private SharedViewModelTeamList sharedTeamList;

    private TeamList mTeamList;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_teamlist;
    }

    @Override
    protected void initData() {
        setHasOptionsMenu(true);
    }

    @Override
    protected void initView() {
        MaterialToolbar toolbar = (MaterialToolbar) mRoot.findViewById(R.id.drop_toolbar);
        ((BaseActivity) getActivity()).setSupportActionBar(toolbar);
        mTeamList = mRoot.findViewById(R.id.teamlist);
    }

    @Override
    protected void dataRequest() {
        logD("dataRequest");
        sharedCharacterModelList = new ViewModelProvider(requireActivity()).get(SharedViewModelCharacterModelList.class);
        sharedTeamList = new ViewModelProvider(requireActivity()).get(SharedViewModelTeamList.class);

        sharedTeamList.teamList.observe(requireActivity(), new Observer<List<TeamModel>>() {
            @Override
            public void onChanged(List<TeamModel> teamModels) {
                logD("sharedTeamList onChanged");
//                for (TeamModel teamModel : teamModels) {
//                    logD(teamModel.toString());
//                }
                mTeamList.setData(teamModels, new ClanwarPreference().getTeamlistshowtype());
                sharedCharacterModelList.characterlist.observe(requireActivity(), new Observer<List<CharacterModel>>() {
                    @Override
                    public void onChanged(List<CharacterModel> characterModels) {
                        mTeamList.notifyCharacter(characterModels);
                    }
                });
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
        inflater.inflate(R.menu.fragment_teamlist_drop_toolbar, menu);
        int index = 0;
        switch (new ClanwarPreference().getTeamlistshowtype()) {
            case TeamListAdapter.SHOW_TYPE_ALL:
                index = 0;
                break;
            case TeamListAdapter.SHOW_TYPE_ONE:
                index = 1;
                break;
            case TeamListAdapter.SHOW_TYPE_TWO:
                index = 2;
                break;
            case TeamListAdapter.SHOW_TYPE_THREE:
                index = 3;
                break;
            default:
                break;
        }
        menu.getItem(index).setChecked(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        int showtype = 0;
        switch (item.getItemId()) {
            case R.id.menu_all:
                item.setChecked(true);
                showtype = TeamListAdapter.SHOW_TYPE_ALL;
                break;
            case R.id.menu_one:
                item.setChecked(true);
                showtype = TeamListAdapter.SHOW_TYPE_ONE;
                break;
            case R.id.menu_two:
                item.setChecked(true);
                showtype = TeamListAdapter.SHOW_TYPE_TWO;
                break;
            case R.id.menu_three:
                item.setChecked(true);
                showtype = TeamListAdapter.SHOW_TYPE_THREE;
                break;
        }
        new ClanwarPreference().setTeamlistshowtype(showtype);
        mTeamList.notifyShowtype(showtype);
        return true;
    }
}
