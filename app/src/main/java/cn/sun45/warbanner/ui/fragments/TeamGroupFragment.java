package cn.sun45.warbanner.ui.fragments;

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

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.list.DialogMultiChoiceExtKt;
import com.google.android.material.appbar.MaterialToolbar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.document.db.clanwar.TeamGroupModel;
import cn.sun45.warbanner.document.db.clanwar.TeamModel;
import cn.sun45.warbanner.document.db.setup.ScreenCharacterModel;
import cn.sun45.warbanner.document.db.source.CharacterModel;
import cn.sun45.warbanner.document.preference.SetupPreference;
import cn.sun45.warbanner.framework.MyApplication;
import cn.sun45.warbanner.framework.document.db.DbHelper;
import cn.sun45.warbanner.framework.ui.BaseActivity;
import cn.sun45.warbanner.framework.ui.BaseFragment;
import cn.sun45.warbanner.ui.shared.SharedViewModelCharacterModelList;
import cn.sun45.warbanner.ui.shared.SharedViewModelSetup;
import cn.sun45.warbanner.ui.shared.SharedViewModelTeamList;
import cn.sun45.warbanner.ui.views.teamgrouplist.TeamGroupList;
import cn.sun45.warbanner.ui.views.teamgrouplist.TeamGroupListElementModel;
import cn.sun45.warbanner.ui.views.teamgrouplist.TeamGroupListListener;
import cn.sun45.warbanner.ui.views.teamgrouplist.TeamGroupListModel;
import cn.sun45.warbanner.util.Utils;
import kotlin.Unit;
import kotlin.jvm.functions.Function3;

/**
 * Created by Sun45 on 2021/5/30
 * 分刀Fragment
 */
public class TeamGroupFragment extends BaseFragment implements TeamGroupListListener {
    private SharedViewModelCharacterModelList sharedCharacterModelList;
    private SharedViewModelTeamList sharedTeamList;
    private SharedViewModelSetup sharedSetup;

    private boolean characterscreenenable;

    private TeamGroupList mTeamGroupList;

    private TextView mState;

    private List<TeamGroupModel> collectionlist;

    private List<TeamGroupListModel> list;

    private static final int interruptsize = 1000;

    private boolean progressinterrupt;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_teamgroup;
    }

    @Override
    protected void initData() {
        setHasOptionsMenu(true);
        characterscreenenable = new SetupPreference().isCharacterscreenenable();
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
        sharedCharacterModelList = new ViewModelProvider(requireActivity()).get(SharedViewModelCharacterModelList.class);
        sharedTeamList = new ViewModelProvider(requireActivity()).get(SharedViewModelTeamList.class);
        sharedSetup = new ViewModelProvider(requireActivity()).get(SharedViewModelSetup.class);

        if (list != null) {
            showresult();
        }
        sharedSetup.screencharacterlist.observe(requireActivity(), new Observer<List<ScreenCharacterModel>>() {
            @Override
            public void onChanged(List<ScreenCharacterModel> screenCharacterModelList) {
                sharedTeamList.teamList.observe(requireActivity(), new Observer<List<TeamModel>>() {
                    @Override
                    public void onChanged(List<TeamModel> teamModels) {
                        sharedCharacterModelList.characterlist.observe(requireActivity(), new Observer<List<CharacterModel>>() {
                            @Override
                            public void onChanged(List<CharacterModel> characterModels) {
                                mTeamGroupList.setCharacterModels(characterModels);
                                mState.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mState.setClickable(false);
                                        mState.setText(R.string.character_screen_state_progressing);
                                        new Thread() {
                                            @Override
                                            public void run() {
                                                super.run();
                                                collectionlist = DbHelper.query(getContext(), TeamGroupModel.class);
                                                progressinterrupt = false;
                                                buildElementList(teamModels);
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

    private CharacterModel findCharacter(String nickname) {
        CharacterModel characterModel = null;
        boolean find = false;
        for (CharacterModel model : sharedCharacterModelList.characterlist.getValue()) {
            for (String str : model.getNicknames()) {
                if (nickname.equals(str)) {
                    find = true;
                    break;
                }
            }
            if (find) {
                characterModel = model;
                break;
            }
        }
        return characterModel;
    }

    /**
     * 构建分刀元素列表
     */
    private void buildElementList(List<TeamModel> teamModels) {
        logD("buildElementList");
        List<TeamGroupListElementModel> elementModels = new ArrayList<>();
        boolean stageonescreen = new SetupPreference().isStageonescreen();
        boolean stagetwoscreen = new SetupPreference().isStagetwoscreen();
        boolean stagethreescreen = new SetupPreference().isStagethreescreen();
        for (TeamModel teamModel : teamModels) {
            int stage = teamModel.getStage();
            switch (stage) {
                case 1:
                    if (!stageonescreen) {
                        continue;
                    }
                    break;
                case 2:
                    if (!stagetwoscreen) {
                        continue;
                    }
                    break;
                case 3:
                    if (!stagethreescreen) {
                        continue;
                    }
                    break;
            }
            CharacterModel characterone = findCharacter(teamModel.getCharacterone());
            CharacterModel charactertwo = findCharacter(teamModel.getCharactertwo());
            CharacterModel characterthree = findCharacter(teamModel.getCharacterthree());
            CharacterModel characterfour = findCharacter(teamModel.getCharacterfour());
            CharacterModel characterfive = findCharacter(teamModel.getCharacterfive());
            if (characterone != null
                    && charactertwo != null
                    && characterthree != null
                    && characterfour != null
                    && characterfive != null) {
                List<Integer> idlist = new ArrayList<>();
                idlist.add(characterone.getId());
                idlist.add(charactertwo.getId());
                idlist.add(characterthree.getId());
                idlist.add(characterfour.getId());
                idlist.add(characterfive.getId());
                int screencharacter = 0;
                if (characterscreenenable) {
                    boolean morethanone = false;
                    for (int i = 0; i < idlist.size(); i++) {
                        int id = idlist.get(i);
                        boolean find = false;
                        for (ScreenCharacterModel screenCharacterModel : sharedSetup.screencharacterlist.getValue()) {
                            if (id == screenCharacterModel.getId()) {
                                find = true;
                                break;
                            }
                        }
                        if (find) {
                            if (screencharacter != 0) {
                                morethanone = true;
                                break;
                            } else {
                                screencharacter = id;
                            }
                        }
                    }
                    if (morethanone) {
                        continue;
                    }
                }
                TeamGroupListElementModel elementModel = new TeamGroupListElementModel();
                elementModel.setIdlist(idlist);
                elementModel.setScreencharacter(screencharacter);
                elementModel.setTeamModel(teamModel);
                elementModels.add(elementModel);
            }
        }
        buildTeamGroupList(elementModels);
    }

    /**
     * 构建分刀数据
     */
    private void buildTeamGroupList(List<TeamGroupListElementModel> elementModelList) {
        logD("buildTeamGroupList");
        int size = elementModelList.size();
        list = new ArrayList<>();
        List<String> bossscreentask = new ArrayList<>();
        if (new SetupPreference().isBossonescreen()) {
            bossscreentask.add("1");
        }
        if (new SetupPreference().isBosstwoscreen()) {
            bossscreentask.add("2");
        }
        if (new SetupPreference().isBossthreescreen()) {
            bossscreentask.add("3");
        }
        if (new SetupPreference().isBossfourscreen()) {
            bossscreentask.add("4");
        }
        if (new SetupPreference().isBossfivescreen()) {
            bossscreentask.add("5");
        }
        boolean usenotauto = new SetupPreference().isNotautoscreen();
        boolean useauto = new SetupPreference().isAutoscreen();
        for (int i = 0; i < size; i++) {
            logD("i：" + i);
            TeamGroupListElementModel one = elementModelList.get(i);
            if (!autoscreencompatible(usenotauto, useauto, one)) {
                continue;
            }
            List<String> provide = new ArrayList<>();
            for (int j = i + 1; j < elementModelList.size(); j++) {
                TeamGroupListElementModel two = elementModelList.get(j);
                if (!autoscreencompatible(usenotauto, useauto, two)) {
                    continue;
                }
                provide.clear();
                provide.add(one.getTeamModel().getBoss().substring(1, 2));
                provide.add(two.getTeamModel().getBoss().substring(1, 2));
                if (!bossscreencompatible(bossscreentask, provide)) {
                    continue;
                }
                if (compatible(one, two)) {
                    for (int k = j + 1; k < elementModelList.size(); k++) {
                        TeamGroupListElementModel three = elementModelList.get(k);
                        if (!autoscreencompatible(usenotauto, useauto, three)) {
                            continue;
                        }
                        provide.clear();
                        provide.add(one.getTeamModel().getBoss().substring(1, 2));
                        provide.add(two.getTeamModel().getBoss().substring(1, 2));
                        provide.add(three.getTeamModel().getBoss().substring(1, 2));
                        if (!bossscreencompatible(bossscreentask, provide)) {
                            continue;
                        }
                        TeamGroupListModel teamGroupListModel = compatible(one, two, three);
                        if (teamGroupListModel != null) {
                            int n = -1;
                            for (int m = 0; m < list.size(); m++) {
                                if (teamGroupListModel.getTotaldamage() >= list.get(m).getTotaldamage()) {
                                    n = m;
                                    break;
                                }
                            }
                            if (n != -1) {
                                list.add(n, teamGroupListModel);
                            } else {
                                list.add(teamGroupListModel);
                            }
                            if (list.size() == interruptsize) {
                                progressinterrupt = true;
                                break;
                            }
                        }
                    }
                    if (progressinterrupt) {
                        break;
                    }
                }
            }
            if (progressinterrupt) {
                break;
            }
        }
        logD("buildTeamGroupList " + list.size());
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

    private void showresult() {
        if (progressinterrupt) {
            mState.setText(Utils.getStringWithPlaceHolder(R.string.character_screen_state_interrupt_finish, list.size()));
        } else {
            mState.setText(Utils.getStringWithPlaceHolder(R.string.character_screen_state_finish, list.size()));
        }
        mState.setClickable(true);
        mTeamGroupList.setData(list);
    }

    /**
     * 满足BOSS筛选
     */
    private boolean bossscreencompatible(List<String> task, List<String> provide) {
        for (String a : provide) {
            boolean find = false;
            for (String b : task) {
                if (a.equals(b)) {
                    find = true;
                }
            }
            if (!find) {
                return false;
            }
        }
        if (provide.size() == 3) {
            for (String a : task) {
                boolean find = false;
                for (String b : provide) {
                    if (a.equals(b)) {
                        find = true;
                        break;
                    }
                }
                if (!find) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 满足auto筛选
     */
    private boolean autoscreencompatible(boolean usenotauto, boolean useauto, TeamGroupListElementModel model) {
        if (!usenotauto && !model.getTeamModel().isAuto()) {
            return false;
        }
        if (!useauto && model.getTeamModel().isAuto()) {
            return false;
        }
        return true;
    }

    /**
     * 获取id重复列表
     */
    private List<Integer> getRepeatList(List<Integer> listone, List<Integer> listtwo) {
        List<Integer> list = new ArrayList<>();
        for (int idone : listone) {
            for (int idtwo : listtwo) {
                if (idone == idtwo) {
                    list.add(idone);
                }
            }
        }
        return list;
    }

    private boolean compatible(TeamGroupListElementModel elementone, TeamGroupListElementModel
            elementtwo) {
        List<Integer> repeatList = getRepeatList(elementone.getIdlist(), elementtwo.getIdlist());
        if (repeatList.size() >= 3) {
            return false;
        }
        int screencharacterone = elementone.getScreencharacter();
        int screencharactertwo = elementtwo.getScreencharacter();
        int screensize = 0;
        if (screencharacterone != 0) {
            repeatList.remove((Object) screencharacterone);
            screensize++;
        }
        if (screencharactertwo != 0) {
            repeatList.remove((Object) screencharactertwo);
            screensize++;
        }
        boolean compatible = repeatList.size() + screensize <= 2;
        return compatible;
    }

    private TeamGroupListModel compatible(TeamGroupListElementModel elementone, TeamGroupListElementModel elementtwo, TeamGroupListElementModel elementthree) {
//        if (elementone.getTeamModel().getNumber().equals("a103") && elementtwo.getTeamModel().getNumber().equals("a111") && elementthree.getTeamModel().getNumber().equals("a307")) {
//            int a = 0;
//            int b = a;
//        }
        List<Integer> repeatListab = getRepeatList(elementone.getIdlist(), elementtwo.getIdlist());
        if (repeatListab.size() >= 3) {
            return null;
        }
        List<Integer> repeatListac = getRepeatList(elementone.getIdlist(), elementthree.getIdlist());
        if (repeatListac.size() >= 3) {
            return null;
        }
        List<Integer> repeatListbc = getRepeatList(elementtwo.getIdlist(), elementthree.getIdlist());
        if (repeatListbc.size() >= 3) {
            return null;
        }
        List<Integer> repeatListabc = getRepeatList(repeatListab, elementthree.getIdlist());
        if (repeatListabc.size() >= 2) {
            return null;
        }
        for (Integer idabc : repeatListabc) {
            repeatListab.remove((Object) idabc);
            repeatListac.remove((Object) idabc);
            repeatListbc.remove((Object) idabc);
        }
        int screencharacterone = elementone.getScreencharacter();
        int screencharactertwo = elementtwo.getScreencharacter();
        int screencharacterthree = elementthree.getScreencharacter();
        int screensize = 0;
        if (screencharacterone != 0) {
            repeatListab.remove((Object) screencharacterone);
            repeatListac.remove((Object) screencharacterone);
            repeatListabc.remove((Object) screencharacterone);
            screensize++;
        }
        if (screencharactertwo != 0) {
            repeatListab.remove((Object) screencharactertwo);
            repeatListbc.remove((Object) screencharactertwo);
            repeatListabc.remove((Object) screencharactertwo);
            screensize++;
        }
        if (screencharacterthree != 0) {
            repeatListac.remove((Object) screencharacterthree);
            repeatListbc.remove((Object) screencharacterthree);
            repeatListabc.remove((Object) screencharacterthree);
            screensize++;
        }
        if (screencharacterone != 0 && screencharactertwo != 0) {
            if (!repeatListab.isEmpty()) {
                return null;
            }
        }
        if (screencharactertwo != 0 && screencharacterthree != 0) {
            if (!repeatListbc.isEmpty()) {
                return null;
            }
        }
        if (screencharacterone != 0 && screencharacterthree != 0) {
            if (!repeatListac.isEmpty()) {
                return null;
            }
        }
        if (screencharacterone != 0) {
            if (repeatListab.size() == 2 || repeatListac.size() == 2) {
                return null;
            }
        }
        if (screencharactertwo != 0) {
            if (repeatListab.size() == 2 || repeatListbc.size() == 2) {
                return null;
            }
        }
        if (screencharacterthree != 0) {
            if (repeatListac.size() == 2 || repeatListbc.size() == 2) {
                return null;
            }
        }
        boolean compatible = repeatListabc.size() * 2 + repeatListab.size() + repeatListac.size() + repeatListbc.size() + screensize <= 3;
        if (compatible) {
            if (screencharacterone == 0) {
                if (repeatListabc.size() == 1) {
                    screencharacterone = repeatListabc.get(0);
                    repeatListabc.remove(0);
                    repeatListbc.add(screencharacterone);
                } else {
                    if (repeatListab.size() == 2) {
                        screencharacterone = repeatListab.get(0);
                        repeatListab.remove(0);
                    } else if (repeatListac.size() == 2) {
                        screencharacterone = repeatListac.get(0);
                        repeatListac.remove(0);
                    } else {
                        if (screencharactertwo != 0) {
                            if (repeatListab.size() == 1) {
                                screencharacterone = repeatListab.get(0);
                                repeatListab.remove(0);
                            } else if (repeatListac.size() == 1) {
                                screencharacterone = repeatListac.get(0);
                                repeatListac.remove(0);
                            } else {
                                screencharacterone = elementone.getIdlist().get(0);
                            }
                        } else if (screencharacterthree != 0) {
                            if (repeatListac.size() == 1) {
                                screencharacterone = repeatListac.get(0);
                                repeatListac.remove(0);
                            } else if (repeatListab.size() == 1) {
                                screencharacterone = repeatListab.get(0);
                                repeatListab.remove(0);
                            } else {
                                screencharacterone = elementone.getIdlist().get(0);
                            }
                        } else {
                            if (repeatListab.size() == 1) {
                                screencharacterone = repeatListab.get(0);
                                repeatListab.remove(0);
                            } else if (repeatListac.size() == 1) {
                                screencharacterone = repeatListac.get(0);
                                repeatListac.remove(0);
                            } else {
                                screencharacterone = elementone.getIdlist().get(0);
                            }
                        }
                    }
                }
            }
            if (screencharactertwo == 0) {
                if (repeatListabc.size() == 1) {
                    screencharactertwo = repeatListabc.get(0);
                    repeatListabc.remove(0);
                    repeatListbc.add(screencharactertwo);
                } else {
                    if (repeatListab.size() == 1) {
                        screencharactertwo = repeatListab.get(0);
                        repeatListab.remove(0);
                    } else if (repeatListbc.size() == 2) {
                        screencharactertwo = repeatListbc.get(0);
                        repeatListbc.remove(0);
                    } else if (repeatListbc.size() == 1) {
                        screencharactertwo = repeatListbc.get(0);
                        repeatListbc.remove(0);
                    } else {
                        screencharactertwo = elementtwo.getIdlist().get(0);
                    }
                }
            }
            if (screencharacterthree == 0) {
                if (repeatListac.size() == 1) {
                    screencharacterthree = repeatListac.get(0);
                    repeatListac.remove(0);
                } else if (repeatListbc.size() == 1) {
                    screencharacterthree = repeatListbc.get(0);
                    repeatListbc.remove(0);
                } else {
                    screencharacterthree = elementthree.getIdlist().get(0);
                }
            }
            if (MyApplication.testing) {
                if (repeatListabc.size() > 0 || repeatListab.size() > 0 || repeatListac.size() > 0 || repeatListbc.size() > 0 || screencharacterone == 0 || screencharactertwo == 0 || screencharacterthree == 0) {
                    throw new RuntimeException("分刀计算遗漏");
                }
            }
            boolean collected = false;
            for (TeamGroupModel teamGroupModel : collectionlist) {
                if (teamGroupModel.getTeamone().equals(elementone.getTeamModel().getNumber())) {
                    if (teamGroupModel.getTeamtwo().equals(elementtwo.getTeamModel().getNumber())) {
                        if (teamGroupModel.getTeamthree().equals(elementthree.getTeamModel().getNumber())) {
                            collected = true;
                            break;
                        }
                    }
                }
            }
            return new TeamGroupListModel(elementone, screencharacterone, elementtwo, screencharactertwo, elementthree, screencharacterthree, collected);
        }
        return null;
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
            case R.id.menu_boss:
                showbossdialog();
                break;
            case R.id.menu_auto:
                showautodialog();
                break;
        }
        return true;
    }

    private void showbossdialog() {
        MaterialDialog dialog = new MaterialDialog(getContext(), MaterialDialog.getDEFAULT_BEHAVIOR());
        dialog.title(R.string.menu_boss_screen, null);
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
        DialogMultiChoiceExtKt.listItemsMultiChoice(dialog, R.array.menu_boss_screen_dialog_options, null, getBossdialogDisabledIndices(selectionlist), selectionlist, false, false, new Function3<MaterialDialog, int[], List<? extends CharSequence>, Unit>() {
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
                DialogMultiChoiceExtKt.updateListItemsMultiChoice(materialDialog, R.array.menu_boss_screen_dialog_options, null, getBossdialogDisabledIndices(ints), null);
                return null;
            }
        });
        dialog.positiveButton(R.string.menu_boss_screen_dialog_confirm, null, null);
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
        dialog.title(R.string.menu_auto_screen, null);
        List<Integer> selection = new ArrayList<>();
        if (new SetupPreference().isNotautoscreen()) {
            selection.add(0);
        }
        if (new SetupPreference().isAutoscreen()) {
            selection.add(1);
        }
        int[] selectionlist = new int[selection.size()];
        for (int i = 0; i < selection.size(); i++) {
            selectionlist[i] = selection.get(i);
        }
        DialogMultiChoiceExtKt.listItemsMultiChoice(dialog, R.array.menu_auto_screen_dialog_options, null, null, selectionlist, true, false, new Function3<MaterialDialog, int[], List<? extends CharSequence>, Unit>() {
            @Override
            public Unit invoke(MaterialDialog materialDialog, int[] ints, List<? extends CharSequence> charSequences) {
                new SetupPreference().setNotautoscreen(false);
                new SetupPreference().setAutoscreen(false);
                for (int which : ints) {
                    switch (which) {
                        case 0:
                            new SetupPreference().setNotautoscreen(true);
                            break;
                        case 1:
                            new SetupPreference().setAutoscreen(true);
                            break;
                        default:
                            break;
                    }
                }
                return null;
            }
        });
        dialog.positiveButton(R.string.menu_auto_screen_dialog_confirm, null, null);
        dialog.show();
    }

    @Override
    public void collect(TeamGroupListModel teamGroupListModel) {
        if (teamGroupListModel.isCollected()) {
            TeamGroupModel teamGroupModel = new TeamGroupModel();
            teamGroupModel.setTeamone(teamGroupListModel.getTeamone().getNumber());
            teamGroupModel.setBorrowindexone(teamGroupListModel.getBorrowindexone());
            teamGroupModel.setTeamtwo(teamGroupListModel.getTeamtwo().getNumber());
            teamGroupModel.setBorrowindextwo(teamGroupListModel.getBorrowindextwo());
            teamGroupModel.setTeamthree(teamGroupListModel.getTeamthree().getNumber());
            teamGroupModel.setBorrowindexthree(teamGroupListModel.getBorrowindexthree());
            DbHelper.insert(getContext(), teamGroupModel);
        } else {
            DbHelper.delete(getContext(), TeamGroupModel.class
                    , new String[]{"teamone", "teamtwo", "teamthree"}
                    , new String[]{
                            teamGroupListModel.getTeamone().getNumber(),
                            teamGroupListModel.getTeamtwo().getNumber(),
                            teamGroupListModel.getTeamthree().getNumber()
                    });
        }
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
        sharedCharacterModelList.characterlist.removeObservers(requireActivity());
        sharedTeamList.teamList.removeObservers(requireActivity());
        sharedSetup.screencharacterlist.removeObservers(requireActivity());
    }
}