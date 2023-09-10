package cn.sun45.warbanner.ui.fragments.team;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import cn.sun45.warbanner.document.database.source.models.BossModel;
import cn.sun45.warbanner.document.database.source.models.CharacterModel;
import cn.sun45.warbanner.document.database.source.models.TeamModel;
import cn.sun45.warbanner.document.preference.SetupPreference;
import cn.sun45.warbanner.framework.ui.BaseActivity;
import cn.sun45.warbanner.framework.ui.BaseFragment;
import cn.sun45.warbanner.stage.StageManager;
import cn.sun45.warbanner.ui.shared.SharedViewModelClanwar;
import cn.sun45.warbanner.ui.shared.SharedViewModelSource;
import cn.sun45.warbanner.ui.shared.SharedViewModelTeamScreenTeam;
import cn.sun45.warbanner.ui.views.listselectbar.ListSelectBar;
import cn.sun45.warbanner.ui.views.listselectbar.ListSelectBarListener;
import cn.sun45.warbanner.ui.views.listselectbar.ListSelectItem;
import cn.sun45.warbanner.ui.views.selectgroup.SelectGroup;
import cn.sun45.warbanner.ui.views.teamlist.TeamList;
import cn.sun45.warbanner.ui.views.teamlist.TeamListListener;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2021/7/4
 * 阵容选择Fragment
 */
public class TeamSelectFragment extends BaseFragment implements TeamListListener, ListSelectBarListener {
    private int team;
    private int stageSelection, bossSelection, typeSelection;

    private SharedViewModelSource sharedSource;
    private SharedViewModelClanwar sharedClanwar;
    private SharedViewModelTeamScreenTeam sharedTeamScreenTeam;

    private TeamList mTeamList;
    private ListSelectBar mListSelectBar;
    private TextView mEmptyHint;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_teamselect;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        team = bundle.getInt("team");
        stageSelection = bundle.getInt("stageSelection");
        bossSelection = bundle.getInt("bossSelection");
        typeSelection = bundle.getInt("typeSelection");
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
        mListSelectBar = mRoot.findViewById(R.id.listselectbar);
        mEmptyHint = mRoot.findViewById(R.id.empty_hint);

        mTeamList.setListener(this);
        mListSelectBar.setListener(this);
    }

    @Override
    protected void dataRequest() {
        sharedSource = new ViewModelProvider(requireActivity()).get(SharedViewModelSource.class);
        sharedClanwar = new ViewModelProvider(requireActivity()).get(SharedViewModelClanwar.class);
        sharedTeamScreenTeam = new ViewModelProvider(requireActivity()).get(SharedViewModelTeamScreenTeam.class);
        mEmptyHint.setText(R.string.teamselect_empty_hint);

        sharedSource.teamList.observe(requireActivity(), teamModels -> {
            logD("sharedSource teamList onChanged");
            if (teamModels != null && !teamModels.isEmpty()) {
                List<BossModel> bossModels = sharedSource.bossList.getValue();
                List<CharacterModel> characterModels = sharedSource.characterList.getValue();
                mTeamList.setData(teamModels, bossModels, characterModels,
                        false,
                        stageSelection, bossSelection, typeSelection,
                        new SetupPreference().isCharacterscreenenable(), CharacterHelper.getScreenCharacterList());
                if (teamModels != null && !teamModels.isEmpty()) {
                    mEmptyHint.setVisibility(View.INVISIBLE);
                }
                sharedClanwar.teamCustomizeList.observe(requireActivity(), teamCustomizeModels -> mTeamList.notifyCustomize(teamCustomizeModels));
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
        List<String> stageDescriptionList = StageManager.getInstance().getStageDescriptionList();
        stageDescriptionList.add(0, Utils.getString(R.string.all));
        stage.setData(stageDescriptionList, stageSelection);
        stage.setListener(position -> {
            mTeamList.notifyStageSelect(position);
        });
        lay.addView(stage);
        SelectGroup boss = new SelectGroup(getContext());
        boss.setData(Utils.getStringArray(R.array.teamlist_screen_boss_options), bossSelection);
        boss.setListener(position -> {
            mTeamList.notifyBossSelect(position);
        });
        lay.addView(boss);
        SelectGroup type = new SelectGroup(getContext());
        type.setData(Utils.getStringArray(R.array.teamlist_screen_type_options), typeSelection);
        type.setListener(position -> {
            mTeamList.notifyTypeSelect(position);
        });
        lay.addView(type);
        DialogCustomViewExtKt.customView(dialog, 0, lay, false, false, true, true);
        dialog.show();
    }

    @Override
    public void dataSet(int count, List<ListSelectItem> listSelectItems) {
        mListSelectBar.init(count, listSelectItems);
    }

    @Override
    public void onScrolled(int first, int last) {
        mListSelectBar.scroll(first, last);
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
    public void seek(int position) {
        mTeamList.scrollToPosition(position);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sharedSource.teamList.removeObservers(requireActivity());
        sharedClanwar.teamCustomizeList.removeObservers(requireActivity());
    }
}
