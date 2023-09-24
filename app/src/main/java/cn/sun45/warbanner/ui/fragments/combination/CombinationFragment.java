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

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.character.CharacterHelper;
import cn.sun45.warbanner.clanwar.ClanwarHelper;
import cn.sun45.warbanner.document.database.setup.models.ScreenCharacterModel;
import cn.sun45.warbanner.document.database.setup.models.TeamCustomizeModel;
import cn.sun45.warbanner.document.database.source.models.TeamModel;
import cn.sun45.warbanner.document.preference.SetupPreference;
import cn.sun45.warbanner.document.statics.charactertype.CharacterOwnType;
import cn.sun45.warbanner.framework.ui.BaseActivity;
import cn.sun45.warbanner.framework.ui.BaseFragment;
import cn.sun45.warbanner.ui.shared.SharedViewModelClanwar;
import cn.sun45.warbanner.ui.shared.SharedViewModelSource;
import cn.sun45.warbanner.ui.views.combinationlist.CombinationList;
import cn.sun45.warbanner.ui.views.combinationlist.CombinationListListener;
import cn.sun45.warbanner.ui.views.combinationlist.CombinationListModel;
import cn.sun45.warbanner.ui.views.combinationlist.selectbar.CombinationListSelectBar;
import cn.sun45.warbanner.ui.views.combinationlist.selectbar.CombinationListSelectBarListener;
import cn.sun45.warbanner.ui.views.combinationlist.selectbar.CombinationListSelectItem;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2023/9/23
 * 套餐Fragment
 */
public class CombinationFragment extends BaseFragment implements CombinationListListener, CombinationListSelectBarListener {
    private static final int TIMEMIN = 110;
    private static final int TIMEMAX = 160;

    private SharedViewModelSource sharedSource;
    private SharedViewModelClanwar sharedClanwar;

    private View mStageALay;
    private TextView mStageAText;
    private View mStageBLay;
    private TextView mStageBText;
    private View mAutoYesLay;
    private TextView mAutoYesText;
    private View mAutoMixLay;
    private TextView mAutoMixText;

    private CombinationList mCombinationList;
    private CombinationListSelectBar mListSelectBar;

    private int currentStagePosition;
    private int currentAutoPosition;

    private List<CombinationListModel>[] combinationListModelsList = new ArrayList[4];

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_combination;
    }

    @Override
    protected void initData() {
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

        mStageALay = mRoot.findViewById(R.id.stage_a_lay);
        mStageAText = mRoot.findViewById(R.id.stage_a_text);
        mStageBLay = mRoot.findViewById(R.id.stage_b_lay);
        mStageBText = mRoot.findViewById(R.id.stage_b_text);
        mAutoYesLay = mRoot.findViewById(R.id.auto_yes_lay);
        mAutoYesText = mRoot.findViewById(R.id.auto_yes_text);
        mAutoMixLay = mRoot.findViewById(R.id.auto_mix_lay);
        mAutoMixText = mRoot.findViewById(R.id.auto_mix_text);
        mCombinationList = mRoot.findViewById(R.id.combinationlist);
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
        List<CombinationListModel> list = combinationListModelsList[position];
        if (list == null) {
            combinationListModelsList[position] = new ArrayList<>();
            Utils.tip(getView(), R.string.combination_analysing);
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    combinationListModelsList[currentStagePosition * 2 + currentAutoPosition]
                            = analyse(currentStagePosition + 1, currentAutoPosition + 1);
                    Activity activity = getActivity();
                    if (activity != null) {
                        activity.runOnUiThread(() -> showList());
                    }
                }
            }.start();
        } else {
            Utils.tip(getView(), "数量：" + list.size());
            mCombinationList.setData(list);
        }
    }

    private List<CombinationListModel> analyse(int stage, int auto) {
        logD("analyse stage:" + stage + " auto:" + auto);
        List<CombinationElementModel> elementModelModels = new ArrayList<>();
        Map<Integer, Integer> screenCharacterMap = new TreeMap<>();
        if (new SetupPreference().isCharacterscreenenable()) {
            List<ScreenCharacterModel> screenCharacterModelList = CharacterHelper.getScreenCharacterList();
            for (ScreenCharacterModel screenCharacterModel : screenCharacterModelList) {
                screenCharacterMap.put(screenCharacterModel.getCharacterId(), screenCharacterModel.getType());
            }
        }
        List<TeamModel> teamModels = sharedSource.teamList.getValue()
                .stream().sorted(Comparator.comparing(TeamModel::getBoss)).collect(Collectors.toList());
        List<TeamCustomizeModel> teamCustomizeModels = sharedClanwar.teamCustomizeList.getValue();
        Map<String, Set<String>[]> compareMap = new HashMap<>();
        for (TeamModel teamModel : teamModels) {
            if (teamModel.getStage() != stage) {
                continue;
            }
            if (auto == 1) {
                if (!teamModel.isAuto()) {
                    continue;
                }
            }
            List<TeamModel.TimeLine> timeLines = teamModel.getTimeLines();
            int returnTime = 0;
            for (int i = 0; i < timeLines.size(); i++) {
                TeamModel.TimeLine timeLine = timeLines.get(i);
                if (timeLine.getReturnTime() != 0) {
                    if (timeLine.getReturnTime() > returnTime) {
                        returnTime = timeLine.getReturnTime();
                    }
                } else {
                    timeLines.remove(i);
                    i--;
                }
            }
            if (timeLines.isEmpty()) {
                continue;
            }
            compareMap.put(teamModel.getSn(), new HashSet[]{new HashSet<String>(), new HashSet<String>()});
            TeamCustomizeModel teamCustomizeModel = ClanwarHelper.findCustomizeModel(teamModel, teamCustomizeModels);
            if (teamCustomizeModel != null && teamCustomizeModel.isBlock()) {
                continue;
            }
            List<Integer> idlist = Arrays.asList(teamModel.getCharacterone(), teamModel.getCharactertwo(), teamModel.getCharacterthree(), teamModel.getCharacterfour(), teamModel.getCharacterfive());
            int screencharacter = 0;
            boolean notuseable = false;
            for (int i = 0; i < idlist.size(); i++) {
                int id = idlist.get(i);
                if (screenCharacterMap.containsKey(id)) {
                    if (screenCharacterMap.get(id) == CharacterOwnType.TYPE_LACK.getScreenType().getType()) {
                        if (screencharacter != 0) {
                            notuseable = true;
                            break;
                        } else {
                            screencharacter = id;
                        }
                    } else {
                        notuseable = true;
                        break;
                    }
                }
            }
            if (notuseable) {
                continue;
            }
            CombinationElementModel elementModel = new CombinationElementModel();
            elementModel.setTeamModel(teamModel);
            elementModel.setTimeLines(timeLines);
            elementModel.setIdlist(idlist);
            elementModel.setScreencharacter(screencharacter);
            elementModel.setReturnTime(returnTime);
            elementModelModels.add(elementModel);
        }
        logD("elementModelModels " + elementModelModels.size());
        List<CombinationListModel> list = new ArrayList<>();
        for (int ia = 0; ia < elementModelModels.size(); ia++) {
            CombinationElementModel elementia = elementModelModels.get(ia);
            for (int ib = ia; ib < elementModelModels.size(); ib++) {
                CombinationElementModel elementib = elementModelModels.get(ib);
                if (!compare(elementia, elementib, false)) {
                    continue;
                }
                for (int ja = ia + 1; ja < elementModelModels.size(); ja++) {
                    CombinationElementModel elementja = elementModelModels.get(ja);
                    if (!compare(elementia, elementja, compareMap) || !compare(elementib, elementja, compareMap)) {
                        continue;
                    }
                    for (int jb = ja; jb < elementModelModels.size(); jb++) {
                        CombinationElementModel elementjb = elementModelModels.get(jb);
                        if (!compare(elementja, elementjb, false)) {
                            continue;
                        }
                        if (!compare(elementia, elementjb, compareMap) || !compare(elementib, elementjb, compareMap)) {
                            continue;
                        }
                        for (int ka = ja + 1; ka < elementModelModels.size(); ka++) {
                            CombinationElementModel elementka = elementModelModels.get(ka);
                            if (!compare(elementia, elementka, compareMap) || !compare(elementib, elementka, compareMap)
                                    || !compare(elementja, elementka, compareMap) || !compare(elementjb, elementka, compareMap)) {
                                continue;
                            }
                            for (int kb = ka; kb < elementModelModels.size(); kb++) {
                                CombinationElementModel elementkb = elementModelModels.get(kb);
                                if (!compare(elementka, elementkb, true)) {
                                    continue;
                                }
                                if (!compare(elementia, elementkb, compareMap) || !compare(elementib, elementkb, compareMap)
                                        || !compare(elementja, elementkb, compareMap) || !compare(elementjb, elementkb, compareMap)) {
                                    continue;
                                }
                                int[] borrowIds = new int[6];
                                if (compare(elementia, elementib, elementja, elementjb, elementka, elementkb, borrowIds)) {
                                    CombinationListModel combinationListModel = new CombinationListModel(
                                            elementia, elementib, elementja, elementjb, elementka, elementkb
                                            , borrowIds);
                                    list.add(combinationListModel);
                                }
                            }
                        }
                    }
                }
            }
        }
        logD("list:" + list.size());
        return list;
    }

    public List<Integer> getRepeatIds(List<Integer> idsOne, List<Integer> idsTwo) {
        List<Integer> repeatIdList = new ArrayList<>();
        for (int idOne : idsOne) {
            for (int idTwo : idsTwo) {
                if (idOne == idTwo) {
                    repeatIdList.add(idOne);
                }
            }
        }
        return repeatIdList;
    }

    public List<Integer> getRepeatIds(List<Integer> idsOne, Set<Integer> idsTwo, Set<Integer> idsThree) {
        List<Integer> repeatIdList = new ArrayList<>();
        for (int idOne : idsOne) {
            if (idsTwo.contains(idOne) || idsThree.contains(idOne)) {
                repeatIdList.add(idOne);
            }
        }
        return repeatIdList;
    }

    public boolean compare(CombinationElementModel elementOne, CombinationElementModel elementTwo, boolean cansame) {
        if (!cansame) {
            if (elementOne.getTeamModel().getBoss().equals(elementTwo.getTeamModel().getBoss())) {
                return false;
            }
        }
        if (elementOne.getTeamModel().getBoss().endsWith("1") && elementTwo.getTeamModel().getBoss().endsWith("2")) {
            return false;
        }
        if (elementOne.getTeamModel().getBoss().endsWith("1") && elementTwo.getTeamModel().getBoss().endsWith("3")) {
            return false;
        }
        if (elementOne.getTeamModel().getBoss().endsWith("2") && elementTwo.getTeamModel().getBoss().endsWith("3")) {
            return false;
        }
        if (elementOne.getReturnTime() + elementTwo.getReturnTime() < TIMEMIN || elementOne.getReturnTime() + elementTwo.getReturnTime() > TIMEMAX) {
            return false;
        }
        return true;
    }

    public boolean compare(CombinationElementModel elementOne, CombinationElementModel elementTwo, Map<String, Set<String>[]> compareMap) {
        String snOne = elementOne.getTeamModel().getSn();
        String snTwo = elementTwo.getTeamModel().getSn();
        if (snOne.equals(snTwo)) {
            return false;
        }
        if (elementOne.getTeamModel().getBoss().equals(elementTwo.getTeamModel().getBoss())) {
            return false;
        }
        if (compareMap.get(snOne)[0].contains(snTwo)) {
            return true;
        }
        if (compareMap.get(snOne)[1].contains(snTwo)) {
            return false;
        }
        boolean compare = compare(elementOne.getIdlist(), elementOne.getScreencharacter(), elementTwo.getIdlist(), elementTwo.getScreencharacter());
        if (compare) {
            compareMap.get(snOne)[0].add(snTwo);
            compareMap.get(snTwo)[0].add(snOne);
        } else {
            compareMap.get(snOne)[1].add(snTwo);
            compareMap.get(snTwo)[1].add(snOne);
        }
        return compare;
    }

    public boolean compare(List<Integer> idlistOne, int screencharacterOne, List<Integer> idlistTwo, int screencharacterTwo) {
        List<Integer> repeatIdList = getRepeatIds(idlistOne, idlistTwo);
        if (repeatIdList.size() > 2) {
            return false;
        }
        int borrowDemand = 0;
        if (screencharacterOne != 0) {
            repeatIdList.remove((Object) screencharacterOne);
            borrowDemand++;
        }
        if (screencharacterTwo != 0) {
            repeatIdList.remove((Object) screencharacterTwo);
            borrowDemand++;
        }
        borrowDemand += repeatIdList.size();
        if (borrowDemand > 2) {
            return false;
        }
        return true;
    }

    public boolean compare(CombinationElementModel elementOne, CombinationElementModel elementTwo
            , CombinationElementModel elementThree, CombinationElementModel elementFour
            , CombinationElementModel elementFive, CombinationElementModel elementSix
            , int[] borrowIds) {
        Set<Integer> idlista = new HashSet<>();
        for (Integer id : elementOne.getIdlist()) {
            idlista.add(id);
        }
        for (Integer id : elementTwo.getIdlist()) {
            idlista.add(id);
        }
        Set<Integer> idlistb = new HashSet<>();
        for (Integer id : elementThree.getIdlist()) {
            idlistb.add(id);
        }
        for (Integer id : elementFour.getIdlist()) {
            idlistb.add(id);
        }
        Set<Integer> idlistc = new HashSet<>();
        for (Integer id : elementFive.getIdlist()) {
            idlistc.add(id);
        }
        for (Integer id : elementSix.getIdlist()) {
            idlistc.add(id);
        }
        int borrowIdOne = elementOne.getScreencharacter();
        List<Integer> repeatIdListOne = getRepeatIds(elementOne.getIdlist(), idlistb, idlistc);
        int borrowIdTwo = elementTwo.getScreencharacter();
        List<Integer> repeatIdListTwo = getRepeatIds(elementTwo.getIdlist(), idlistb, idlistc);
        int borrowIdThree = elementThree.getScreencharacter();
        List<Integer> repeatIdListThree = getRepeatIds(elementThree.getIdlist(), idlista, idlistc);
        int borrowIdFour = elementFour.getScreencharacter();
        List<Integer> repeatIdListFour = getRepeatIds(elementFour.getIdlist(), idlista, idlistc);
        int borrowIdFive = elementFive.getScreencharacter();
        List<Integer> repeatIdListFive = getRepeatIds(elementFive.getIdlist(), idlista, idlistb);
        int borrowIdSix = elementSix.getScreencharacter();
        List<Integer> repeatIdListSix = getRepeatIds(elementSix.getIdlist(), idlista, idlistb);
        for (int borrowIndexOne = -1; borrowIndexOne < repeatIdListOne.size(); borrowIndexOne++) {
            if (borrowIndexOne == -1) {
                if (elementOne.getScreencharacter() == 0) {
                    continue;
                } else {
                    borrowIndexOne = repeatIdListOne.size();
                }
            } else {
                borrowIdOne = repeatIdListOne.get(borrowIndexOne);
            }
            for (int borrowIndexTwo = -1; borrowIndexTwo < repeatIdListTwo.size(); borrowIndexTwo++) {
                if (borrowIndexTwo == -1) {
                    if (elementTwo.getScreencharacter() == 0) {
                        continue;
                    } else {
                        borrowIndexTwo = repeatIdListTwo.size();
                    }
                } else {
                    borrowIdTwo = repeatIdListTwo.get(borrowIndexTwo);
                }
                for (int borrowIndexThree = -1; borrowIndexThree < repeatIdListThree.size(); borrowIndexThree++) {
                    if (borrowIndexThree == -1) {
                        if (elementThree.getScreencharacter() == 0) {
                            continue;
                        } else {
                            borrowIndexThree = repeatIdListThree.size();
                        }
                    } else {
                        borrowIdThree = repeatIdListThree.get(borrowIndexThree);
                    }
                    for (int borrowIndexFour = -1; borrowIndexFour < repeatIdListFour.size(); borrowIndexFour++) {
                        if (borrowIndexFour == -1) {
                            if (elementFour.getScreencharacter() == 0) {
                                continue;
                            } else {
                                borrowIndexFour = repeatIdListFour.size();
                            }
                        } else {
                            borrowIdFour = repeatIdListFour.get(borrowIndexFour);
                        }
                        for (int borrowIndexFive = -1; borrowIndexFive < repeatIdListFive.size(); borrowIndexFive++) {
                            if (borrowIndexFive == -1) {
                                if (elementFive.getScreencharacter() == 0) {
                                    continue;
                                } else {
                                    borrowIndexFive = repeatIdListFive.size();
                                }
                            } else {
                                borrowIdFive = repeatIdListFive.get(borrowIndexFive);
                            }
                            for (int borrowIndexSix = -1; borrowIndexSix < repeatIdListSix.size(); borrowIndexSix++) {
                                if (borrowIndexSix == -1) {
                                    if (elementSix.getScreencharacter() == 0) {
                                        continue;
                                    } else {
                                        borrowIndexSix = repeatIdListSix.size();
                                    }
                                } else {
                                    borrowIdSix = repeatIdListSix.get(borrowIndexSix);
                                }
                                Set<Integer> seta = new HashSet<>();
                                for (Integer id : repeatIdListOne) {
                                    if (id != borrowIdOne) {
                                        seta.add(id);
                                    }
                                }
                                for (Integer id : repeatIdListTwo) {
                                    if (id != borrowIdTwo) {
                                        seta.add(id);
                                    }
                                }
                                Set<Integer> setb = new HashSet<>();
                                for (Integer id : repeatIdListThree) {
                                    if (id != borrowIdThree) {
                                        if (seta.contains(id)) {
                                            return false;
                                        }
                                        setb.add(id);
                                    }
                                }
                                for (Integer id : repeatIdListFour) {
                                    if (id != borrowIdFour) {
                                        if (seta.contains(id)) {
                                            return false;
                                        }
                                        setb.add(id);
                                    }
                                }
                                for (Integer id : repeatIdListFive) {
                                    if (id != borrowIdFive) {
                                        if (seta.contains(id) || setb.contains(id)) {
                                            return false;
                                        }
                                    }
                                }
                                for (Integer id : repeatIdListSix) {
                                    if (id != borrowIdSix) {
                                        if (seta.contains(id) || setb.contains(id)) {
                                            return false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        borrowIds[0] = borrowIdOne;
        borrowIds[1] = borrowIdTwo;
        borrowIds[2] = borrowIdThree;
        borrowIds[3] = borrowIdFour;
        borrowIds[4] = borrowIdFive;
        borrowIds[5] = borrowIdSix;
        return true;
    }

    @Override
    public void dataSet(int count, List<CombinationListSelectItem> listSelectItems) {
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
    public void seek(int position) {
        mCombinationList.scrollToPosition(position);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sharedSource.characterList.removeObservers(requireActivity());
    }
}
