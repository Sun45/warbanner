package cn.sun45.warbanner.ui.fragments.combination;

import android.app.Activity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.combination.CombinationHelper;
import cn.sun45.warbanner.document.database.setup.models.TeamCustomizeModel;
import cn.sun45.warbanner.document.database.source.models.TeamModel;
import cn.sun45.warbanner.framework.ui.BaseActivity;
import cn.sun45.warbanner.framework.ui.BaseFragment;
import cn.sun45.warbanner.ui.shared.SharedViewModelClanwar;
import cn.sun45.warbanner.ui.shared.SharedViewModelSource;
import cn.sun45.warbanner.ui.views.combinationlist.CombinationGroupModel;
import cn.sun45.warbanner.ui.views.combinationlist.CombinationList;
import cn.sun45.warbanner.ui.views.combinationlist.CombinationListListener;
import cn.sun45.warbanner.ui.views.combinationlist.CombinationListModel;
import cn.sun45.warbanner.ui.views.listselectbar.ListSelectBar;
import cn.sun45.warbanner.ui.views.listselectbar.ListSelectBarListener;
import cn.sun45.warbanner.ui.views.listselectbar.ListSelectItem;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2023/9/23
 * 套餐Fragment
 */
public class CombinationFragment extends BaseFragment implements CombinationListListener, ListSelectBarListener {
    private SharedViewModelSource sharedSource;
    private SharedViewModelClanwar sharedClanwar;

    private CombinationHelper combinationHelper;

    private View mStageALay;
    private TextView mStageAText;
    private View mStageBLay;
    private TextView mStageBText;
    private View mAutoYesLay;
    private TextView mAutoYesText;
    private View mAutoMixLay;
    private TextView mAutoMixText;

    private CombinationList mCombinationList;
    private TextView mEmptyHint;
    private ListSelectBar mListSelectBar;

    private int currentStagePosition;
    private int currentAutoPosition;

    private List<CombinationGroupModel>[] combinationLists = new ArrayList[4];

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_combination;
    }

    @Override
    protected void initData() {
        combinationHelper = new CombinationHelper();
    }

    @Override
    protected void initView() {
        MaterialToolbar toolbar = mRoot.findViewById(R.id.drop_toolbar);
        ((BaseActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        mStageALay = mRoot.findViewById(R.id.stage_a_lay);
        mStageAText = mRoot.findViewById(R.id.stage_a_text);
        mStageBLay = mRoot.findViewById(R.id.stage_b_lay);
        mStageBText = mRoot.findViewById(R.id.stage_b_text);
        mAutoYesLay = mRoot.findViewById(R.id.auto_yes_lay);
        mAutoYesText = mRoot.findViewById(R.id.auto_yes_text);
        mAutoMixLay = mRoot.findViewById(R.id.auto_mix_lay);
        mAutoMixText = mRoot.findViewById(R.id.auto_mix_text);
        mCombinationList = mRoot.findViewById(R.id.combinationlist);
        mEmptyHint = mRoot.findViewById(R.id.empty_hint);
        mListSelectBar = mRoot.findViewById(R.id.listselectbar);

        mStageALay.setOnClickListener(v -> {
            selectStage(0);
            showList();
        });
        mStageBLay.setOnClickListener(v -> {
            selectStage(1);
            showList();
        });
        mAutoYesLay.setOnClickListener(v -> {
            selectAuto(0);
            showList();
        });
        mAutoMixLay.setOnClickListener(v -> {
            selectAuto(1);
            showList();
        });
        mCombinationList.setListener(this);
        mListSelectBar.setListener(this);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void dataRequest() {
        sharedSource = new ViewModelProvider(requireActivity()).get(SharedViewModelSource.class);
        sharedClanwar = new ViewModelProvider(requireActivity()).get(SharedViewModelClanwar.class);

        mCombinationList.setCharacterModels(sharedSource.characterList.getValue());
        selectStage(currentStagePosition);
        selectAuto(currentAutoPosition);
        showList();
    }

    @Override
    protected void onShow() {

    }

    @Override
    protected void onHide() {

    }

    private void selectStage(int position) {
        currentStagePosition = position;
        showTextSelect(false, mStageAText);
        showTextSelect(false, mStageBText);
        switch (position) {
            case 0:
                showTextSelect(true, mStageAText);
                break;
            case 1:
                showTextSelect(true, mStageBText);
                break;
        }
    }

    private void selectAuto(int position) {
        currentAutoPosition = position;
        showTextSelect(false, mAutoYesText);
        showTextSelect(false, mAutoMixText);
        switch (position) {
            case 0:
                showTextSelect(true, mAutoYesText);
                break;
            case 1:
                showTextSelect(true, mAutoMixText);
                break;
        }
    }

    private void showTextSelect(boolean select, TextView textView) {
        String str = textView.getText().toString();
        if (select) {
            SpannableStringBuilder builder = new SpannableStringBuilder(str);
            builder.setSpan(new ForegroundColorSpan(Utils.getAttrColor(getContext(), R.attr.colorSecondary)), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(builder);
        } else {
            textView.setText(str);
        }
    }

    private void showList() {
        int position = currentStagePosition * 2 + currentAutoPosition;
        List<CombinationGroupModel> list = combinationLists[position];
        if (list == null) {
            combinationLists[position] = new ArrayList<>();
            MaterialDialog materialDialog = new MaterialDialog(getContext(), MaterialDialog.getDEFAULT_BEHAVIOR());
            materialDialog.message(R.string.combination_analysing, null, null)
                    .cancelOnTouchOutside(false)
                    .show();
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    List<TeamModel> teamModels = sharedSource.teamList.getValue()
                            .stream().sorted(Comparator.comparing(TeamModel::getBoss)).collect(Collectors.toList());
                    List<TeamCustomizeModel> teamCustomizeModels = sharedClanwar.teamCustomizeList.getValue();
                    combinationLists[currentStagePosition * 2 + currentAutoPosition]
                            = combinationHelper.build(currentStagePosition + 1, currentAutoPosition + 1, teamModels, teamCustomizeModels);
                    Activity activity = getActivity();
                    if (activity != null) {
                        activity.runOnUiThread(() -> {
                            materialDialog.dismiss();
                            showList();
                        });
                    }
                }
            }.start();
        } else {
            mCombinationList.setData(list);
            if (list.isEmpty()) {
                mEmptyHint.setVisibility(View.VISIBLE);
            } else {
                mEmptyHint.setVisibility(View.GONE);
            }
        }
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
    public void open(CombinationListModel combinationListModel) {
        NavController controller = Navigation.findNavController(getView());
        Bundle bundle = new Bundle();
        bundle.putSerializable("combinationListModel", combinationListModel);
        controller.navigate(R.id.action_nav_combination_to_nav_combinationdetail, bundle);
    }

    @Override
    public void reCalucate(CombinationListModel combinationListModel) {
        NavController controller = Navigation.findNavController(getView());
        Bundle bundle = new Bundle();
        bundle.putIntegerArrayList("usedCharacterList", combinationListModel.getUsedCharacterList());
        controller.navigate(R.id.action_nav_combination_to_nav_recalucate, bundle);
    }

    @Override
    public void seek(int position) {
        mCombinationList.scrollToPosition(position);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sharedSource.characterList.removeObservers(requireActivity());
    }
}
