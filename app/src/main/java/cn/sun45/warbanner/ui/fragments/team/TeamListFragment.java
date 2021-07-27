package cn.sun45.warbanner.ui.fragments.team;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.checkbox.DialogCheckboxExtKt;
import com.afollestad.materialdialogs.list.DialogSingleChoiceExtKt;
import com.google.android.material.appbar.MaterialToolbar;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import cn.sun45.warbanner.ui.views.teamlist.TeamList;
import cn.sun45.warbanner.ui.views.teamlist.TeamListAdapter;
import cn.sun45.warbanner.ui.views.teamlist.TeamListListener;
import cn.sun45.warbanner.util.Utils;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function3;

/**
 * Created by Sun45 on 2021/5/22
 * 阵容列表Fragment
 */
public class TeamListFragment extends BaseFragment implements TeamListListener {
    private SharedViewModelSource sharedSource;
    private SharedViewModelClanwar sharedClanwar;

    private TeamList mTeamList;
    private TextView mEmptyHint;

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
        mEmptyHint = mRoot.findViewById(R.id.empty_hint);

        mTeamList.setListener(this);
    }

    @Override
    protected void dataRequest() {
        logD("dataRequest");
        sharedSource = new ViewModelProvider(requireActivity()).get(SharedViewModelSource.class);
        sharedClanwar = new ViewModelProvider(requireActivity()).get(SharedViewModelClanwar.class);
        String emptyhint;
        String date = ClanwarHelper.getCurrentClanWarDate();
        if (!TextUtils.isEmpty(date) && date.length() == 6) {
            String year = date.substring(0, 4);
            String month = date.substring(4, 6);
            date = year + "年" + month + "月";
            emptyhint = Utils.getStringWithPlaceHolder(R.string.teamlist_empty_hint, date);
        } else {
            emptyhint = Utils.getStringWithPlaceHolder(R.string.teamlist_empty_hint, "");
        }
        mEmptyHint.setText(emptyhint);

        sharedClanwar.teamList.observe(requireActivity(), new Observer<List<TeamModel>>() {
            @Override
            public void onChanged(List<TeamModel> teamModels) {
                logD("sharedTeamList onChanged");
//                for (TeamModel teamModel : teamModels) {
//                    logD(teamModel.toString());
//                }
                if (teamModels != null && !teamModels.isEmpty()) {
                    sharedSource.clanWarlist.observe(requireActivity(), new Observer<List<ClanWarModel>>() {
                        @Override
                        public void onChanged(List<ClanWarModel> clanWarModels) {
                            mTeamList.setData(teamModels, clanWarModels.get(0), new ClanwarPreference().isLinkshow(), new ClanwarPreference().getTeamlistautoscreen(), new ClanwarPreference().getTeamlistshowtype());
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
                mTeamList.notifyAutoScreen(integer);
                return null;
            }
        });
        dialog.cancelOnTouchOutside(false);
        dialog.positiveButton(R.string.teamlist_menu_auto_screen_dialog_confirm, null, null);
        dialog.show();
    }

    @Override
    public void select(TeamModel teamModel) {
        NavController controller = Navigation.findNavController(getView());
        Bundle bundle = new Bundle();
        bundle.putSerializable("teamModel", teamModel);
        controller.navigate(R.id.action_nav_main_to_nav_teamdetail, bundle);
    }

    @Override
    public void onDestroy() {
        logD("onDestroy");
        super.onDestroy();
        new SetupPreference().unregistListener(listener);
        sharedClanwar.teamList.removeObservers(requireActivity());
        sharedSource.clanWarlist.removeObservers(requireActivity());
        sharedClanwar.teamCustomizeList.removeObservers(requireActivity());
        sharedSource.characterlist.removeObservers(requireActivity());
    }
}
