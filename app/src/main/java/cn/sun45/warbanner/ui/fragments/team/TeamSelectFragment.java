package cn.sun45.warbanner.ui.fragments.team;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.afollestad.materialdialogs.LayoutMode;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.bottomsheets.BottomSheet;
import com.afollestad.materialdialogs.customview.DialogCustomViewExtKt;
import com.google.android.material.appbar.MaterialToolbar;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.character.CharacterHelper;
import cn.sun45.warbanner.clanwar.ClanwarHelper;
import cn.sun45.warbanner.document.db.clanwar.TeamCustomizeModel;
import cn.sun45.warbanner.document.db.clanwar.TeamModel;
import cn.sun45.warbanner.document.db.source.CharacterModel;
import cn.sun45.warbanner.document.db.source.ClanWarModel;
import cn.sun45.warbanner.document.preference.ClanwarPreference;
import cn.sun45.warbanner.document.preference.SetupPreference;
import cn.sun45.warbanner.framework.ui.BaseActivity;
import cn.sun45.warbanner.framework.ui.BaseFragment;
import cn.sun45.warbanner.ui.shared.SharedViewModelClanwar;
import cn.sun45.warbanner.ui.shared.SharedViewModelSource;
import cn.sun45.warbanner.ui.shared.SharedViewModelTeamScreenTeam;
import cn.sun45.warbanner.ui.views.selectgroup.SelectGroup;
import cn.sun45.warbanner.ui.views.teamlist.TeamList;
import cn.sun45.warbanner.ui.views.teamlist.TeamListListener;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2021/7/4
 * 阵容选择Fragment
 */
public class TeamSelectFragment extends BaseFragment implements TeamListListener {
    private int team;

    private SharedViewModelSource sharedSource;
    private SharedViewModelClanwar sharedClanwar;
    private SharedViewModelTeamScreenTeam sharedTeamScreenTeam;

    private TeamList mTeamList;
    private TextView mEmptyHint;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_teamselect;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        team = bundle.getInt("team");
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
        mEmptyHint = mRoot.findViewById(R.id.empty_hint);

        mTeamList.setListener(this);
    }

    @Override
    protected void dataRequest() {
        sharedSource = new ViewModelProvider(requireActivity()).get(SharedViewModelSource.class);
        sharedClanwar = new ViewModelProvider(requireActivity()).get(SharedViewModelClanwar.class);
        sharedTeamScreenTeam = new ViewModelProvider(requireActivity()).get(SharedViewModelTeamScreenTeam.class);
        String emptyhint;
        String date = ClanwarHelper.getCurrentClanWarDate();
        if (!TextUtils.isEmpty(date) && date.length() == 8) {
            String year = date.substring(0, 4);
            String month = date.substring(4, 6);
            String day = date.substring(6, 8);
            date = year + "年" + month + "月" + day + "日";
            emptyhint = Utils.getStringWithPlaceHolder(R.string.teamselect_empty_hint, date);
        } else {
            emptyhint = Utils.getStringWithPlaceHolder(R.string.teamselect_empty_hint, "");
        }
        mEmptyHint.setText(emptyhint);

        sharedClanwar.teamList.observe(requireActivity(), new Observer<List<TeamModel>>() {
            @Override
            public void onChanged(List<TeamModel> teamModels) {
                if (teamModels != null && !teamModels.isEmpty()) {
                    sharedSource.clanWarlist.observe(requireActivity(), new Observer<List<ClanWarModel>>() {
                        @Override
                        public void onChanged(List<ClanWarModel> clanWarModels) {
                            mTeamList.setData(teamModels, clanWarModels.get(0), false, new ClanwarPreference().getTeamliststage(), new ClanwarPreference().getTeamlistboss(), new ClanwarPreference().getTeamlisttype());
                            if (teamModels != null && !teamModels.isEmpty()) {
                                mEmptyHint.setVisibility(View.INVISIBLE);
                            }
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
                                    mTeamList.notifyScreenCharacter(new SetupPreference().isCharacterscreenenable(), CharacterHelper.getScreenCharacterList());
                                }
                            });
                        }
                    });
                }
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
        inflater.inflate(R.menu.fragment_teamselect_drop_toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_screen:
                showScreendialog();
                break;
        }
        return true;
    }

    private void showScreendialog() {
        BottomSheet bottomSheet = new BottomSheet(LayoutMode.WRAP_CONTENT);
        MaterialDialog dialog = new MaterialDialog(getContext(), bottomSheet);
        LinearLayout lay = new LinearLayout(getContext());
        lay.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        lay.setOrientation(LinearLayout.VERTICAL);
        SelectGroup stage = new SelectGroup(getContext());
        stage.setData(Utils.getStringArray(R.array.teamlist_screen_stage_options), new ClanwarPreference().getTeamliststage());
        stage.setListener(position -> {
            new ClanwarPreference().setTeamliststage(position);
            mTeamList.notifyStageSelect(position);
        });
        lay.addView(stage);
        SelectGroup boss = new SelectGroup(getContext());
        boss.setData(Utils.getStringArray(R.array.teamlist_screen_boss_options), new ClanwarPreference().getTeamlistboss());
        boss.setListener(position -> {
            new ClanwarPreference().setTeamlistboss(position);
            mTeamList.notifyBossSelect(position);
        });
        lay.addView(boss);
        SelectGroup type = new SelectGroup(getContext());
        type.setData(Utils.getStringArray(R.array.teamlist_screen_type_options), new ClanwarPreference().getTeamlisttype());
        type.setListener(position -> {
            new ClanwarPreference().setTeamlisttype(position);
            mTeamList.notifyTypeSelect(position);
        });
        lay.addView(type);
        DialogCustomViewExtKt.customView(dialog, 0, lay, false, false, true, true);
        dialog.show();
    }

    @Override
    public void select(TeamModel teamModel) {
        switch (team) {
            case 1:
                sharedTeamScreenTeam.teamOne.postValue(teamModel);
                break;
            case 2:
                sharedTeamScreenTeam.teamTwo.postValue(teamModel);
                break;
            case 3:
                sharedTeamScreenTeam.teamThree.postValue(teamModel);
                break;
        }
        Navigation.findNavController(getView()).navigateUp();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sharedClanwar.teamList.removeObservers(requireActivity());
        sharedSource.clanWarlist.removeObservers(requireActivity());
        sharedClanwar.teamCustomizeList.removeObservers(requireActivity());
        sharedSource.characterlist.removeObservers(requireActivity());
    }
}
