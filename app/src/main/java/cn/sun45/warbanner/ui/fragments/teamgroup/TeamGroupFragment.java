package cn.sun45.warbanner.ui.fragments.teamgroup;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.checkbox.DialogCheckboxExtKt;
import com.afollestad.materialdialogs.customview.DialogCustomViewExtKt;
import com.afollestad.materialdialogs.list.DialogMultiChoiceExtKt;
import com.google.android.material.appbar.MaterialToolbar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.character.CharacterHelper;
import cn.sun45.warbanner.clanwar.ClanwarHelper;
import cn.sun45.warbanner.document.db.clanwar.TeamCustomizeModel;
import cn.sun45.warbanner.document.db.clanwar.TeamModel;
import cn.sun45.warbanner.document.db.source.CharacterModel;
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
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function3;

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
                sharedSource.characterlist.observe(requireActivity(), new Observer<List<CharacterModel>>() {
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

    private void showbossdialog() {
        MaterialDialog dialog = new MaterialDialog(getContext(), MaterialDialog.getDEFAULT_BEHAVIOR());
//        dialog.title(R.string.teamgroup_menu_boss_screen, null);
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
        DialogMultiChoiceExtKt.listItemsMultiChoice(dialog, R.array.teamgroup_menu_boss_screen_dialog_options, null, getBossdialogDisabledIndices(selectionlist), selectionlist, false, false, new Function3<MaterialDialog, int[], List<? extends CharSequence>, Unit>() {
            @Override
            public Unit invoke(MaterialDialog materialDialog, int[] ints, List<? extends CharSequence> charSequences) {
                new SetupPreference().setBossonescreen(false);
                new SetupPreference().setBosstwoscreen(false);
                new SetupPreference().setBossthreescreen(false);
                new SetupPreference().setBossfourscreen(false);
                new SetupPreference().setBossfivescreen(false);
                for (int which : ints) {
                    switch (which) {
                        case 0:
                            new SetupPreference().setBossonescreen(true);
                            break;
                        case 1:
                            new SetupPreference().setBosstwoscreen(true);
                            break;
                        case 2:
                            new SetupPreference().setBossthreescreen(true);
                            break;
                        case 3:
                            new SetupPreference().setBossfourscreen(true);
                            break;
                        case 4:
                            new SetupPreference().setBossfivescreen(true);
                            break;
                        default:
                            break;
                    }
                }
                DialogMultiChoiceExtKt.updateListItemsMultiChoice(materialDialog, R.array.teamgroup_menu_boss_screen_dialog_options, null, getBossdialogDisabledIndices(ints), null);
                return null;
            }
        });
        dialog.positiveButton(R.string.teamgroup_menu_boss_screen_dialog_confirm, null, null);
        dialog.show();
    }

    private int[] getBossdialogDisabledIndices(int[] selectionlist) {
        if (selectionlist.length == 3) {
            List<Integer> disabledlist = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                boolean contain = false;
                for (int selection : selectionlist) {
                    if (selection == i) {
                        contain = true;
                        break;
                    }
                }
                if (!contain) {
                    disabledlist.add(i);
                }
            }
            int[] disabledIndices = new int[disabledlist.size()];
            for (int i = 0; i < disabledlist.size(); i++) {
                disabledIndices[i] = disabledlist.get(i);
            }
            return disabledIndices;
        }
        return new int[]{};
    }

    private void showautodialog() {
        MaterialDialog dialog = new MaterialDialog(getContext(), MaterialDialog.getDEFAULT_BEHAVIOR());
        dialog.title(R.string.teamgroup_menu_auto_screen, null);
        int autocount = new SetupPreference().getAutocount();
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        TextView auto = new TextView(getContext());
        auto.setText(Utils.getStringWithPlaceHolder(R.string.teamgroup_menu_auto_screen_autocount, autocount));
        auto.setTextColor(Utils.getColor(R.color.green_200));
        TextView notauto = new TextView(getContext());
        notauto.setText(Utils.getStringWithPlaceHolder(R.string.teamgroup_menu_auto_screen_notautocount, 3 - autocount));
        notauto.setTextColor(Utils.getColor(R.color.red_200));
        AppCompatSeekBar seekBar = new AppCompatSeekBar(getContext());
        seekBar.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        seekBar.setProgressDrawable(getResources().getDrawable(R.drawable.teamgroup_auto_screen_progress_drawable));
        seekBar.setMax(3);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                new SetupPreference().setAutocount(progress);
                auto.setText(Utils.getStringWithPlaceHolder(R.string.teamgroup_menu_auto_screen_autocount, progress));
                notauto.setText(Utils.getStringWithPlaceHolder(R.string.teamgroup_menu_auto_screen_notautocount, 3 - progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekBar.setProgress(autocount);
        linearLayout.addView(auto);
        linearLayout.addView(seekBar);
        linearLayout.addView(notauto);
        DialogCustomViewExtKt.customView(dialog, null, linearLayout, false, false, true, false);
        DialogCheckboxExtKt.checkBoxPrompt(dialog, R.string.teamgroup_menu_auto_screen_use, null, new SetupPreference().isUseautoscreen(), new Function1<Boolean, Unit>() {
            @Override
            public Unit invoke(Boolean aBoolean) {
                new SetupPreference().setUseautoscreen(aBoolean);
                return null;
            }
        });
        dialog.show();
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
        sharedSource.characterlist.removeObservers(requireActivity());
        sharedClanwar.teamList.removeObservers(requireActivity());
        sharedClanwar.teamCustomizeList.removeObservers(requireActivity());
        sharedClanwar.loadData();
    }
}