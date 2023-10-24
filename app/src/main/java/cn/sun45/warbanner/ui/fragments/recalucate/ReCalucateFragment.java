package cn.sun45.warbanner.ui.fragments.recalucate;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.afollestad.materialdialogs.LayoutMode;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.bottomsheets.BottomSheet;
import com.afollestad.materialdialogs.customview.DialogCustomViewExtKt;
import com.google.android.material.appbar.MaterialToolbar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.document.database.source.models.TeamModel;
import cn.sun45.warbanner.document.preference.SetupPreference;
import cn.sun45.warbanner.framework.MyApplication;
import cn.sun45.warbanner.framework.ui.BaseActivity;
import cn.sun45.warbanner.framework.ui.BaseFragment;
import cn.sun45.warbanner.stage.StageManager;
import cn.sun45.warbanner.teamgroup.TeamGroupConfigureModel;
import cn.sun45.warbanner.teamgroup.TeamGroupHelper;
import cn.sun45.warbanner.ui.shared.SharedViewModelClanwar;
import cn.sun45.warbanner.ui.shared.SharedViewModelSource;
import cn.sun45.warbanner.ui.views.listselectbar.ListSelectItem;
import cn.sun45.warbanner.ui.views.selectgroup.SelectGroup;
import cn.sun45.warbanner.ui.views.teamgrouplist.TeamGroupListModel;
import cn.sun45.warbanner.ui.views.teamlist.TeamList;
import cn.sun45.warbanner.ui.views.teamlist.TeamListListener;
import cn.sun45.warbanner.ui.views.teamlist.TeamListReCalucateModel;
import cn.sun45.warbanner.ui.views.teamlist.TeamListTeamModel;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2023/10/19
 */
public class ReCalucateFragment extends BaseFragment implements TeamListListener {
    private SharedViewModelSource sharedSource;
    private SharedViewModelClanwar sharedClanwar;

    private List<Integer> usedCharacterList;

    private TeamGroupHelper teamGroupHelper;

    private TeamList mTeamList;

    private TextView mEmptyHint;

    private List<TeamListTeamModel> list;

    private int stageSelection;

    private int bossSelection;

    private int typeSelection;

    private int lastStageSelection = -1;

    private int lastBossSelection = -1;

    private int lastTypeSelection = -1;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_recalucate;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        usedCharacterList = bundle.getIntegerArrayList("usedCharacterList");
        setHasOptionsMenu(true);
        teamGroupHelper = new TeamGroupHelper(new SetupPreference().isCharacterscreenenable());
    }

    @Override
    protected void initView() {
        MaterialToolbar toolbar = mRoot.findViewById(R.id.drop_toolbar);
        ((BaseActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        mTeamList = mRoot.findViewById(R.id.teamlist);
        mEmptyHint = mRoot.findViewById(R.id.empty_hint);

        mTeamList.setListener(this);
    }

    @Override
    protected void dataRequest() {
        sharedSource = new ViewModelProvider(requireActivity()).get(SharedViewModelSource.class);
        sharedClanwar = new ViewModelProvider(requireActivity()).get(SharedViewModelClanwar.class);

        showList();
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
        inflater.inflate(R.menu.fragment_recalucate_drop_toolbar, menu);
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
        stage.setData(stageDescriptionList, stageSelection);
        stage.setListener(position -> {
            stageSelection = position;
        });
        lay.addView(stage);
        SelectGroup boss = new SelectGroup(getContext());
        boss.setData(Utils.getStringArray(R.array.re_calucate_boss_options), bossSelection);
        boss.setListener(position -> {
            bossSelection = position;
        });
        lay.addView(boss);
        SelectGroup type = new SelectGroup(getContext());
        type.setData(Utils.getStringArray(R.array.re_calucate_auto_options), typeSelection);
        type.setListener(position -> {
            typeSelection = position;
        });
        lay.addView(type);
        DialogCustomViewExtKt.customView(dialog, 0, lay, false, false, true, true);
        dialog.setOnDismissListener(dialog1 -> {
            showList();
        });
        dialog.show();
    }

    private void showList() {
        if (lastStageSelection != stageSelection || lastBossSelection != bossSelection || lastTypeSelection != typeSelection) {
            lastStageSelection = stageSelection;
            lastBossSelection = bossSelection;
            lastTypeSelection = typeSelection;
            MaterialDialog materialDialog = new MaterialDialog(getContext(), MaterialDialog.getDEFAULT_BEHAVIOR());
            materialDialog.message(R.string.re_calucate_analysing, null, null)
                    .cancelOnTouchOutside(false)
                    .show();
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    long start = MyApplication.getTimecurrent();
                    logD("teamGroup build");
                    TeamGroupConfigureModel teamGroupConfigureModel = new TeamGroupConfigureModel();
                    TeamGroupConfigureModel.Configure configure = teamGroupConfigureModel.new Configure(
                            stageSelection, bossSelection, typeSelection, 0, 0, 0, 0, 0, -1, false);
                    teamGroupConfigureModel.setTeamOneConfigure(configure);
                    List<TeamGroupListModel> teamGroupListModels = teamGroupHelper.build(sharedSource.teamList.getValue(), sharedClanwar.teamCustomizeList.getValue(), null, usedCharacterList, teamGroupConfigureModel);
                    logD("teamGroup finish count:" + (teamGroupListModels != null ? teamGroupListModels.size() : 0) + " " + (MyApplication.getTimecurrent() - start) + "ms");
                    list = new ArrayList<>();
                    if (teamGroupListModels != null) {
                        for (TeamGroupListModel teamGroupListModel : teamGroupListModels) {
                            list.add(new TeamListTeamModel(teamGroupListModel.getTeamone(), teamGroupListModel.getBorrowindexone()));
                        }
                    }
                    Activity activity = getActivity();
                    if (activity != null) {
                        activity.runOnUiThread(() -> {
                            materialDialog.dismiss();
                            showResult();
                        });
                    }
                }
            }.start();
        } else {
            showResult();
        }
    }

    private void showResult() {
        mTeamList.setData(list, sharedSource.characterList.getValue());
        if (list == null || list.isEmpty()) {
            mEmptyHint.setVisibility(View.VISIBLE);
        } else {
            mEmptyHint.setVisibility(View.GONE);
        }
    }

    @Override
    public void dataSet(int count, List<ListSelectItem> listSelectItems) {

    }

    @Override
    public void onScrolled(int first, int last) {

    }

    @Override
    public void select(TeamModel teamModel) {
        NavController controller = Navigation.findNavController(getView());
        Bundle bundle = new Bundle();
        bundle.putSerializable("teamModel", teamModel);
        controller.navigate(R.id.action_nav_recalucate_to_nav_teamdetail, bundle);
    }

    @Override
    public void reCalucate(TeamListReCalucateModel teamListReCalucateModel) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
