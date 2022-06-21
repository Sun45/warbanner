package cn.sun45.warbanner.ui.fragments.teamgroup;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.appbar.MaterialToolbar;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.character.CharacterHelper;
import cn.sun45.warbanner.clanwar.ClanwarHelper;
import cn.sun45.warbanner.document.database.setup.models.TeamCustomizeModel;
import cn.sun45.warbanner.document.database.source.models.CharacterModel;
import cn.sun45.warbanner.document.database.source.models.TeamModel;
import cn.sun45.warbanner.document.preference.SetupPreference;
import cn.sun45.warbanner.framework.MyApplication;
import cn.sun45.warbanner.framework.ui.BaseActivity;
import cn.sun45.warbanner.framework.ui.BaseFragment;
import cn.sun45.warbanner.teamgroup.TeamGroupHelper;
import cn.sun45.warbanner.ui.shared.SharedViewModelClanwar;
import cn.sun45.warbanner.ui.shared.SharedViewModelSource;
import cn.sun45.warbanner.ui.views.teamgrouplist.TeamGroupList;
import cn.sun45.warbanner.ui.views.teamgrouplist.TeamGroupListListener;
import cn.sun45.warbanner.ui.views.teamgrouplist.TeamGroupListModel;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2021/5/30
 * 分刀Fragment
 */
public class TeamGroupFragment extends BaseFragment implements TeamGroupListListener {
    private SharedViewModelSource sharedSource;
    private SharedViewModelClanwar sharedClanwar;

    private TeamGroupHelper teamGroupHelper;

    private TeamGroupList mTeamGroupList;
    private TextView mState;

    private List<TeamGroupListModel> list;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_teamgroup;
    }

    @Override
    protected void initData() {
        setHasOptionsMenu(true);
        teamGroupHelper = new TeamGroupHelper(new SetupPreference().isCharacterscreenenable());
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

        mTeamGroupList = mRoot.findViewById(R.id.teamgrouplist);
        mTeamGroupList.setListener(this);
        mState = mRoot.findViewById(R.id.state);
    }

    @Override
    protected void dataRequest() {
        logD("dataRequest");
        sharedSource = new ViewModelProvider(requireActivity()).get(SharedViewModelSource.class);
        sharedClanwar = new ViewModelProvider(requireActivity()).get(SharedViewModelClanwar.class);

        if (list != null) {
            showresult();
        }
        sharedClanwar.teamList.observe(requireActivity(), new Observer<List<TeamModel>>() {
            @Override
            public void onChanged(List<TeamModel> teamModels) {
                sharedSource.characterList.observe(requireActivity(), new Observer<List<CharacterModel>>() {
                    @Override
                    public void onChanged(List<CharacterModel> characterModels) {
                        mTeamGroupList.setCharacterModels(characterModels);
                        sharedClanwar.teamCustomizeList.observe(requireActivity(), new Observer<List<TeamCustomizeModel>>() {
                            @Override
                            public void onChanged(List<TeamCustomizeModel> teamCustomizeModels) {
                                mTeamGroupList.notifyCustomize(teamCustomizeModels);
                                mState.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mState.setClickable(false);
                                        mState.setText(R.string.teamgroup_state_progressing);
                                        new Thread() {
                                            @Override
                                            public void run() {
                                                super.run();
                                                long start = MyApplication.getTimecurrent();
                                                logD("teamGroup build");
                                                list = teamGroupHelper.build(CharacterHelper.getScreenCharacterList(), teamModels, teamCustomizeModels);
                                                logD("teamGroup finish:" + (list != null ? list.size() : 0) + " " + (MyApplication.getTimecurrent() - start) + "ms");
                                                Activity activity = getActivity();
                                                if (activity != null) {
                                                    activity.runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            showresult();
                                                        }
                                                    });
                                                }
                                            }
                                        }.start();
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * 展示分刀结果
     */
    private void showresult() {
        int size = list != null ? list.size() : 0;
        if (size == TeamGroupHelper.interruptsize) {
            mState.setText(Utils.getStringWithPlaceHolder(R.string.teamgroup_state_interrupt_finish, size));
        } else {
            mState.setText(Utils.getStringWithPlaceHolder(R.string.teamgroup_state_finish, size));
        }
        mState.setClickable(true);
        mTeamGroupList.setData(list);
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
        inflater.inflate(R.menu.fragment_teamgroup_drop_toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_screen:
                NavController controller = Navigation.findNavController(getView());
                controller.navigate(R.id.action_nav_teamgroup_to_nav_teamgroupscreen);
                break;
        }
        return true;
    }

    @Override
    public void collect(TeamGroupListModel teamGroupListModel, boolean collect) {
        ClanwarHelper.collect(teamGroupListModel, collect);
    }

    @Override
    public void open(TeamGroupListModel teamGroupListModel) {
        NavController controller = Navigation.findNavController(getView());
        Bundle bundle = new Bundle();
        bundle.putSerializable("teamGroupListModel", teamGroupListModel);
        controller.navigate(R.id.action_nav_teamgroup_to_nav_teamgroupdetail, bundle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sharedSource.characterList.removeObservers(requireActivity());
        sharedClanwar.teamList.removeObservers(requireActivity());
        sharedClanwar.teamCustomizeList.removeObservers(requireActivity());
        sharedClanwar.loadData();
    }
}