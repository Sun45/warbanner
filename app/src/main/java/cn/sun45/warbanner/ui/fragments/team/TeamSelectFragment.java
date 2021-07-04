package cn.sun45.warbanner.ui.fragments.team;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.checkbox.DialogCheckboxExtKt;
import com.afollestad.materialdialogs.list.DialogSingleChoiceExtKt;
import com.google.android.material.appbar.MaterialToolbar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.character.CharacterHelper;
import cn.sun45.warbanner.document.db.clanwar.TeamGroupScreenModel;
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
import cn.sun45.warbanner.ui.views.teamlist.TeamList;
import cn.sun45.warbanner.ui.views.teamlist.TeamListAdapter;
import cn.sun45.warbanner.ui.views.teamlist.TeamListListener;
import cn.sun45.warbanner.util.FileUtil;
import cn.sun45.warbanner.util.Utils;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function3;

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

        mTeamList.setListener(this);
    }

    @Override
    protected void dataRequest() {
        sharedSource = new ViewModelProvider(requireActivity()).get(SharedViewModelSource.class);
        sharedClanwar = new ViewModelProvider(requireActivity()).get(SharedViewModelClanwar.class);
        sharedTeamScreenTeam = new ViewModelProvider(requireActivity()).get(SharedViewModelTeamScreenTeam.class);

        sharedClanwar.teamList.observe(requireActivity(), new Observer<List<TeamModel>>() {
            @Override
            public void onChanged(List<TeamModel> teamModels) {
                mTeamList.setData(teamModels, false, new ClanwarPreference().getTeamlistautoscreen(), new ClanwarPreference().getTeamlistshowtype());
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
        menu.getItem(new ClanwarPreference().getTeamlistshowtype() + 1).setChecked(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        if (item.getGroupId() == R.id.stage) {
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
        } else {
            switch (item.getItemId()) {
                case R.id.menu_auto:
                    showautoscreendialog();
                    break;
            }
        }
        return true;
    }

    private void showautoscreendialog() {
        MaterialDialog dialog = new MaterialDialog(getContext(), MaterialDialog.getDEFAULT_BEHAVIOR());
        dialog.title(R.string.teamselect_menu_auto_screen, null);
        List<Integer> selection = new ArrayList<>();
        if (new SetupPreference().isBossonescreen()) {
            selection.add(0);
        }
        if (new SetupPreference().isBosstwoscreen()) {
            selection.add(1);
        }
        if (new SetupPreference().isBossthreescreen()) {
            selection.add(2);
        }
        if (new SetupPreference().isBossfourscreen()) {
            selection.add(3);
        }
        if (new SetupPreference().isBossfivescreen()) {
            selection.add(4);
        }
        int[] selectionlist = new int[selection.size()];
        for (int i = 0; i < selection.size(); i++) {
            selectionlist[i] = selection.get(i);
        }
        DialogSingleChoiceExtKt.listItemsSingleChoice(dialog, R.array.teamselect_menu_auto_screen_dialog_options, null, null, new ClanwarPreference().getTeamlistautoscreen(), true, 0, 0, new Function3<MaterialDialog, Integer, CharSequence, Unit>() {
            @Override
            public Unit invoke(MaterialDialog materialDialog, Integer integer, CharSequence charSequence) {
                new ClanwarPreference().setTeamlistautoscreen(integer);
                mTeamList.notifyAutoScreen(integer);
                return null;
            }
        });
        dialog.cancelOnTouchOutside(false);
        dialog.positiveButton(R.string.teamselect_menu_auto_screen_dialog_confirm, null, null);
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
        sharedSource.characterlist.removeObservers(requireActivity());
        sharedClanwar.teamList.removeObservers(requireActivity());
    }
}
