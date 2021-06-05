package cn.sun45.warbanner.teamgroup;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.document.db.clanwar.TeamGroupModel;
import cn.sun45.warbanner.document.db.clanwar.TeamModel;
import cn.sun45.warbanner.document.db.setup.ScreenCharacterModel;
import cn.sun45.warbanner.document.db.source.CharacterModel;
import cn.sun45.warbanner.document.preference.SetupPreference;
import cn.sun45.warbanner.framework.MyApplication;
import cn.sun45.warbanner.ui.views.teamgrouplist.TeamGroupListModel;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2021/6/4
 * 分刀帮助类
 */
public class TeamGroupHelper {
    private static final String TAG = "TeamGroupHelper";

    //分刀中断阈值
    public static final int interruptsize = 6000;

    private boolean characterscreenenable;

    public TeamGroupHelper(boolean characterscreenenable) {
        this.characterscreenenable = characterscreenenable;
    }

    public List<TeamGroupListModel> build(List<ScreenCharacterModel> screenCharacterModelList, List<TeamModel> teamModels, List<CharacterModel> characterModels, List<TeamGroupModel> collectionlist) {
        return buildElementList(screenCharacterModelList, teamModels, characterModels, collectionlist);
    }

    /**
     * 构建分刀元素列表
     */
    private List<TeamGroupListModel> buildElementList(List<ScreenCharacterModel> screenCharacterModelList, List<TeamModel> teamModels, List<CharacterModel> characterModels, List<TeamGroupModel> collectionlist) {
        Utils.logD(TAG, "buildElementList");
        List<TeamGroupElementModel> elementModels = new ArrayList<>();
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
            CharacterModel characterone = findCharacter(teamModel.getCharacterone(), characterModels);
            CharacterModel charactertwo = findCharacter(teamModel.getCharactertwo(), characterModels);
            CharacterModel characterthree = findCharacter(teamModel.getCharacterthree(), characterModels);
            CharacterModel characterfour = findCharacter(teamModel.getCharacterfour(), characterModels);
            CharacterModel characterfive = findCharacter(teamModel.getCharacterfive(), characterModels);
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
                        for (ScreenCharacterModel screenCharacterModel : screenCharacterModelList) {
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
                TeamGroupElementModel elementModel = new TeamGroupElementModel();
                elementModel.setIdlist(idlist);
                elementModel.setScreencharacter(screencharacter);
                elementModel.setTeamModel(teamModel);
                elementModels.add(elementModel);
            }
        }
        return buildTeamGroupList(elementModels, collectionlist);
    }

    private CharacterModel findCharacter(String nickname, List<CharacterModel> characterModels) {
        CharacterModel characterModel = null;
        boolean find = false;
        for (CharacterModel model : characterModels) {
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
     * 构建分刀数据
     */
    private List<TeamGroupListModel> buildTeamGroupList(List<TeamGroupElementModel> elementModelList, List<TeamGroupModel> collectionlist) {
        Utils.logD(TAG, "buildTeamGroupList");
        List<TeamGroupListModel> list = new ArrayList<>();
        int size = elementModelList.size();
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
        boolean useautoscreen = new SetupPreference().isUseautoscreen();
        int targetautocount = new SetupPreference().getAutocount();
        int currentautocount = 0;
        for (int i = 0; i < size; i++) {
            TeamGroupElementModel one = elementModelList.get(i);
            if (useautoscreen) {
                currentautocount = 0;
                if (one.getTeamModel().isAuto()) {
                    currentautocount++;
                }
                if (!autoscreencompatible(targetautocount, currentautocount, 1)) {
                    continue;
                }
            }
            List<String> provide = new ArrayList<>();
            provide.add(one.getTeamModel().getBoss().substring(1, 2));
            if (!bossscreencompatible(bossscreentask, provide)) {
                continue;
            }
            for (int j = i + 1; j < elementModelList.size(); j++) {
                TeamGroupElementModel two = elementModelList.get(j);
                if (useautoscreen) {
                    currentautocount = 0;
                    if (one.getTeamModel().isAuto()) {
                        currentautocount++;
                    }
                    if (two.getTeamModel().isAuto()) {
                        currentautocount++;
                    }
                    if (!autoscreencompatible(targetautocount, currentautocount, 2)) {
                        continue;
                    }
                }
                provide.clear();
                provide.add(one.getTeamModel().getBoss().substring(1, 2));
                provide.add(two.getTeamModel().getBoss().substring(1, 2));
                if (!bossscreencompatible(bossscreentask, provide)) {
                    continue;
                }
                if (compatible(one, two)) {
                    for (int k = j + 1; k < elementModelList.size(); k++) {
                        TeamGroupElementModel three = elementModelList.get(k);
                        if (useautoscreen) {
                            currentautocount = 0;
                            if (one.getTeamModel().isAuto()) {
                                currentautocount++;
                            }
                            if (two.getTeamModel().isAuto()) {
                                currentautocount++;
                            }
                            if (three.getTeamModel().isAuto()) {
                                currentautocount++;
                            }
                            if (!autoscreencompatible(targetautocount, currentautocount, 3)) {
                                continue;
                            }
                        }
                        provide.clear();
                        provide.add(one.getTeamModel().getBoss().substring(1, 2));
                        provide.add(two.getTeamModel().getBoss().substring(1, 2));
                        provide.add(three.getTeamModel().getBoss().substring(1, 2));
                        if (!bossscreencompatible(bossscreentask, provide)) {
                            continue;
                        }
                        TeamGroupListModel teamGroupListModel = compatible(one, two, three, collectionlist);
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
                                break;
                            }
                        }
                    }
                    if (list.size() == interruptsize) {
                        break;
                    }
                }
            }
            if (list.size() == interruptsize) {
                break;
            }
        }
        return list;
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
    private boolean autoscreencompatible(int targetautocount, int currentautocount, int layer) {
        if (currentautocount > targetautocount) {
            return false;
        }
        if (layer - currentautocount > (3 - targetautocount)) {
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

    private boolean compatible(TeamGroupElementModel elementone, TeamGroupElementModel
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

    private TeamGroupListModel compatible(TeamGroupElementModel elementone, TeamGroupElementModel elementtwo, TeamGroupElementModel elementthree, List<TeamGroupModel> collectionlist) {
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
}
