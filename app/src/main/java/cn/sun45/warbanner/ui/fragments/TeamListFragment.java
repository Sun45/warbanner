package cn.sun45.warbanner.ui.fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.checkbox.DialogCheckboxExtKt;
import com.afollestad.materialdialogs.list.DialogSingleChoiceExtKt;
import com.google.android.material.appbar.MaterialToolbar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function3;

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
                listShow(teamModels);
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

    private void listShow(List<TeamModel> teamModels) {
        int teamlistautoscreen = new ClanwarPreference().getTeamlistautoscreen();
        List<TeamModel> data = new ArrayList<>();
        for (TeamModel teamModel : teamModels) {
            if (teamModel.isAuto()) {
                if (teamlistautoscreen == 0 || teamlistautoscreen == 1) {
                    data.add(teamModel);
                }
            } else {
                if (teamlistautoscreen == 0 || teamlistautoscreen == 2) {
                    data.add(teamModel);
                }
            }
        }
        mTeamList.setData(data, new ClanwarPreference().isLinkshow(), new ClanwarPreference().getTeamlistshowtype());
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
        menu.getItem(new ClanwarPreference().getTeamlistshowtype() + 2).setChecked(true);
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
                case R.id.menu_link:
                    showlinkdialog();
                    break;
                case R.id.menu_auto:
                    showautoscreendialog();
                    break;
            }
        }
        return true;
    }

    private void showlinkdialog() {
        MaterialDialog dialog = new MaterialDialog(getContext(), MaterialDialog.getDEFAULT_BEHAVIOR());
        dialog.title(R.string.teamlist_menu_link_screen, null);
        DialogCheckboxExtKt.checkBoxPrompt(dialog, R.string.teamlist_menu_link_show, null, new ClanwarPreference().isLinkshow(), new Function1<Boolean, Unit>() {
            @Override
            public Unit invoke(Boolean aBoolean) {
                new ClanwarPreference().setLinkshow(aBoolean);
                mTeamList.notifyShowLink(aBoolean);
                return null;
            }
        });
        dialog.positiveButton(R.string.teamlist_menu_link_screen_dialog_confirm, null, null);
        dialog.show();
    }

    private void showautoscreendialog() {
        MaterialDialog dialog = new MaterialDialog(getContext(), MaterialDialog.getDEFAULT_BEHAVIOR());
        dialog.title(R.string.teamlist_menu_auto_screen, null);
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
        DialogSingleChoiceExtKt.listItemsSingleChoice(dialog, R.array.teamlist_menu_auto_screen_dialog_options, null, null, new ClanwarPreference().getTeamlistautoscreen(), true, 0, 0, new Function3<MaterialDialog, Integer, CharSequence, Unit>() {
            @Override
            public Unit invoke(MaterialDialog materialDialog, Integer integer, CharSequence charSequence) {
                new ClanwarPreference().setTeamlistautoscreen(integer);
                listShow(sharedTeamList.teamList.getValue());
                return null;
            }
        });
        dialog.cancelOnTouchOutside(false);
        dialog.positiveButton(R.string.teamlist_menu_auto_screen_dialog_confirm, null, null);
        dialog.show();
    }

    @Override
    public void onDestroy() {
        logD("onDestroy");
        super.onDestroy();
        new SetupPreference().unregistListener(listener);
        sharedCharacterModelList.characterlist.removeObservers(requireActivity());
        sharedTeamList.teamList.removeObservers(requireActivity());
        sharedSetup.screencharacterlist.removeObservers(requireActivity());
    }
}
