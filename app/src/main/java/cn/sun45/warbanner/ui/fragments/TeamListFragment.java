package cn.sun45.warbanner.ui.fragments;

import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.appbar.MaterialToolbar;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.datamanager.clanwar.ClanWarManager;
import cn.sun45.warbanner.datamanager.source.SourceManager;
import cn.sun45.warbanner.document.db.clanwar.TeamModel;
import cn.sun45.warbanner.document.db.setup.ScreenCharacterModel;
import cn.sun45.warbanner.document.db.source.CharacterModel;
import cn.sun45.warbanner.document.preference.ClanwarPreference;
import cn.sun45.warbanner.document.preference.SetupPreference;
import cn.sun45.warbanner.framework.ui.BaseActivity;
import cn.sun45.warbanner.framework.ui.BaseFragment;
import cn.sun45.warbanner.ui.shared.SharedViewModelCharacterModelList;
import cn.sun45.warbanner.ui.shared.SharedViewModelSetup;
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
    private SharedViewModelSetup sharedSetup;

    private TeamList mTeamList;

    private SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals("characterscreenenable")) {
                mTeamList.setScreenFunction(new SetupPreference().isCharacterscreenenable());
            }
        }
    };

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
        MaterialToolbar toolbar = mRoot.findViewById(R.id.drop_toolbar);
        ((BaseActivity) getActivity()).setSupportActionBar(toolbar);
        mTeamList = mRoot.findViewById(R.id.teamlist);
    }

    @Override
    protected void dataRequest() {
        logD("dataRequest");
        sharedCharacterModelList = new ViewModelProvider(requireActivity()).get(SharedViewModelCharacterModelList.class);
        sharedTeamList = new ViewModelProvider(requireActivity()).get(SharedViewModelTeamList.class);
        sharedSetup = new ViewModelProvider(requireActivity()).get(SharedViewModelSetup.class);

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
                sharedSetup.screencharacterlist.observe(requireActivity(), new Observer<List<ScreenCharacterModel>>() {
                    @Override
                    public void onChanged(List<ScreenCharacterModel> screenCharacterModels) {
                        mTeamList.notifyScreenCharacter(new SetupPreference().isCharacterscreenenable(), screenCharacterModels);
                    }
                });
            }
        });
        new SetupPreference().registListener(listener);
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
        menu.getItem(new ClanwarPreference().getTeamlistshowtype()).setChecked(true);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        new SetupPreference().unregistListener(listener);
    }
}
