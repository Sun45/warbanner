package cn.sun45.warbanner.teamgroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import cn.sun45.warbanner.character.CharacterHelper;
import cn.sun45.warbanner.clanwar.ClanwarHelper;
import cn.sun45.warbanner.document.database.setup.models.ScreenCharacterModel;
import cn.sun45.warbanner.document.database.setup.models.TeamCustomizeModel;
import cn.sun45.warbanner.document.database.setup.models.TeamGroupScreenModel;
import cn.sun45.warbanner.document.database.setup.models.TeamGroupScreenUsedCharacterModel;
import cn.sun45.warbanner.document.database.source.models.TeamModel;
import cn.sun45.warbanner.document.statics.charactertype.CharacterOwnType;
import cn.sun45.warbanner.document.statics.charactertype.CharacterUseType;
import cn.sun45.warbanner.framework.MyApplication;
import cn.sun45.warbanner.stage.StageManager;
import cn.sun45.warbanner.ui.views.teamgrouplist.TeamGroupListModel;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2021/6/4
 * 分刀帮助类
 */
public class TeamGroupHelper {
    private static final String TAG = "TeamGroupHelper";

    //分刀中断阈值
    public static final int interruptsize = 10_0000;

    private boolean characterscreenenable;

    public TeamGroupHelper(boolean characterscreenenable) {
        this.characterscreenenable = characterscreenenable;
    }

    public List<TeamGroupListModel> build(
            List<TeamModel> teamModels, List<TeamCustomizeModel> teamCustomizeModels) {
        List<ScreenCharacterModel> screenCharacterModelList = CharacterHelper.getScreenCharacterList();
        List<TeamGroupScreenUsedCharacterModel> usedCharacterModelList = CharacterHelper.getUsedCharacterList();
        TeamGroupScreenModel teamGroupScreenModel = ClanwarHelper.getScreenModel();
        return buildElementList(
                screenCharacterModelList, usedCharacterModelList,
                teamModels, teamCustomizeModels, teamGroupScreenModel);
    }

    /**
     * 构建分刀元素列表
     */
    private List<TeamGroupListModel> buildElementList(
            List<ScreenCharacterModel> screenCharacterModelList,
            List<TeamGroupScreenUsedCharacterModel> usedCharacterModelList,
            List<TeamModel> teamModels, List<TeamCustomizeModel> teamCustomizeModels, TeamGroupScreenModel teamGroupScreenModel) {
        Utils.logD(TAG, "buildElementList");
        Map<Integer, Integer> screenCharacterMap = new TreeMap<>();
        if (characterscreenenable) {
            for (ScreenCharacterModel screenCharacterModel : screenCharacterModelList) {
                screenCharacterMap.put(screenCharacterModel.getCharacterId(), screenCharacterModel.getType());
            }
        }
        List<Integer> usingList = new ArrayList<>();
        List<Integer> usedList = new ArrayList<>();
        for (TeamGroupScreenUsedCharacterModel teamGroupScreenUsedCharacterModel : usedCharacterModelList) {
            if (teamGroupScreenUsedCharacterModel.getType() == CharacterUseType.TYPE_USING.getScreenType().getType()) {
                usingList.add(teamGroupScreenUsedCharacterModel.getCharacterId());
            } else if (teamGroupScreenUsedCharacterModel.getType() == CharacterUseType.TYPE_USED.getScreenType().getType()) {
                usedList.add(teamGroupScreenUsedCharacterModel.getCharacterId());
            }
        }
        List<TeamGroupElementModel> elementModels = new ArrayList<>();
        for (TeamModel teamModel : teamModels) {
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
                if (usedList.contains(id)) {
                    if (screencharacter != 0) {
                        notuseable = true;
                        break;
                    } else {
                        screencharacter = id;
                    }
                }
            }
            if (notuseable) {
                continue;
            }
            TeamGroupElementModel elementModel = new TeamGroupElementModel();
            elementModel.setIdlist(idlist);
            elementModel.setScreencharacter(screencharacter);
            elementModel.setTeamModel(teamModel);
            elementModel.setTeamCustomizeModel(teamCustomizeModel);
            elementModels.add(elementModel);
        }
        if (elementModels.isEmpty()) {
            return null;
        }
        List<TeamGroupElementModel> elementModelListOne = new ArrayList<>();
        if (teamGroupScreenModel.isTeamoneenable()) {
            int borrowid = 0;
            switch (teamGroupScreenModel.getTeamoneborrowindex()) {
                case 0:
                    borrowid = teamGroupScreenModel.getTeamonecharacteroneid();
                    break;
                case 1:
                    borrowid = teamGroupScreenModel.getTeamonecharactertwoid();
                    break;
                case 2:
                    borrowid = teamGroupScreenModel.getTeamonecharacterthreeid();
                    break;
                case 3:
                    borrowid = teamGroupScreenModel.getTeamonecharacterfourid();
                    break;
                case 4:
                    borrowid = teamGroupScreenModel.getTeamonecharacterfiveid();
                    break;
                default:
                    break;
            }
            for (TeamGroupElementModel teamGroupElementModel : elementModels) {
                if (fit(teamGroupElementModel,
                        teamGroupScreenModel.isTeamoneextra(), usingList,
                        teamGroupScreenModel.getTeamonestage(), teamGroupScreenModel.getTeamoneboss(), teamGroupScreenModel.getTeamoneauto(),
                        teamGroupScreenModel.getTeamonecharacteroneid(), teamGroupScreenModel.getTeamonecharactertwoid(), teamGroupScreenModel.getTeamonecharacterthreeid(), teamGroupScreenModel.getTeamonecharacterfourid(), teamGroupScreenModel.getTeamonecharacterfiveid(),
                        borrowid)) {
                    elementModelListOne.add(teamGroupElementModel);
                }
            }
            if (elementModelListOne.isEmpty()) {
                return null;
            }
        }
        List<TeamGroupElementModel> elementModelListTwo = new ArrayList<>();
        if (teamGroupScreenModel.isTeamtwoenable()) {
            int borrowid = 0;
            switch (teamGroupScreenModel.getTeamtwoborrowindex()) {
                case 0:
                    borrowid = teamGroupScreenModel.getTeamtwocharacteroneid();
                    break;
                case 1:
                    borrowid = teamGroupScreenModel.getTeamtwocharactertwoid();
                    break;
                case 2:
                    borrowid = teamGroupScreenModel.getTeamtwocharacterthreeid();
                    break;
                case 3:
                    borrowid = teamGroupScreenModel.getTeamtwocharacterfourid();
                    break;
                case 4:
                    borrowid = teamGroupScreenModel.getTeamtwocharacterfiveid();
                    break;
                default:
                    break;
            }
            for (TeamGroupElementModel teamGroupElementModel : elementModels) {
                if (fit(teamGroupElementModel,
                        teamGroupScreenModel.isTeamtwoextra(), usingList,
                        teamGroupScreenModel.getTeamtwostage(), teamGroupScreenModel.getTeamtwoboss(), teamGroupScreenModel.getTeamtwoauto(),
                        teamGroupScreenModel.getTeamtwocharacteroneid(), teamGroupScreenModel.getTeamtwocharactertwoid(), teamGroupScreenModel.getTeamtwocharacterthreeid(), teamGroupScreenModel.getTeamtwocharacterfourid(), teamGroupScreenModel.getTeamtwocharacterfiveid(),
                        borrowid)) {
                    elementModelListTwo.add(teamGroupElementModel);
                }
            }
            if (elementModelListTwo.isEmpty()) {
                return null;
            }
        }
        List<TeamGroupElementModel> elementModelListThree = new ArrayList<>();
        if (teamGroupScreenModel.isTeamthreeenable()) {
            int borrowid = 0;
            switch (teamGroupScreenModel.getTeamthreeborrowindex()) {
                case 0:
                    borrowid = teamGroupScreenModel.getTeamthreecharacteroneid();
                    break;
                case 1:
                    borrowid = teamGroupScreenModel.getTeamthreecharactertwoid();
                    break;
                case 2:
                    borrowid = teamGroupScreenModel.getTeamthreecharacterthreeid();
                    break;
                case 3:
                    borrowid = teamGroupScreenModel.getTeamthreecharacterfourid();
                    break;
                case 4:
                    borrowid = teamGroupScreenModel.getTeamthreecharacterfiveid();
                    break;
                default:
                    break;
            }
            for (TeamGroupElementModel teamGroupElementModel : elementModels) {
                if (fit(teamGroupElementModel,
                        teamGroupScreenModel.isTeamthreeextra(), usingList,
                        teamGroupScreenModel.getTeamthreestage(), teamGroupScreenModel.getTeamthreeboss(), teamGroupScreenModel.getTeamthreeauto(),
                        teamGroupScreenModel.getTeamthreecharacteroneid(), teamGroupScreenModel.getTeamthreecharactertwoid(), teamGroupScreenModel.getTeamthreecharacterthreeid(), teamGroupScreenModel.getTeamthreecharacterfourid(), teamGroupScreenModel.getTeamthreecharacterfiveid(),
                        borrowid)) {
                    elementModelListThree.add(teamGroupElementModel);
                }
            }
            if (elementModelListThree.isEmpty()) {
                return null;
            }
        }
        int min = elementModels.get(0).getDamage();
        int max = min;
        for (int i = 0; i < elementModels.size(); i++) {
            TeamGroupElementModel teamGroupElementModel = elementModels.get(i);
            int damage = teamGroupElementModel.getDamage();
            if (damage < min) {
                min = damage;
            }
            if (damage > max) {
                max = damage;
            }
        }
        int count = 0;
        List<List<TeamGroupElementModel>> list = new ArrayList<>();
        if (!elementModelListOne.isEmpty()) {
            count++;
            list.add(elementModelListOne);
        }
        if (!elementModelListTwo.isEmpty()) {
            count++;
            list.add(elementModelListTwo);
        }
        if (!elementModelListThree.isEmpty()) {
            count++;
            list.add(elementModelListThree);
        }
        if (count == 3) {
            return buildTeamGroupList(min * 3, max * 3, elementModelListOne, elementModelListTwo, elementModelListThree);
        } else if (count == 2) {
            return buildTeamGroupList(min * 2, max * 2, list.get(0), list.get(1));
        }
        List<TeamGroupElementModel> sortElementList = list.get(0)
                .stream().sorted(Comparator.comparingInt(TeamGroupElementModel::getDamage).reversed())
                .collect(Collectors.toList());
        List<TeamGroupListModel> teamGroupListModels = new ArrayList<>();
        for (TeamGroupElementModel teamGroupElementModel : sortElementList) {
            teamGroupListModels.add(new TeamGroupListModel(teamGroupElementModel, teamGroupElementModel.getScreencharacter(), null, 0, null, 0));
        }
        return teamGroupListModels;
    }

    private boolean fit(TeamGroupElementModel teamGroupElementModel,
                        boolean extra, List<Integer> usingList,
                        int stage, int boss, int auto,
                        int characteroneid, int charactertwoid, int characterthreeid, int characterfourid, int characterfiveid,
                        int borrowid) {
        List<Integer> idlist = teamGroupElementModel.getIdlist();
        int screencharacter = teamGroupElementModel.getScreencharacter();
        if (borrowid != 0) {
            if (screencharacter != 0) {
                if (screencharacter != borrowid) {
                    return false;
                }
            } else {
                screencharacter = borrowid;
            }
        }
        TeamModel teamModel = teamGroupElementModel.getTeamModel();
        if (!StageManager.getInstance().matchTeamModel(teamModel, stage)) {
            return false;
        }
        if (Integer.valueOf(teamModel.getBoss().substring(1, 2)) != boss + 1) {
            return false;
        }
        if (auto != 0) {
            if (auto == 1 && !teamModel.isAuto()) {
                return false;
            }
            if (auto == 2 && (teamModel.isAuto() || teamModel.isFinish())) {
                return false;
            }
            if (auto == 3 && !teamModel.isFinish()) {
                return false;
            }
        }
        if (characteroneid != 0 && !idlist.contains(characteroneid)) {
            return false;
        }
        if (charactertwoid != 0 && !idlist.contains(charactertwoid)) {
            return false;
        }
        if (characterthreeid != 0 && !idlist.contains(characterthreeid)) {
            return false;
        }
        if (characterfourid != 0 && !idlist.contains(characterfourid)) {
            return false;
        }
        if (characterfiveid != 0 && !idlist.contains(characterfiveid)) {
            return false;
        }
        if (!extra) {
            for (int id : idlist) {
                for (int usingId : usingList) {
                    if (id == usingId) {
                        //不需要考虑全局需要借的角色和已使用的角色重复的情况，因为全局需要借的角色说明不拥有
                        if (screencharacter != 0) {
                            return false;
                        } else {
                            screencharacter = id;
                        }
                    }
                }
            }
        }
        teamGroupElementModel.setScreencharacter(screencharacter);
        return true;
    }

    /**
     * 构建分刀数据
     */
    private List<TeamGroupListModel> buildTeamGroupList(
            int min, int max,
            List<TeamGroupElementModel> elementModelListOne,
            List<TeamGroupElementModel> elementModelListTwo) {
        Utils.logD(TAG, "buildTeamGroupList min:" + min + " max:" + max + " elementModelListOne:" + elementModelListOne.size() + " elementModelListTwo:" + elementModelListTwo.size());
        int generate = 0;
        List<TeamGroupListModel>[] sortlist = new ArrayList[max - min + 1];
        for (int i = 0; i < elementModelListOne.size(); i++) {
            TeamGroupElementModel one = elementModelListOne.get(i);
            for (int j = 0; j < elementModelListTwo.size(); j++) {
                TeamGroupElementModel two = elementModelListTwo.get(j);
                if (elementModelListOne.contains(two) && elementModelListTwo.contains(one) && one.getTeamModel().getId() - two.getTeamModel().getId() >= 0) {
                    continue;
                }
                TeamGroupListModel teamGroupListModel = compatibleTwo(one, two);
                if (teamGroupListModel != null) {
                    generate++;
                    List<TeamGroupListModel> list = sortlist[teamGroupListModel.getTotaldamage() - min];
                    if (list == null) {
                        list = new ArrayList<>();
                        sortlist[teamGroupListModel.getTotaldamage() - min] = list;
                    }
                    list.add(teamGroupListModel);
                    if (generate == interruptsize) {
                        break;
                    }
                }
            }
            if (generate == interruptsize) {
                break;
            }
        }
        List<TeamGroupListModel> list = new ArrayList<>();
        for (int i = sortlist.length - 1; i >= 0; i--) {
            List<TeamGroupListModel> childlist = sortlist[i];
            if (childlist != null) {
                for (int j = 0; j < childlist.size(); j++) {
                    TeamGroupListModel model = childlist.get(j);
                    list.add(model);
                }
            }
        }
        Utils.logD(TAG, "list:" + list.size());
        return list;
    }

    /**
     * 构建分刀数据
     */
    private List<TeamGroupListModel> buildTeamGroupList(
            int min, int max,
            List<TeamGroupElementModel> elementModelListOne,
            List<TeamGroupElementModel> elementModelListTwo,
            List<TeamGroupElementModel> elementModelListThree) {
        Utils.logD(TAG, "buildTeamGroupList min:" + min + " max:" + max + " elementModelListOne:" + elementModelListOne.size() + " elementModelListTwo:" + elementModelListTwo.size() + " elementModelListThree:" + elementModelListThree.size());
        int generate = 0;
        List<TeamGroupListModel>[] sortlist = new ArrayList[max - min + 1];
        for (int i = 0; i < elementModelListOne.size(); i++) {
            TeamGroupElementModel one = elementModelListOne.get(i);
            for (int j = 0; j < elementModelListTwo.size(); j++) {
                TeamGroupElementModel two = elementModelListTwo.get(j);
                if (elementModelListOne.contains(two) && elementModelListTwo.contains(one) && one.getTeamModel().getId() - two.getTeamModel().getId() >= 0) {
                    continue;
                }
                if (compatibleTwo(one, two) != null) {
                    for (int k = 0; k < elementModelListThree.size(); k++) {
                        TeamGroupElementModel three = elementModelListThree.get(k);
                        if (elementModelListOne.contains(three) && elementModelListThree.contains(one) && one.getTeamModel().getId() - three.getTeamModel().getId() >= 0) {
                            continue;
                        }
                        if (elementModelListTwo.contains(three) && elementModelListThree.contains(two) && two.getTeamModel().getId() - three.getTeamModel().getId() >= 0) {
                            continue;
                        }
                        TeamGroupListModel teamGroupListModel = compatibleThree(one, two, three);
                        if (teamGroupListModel != null) {
                            generate++;
                            List<TeamGroupListModel> list = sortlist[teamGroupListModel.getTotaldamage() - min];
                            if (list == null) {
                                list = new ArrayList<>();
                                sortlist[teamGroupListModel.getTotaldamage() - min] = list;
                            }
                            list.add(teamGroupListModel);
                            if (generate == interruptsize) {
                                break;
                            }
                        }
                    }
                    if (generate == interruptsize) {
                        break;
                    }
                }
            }
            if (generate == interruptsize) {
                break;
            }
        }
        List<TeamGroupListModel> list = new ArrayList<>();
        for (int i = sortlist.length - 1; i >= 0; i--) {
            List<TeamGroupListModel> childlist = sortlist[i];
            if (childlist != null) {
                for (int j = 0; j < childlist.size(); j++) {
                    TeamGroupListModel model = childlist.get(j);
                    list.add(model);
                }
            }
        }
        Utils.logD(TAG, "list:" + list.size());
        return list;
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

    private TeamGroupListModel compatibleTwo(TeamGroupElementModel elementone, TeamGroupElementModel elementtwo) {
        List<Integer> repeatList = getRepeatList(elementone.getIdlist(), elementtwo.getIdlist());
        if (repeatList.size() >= 3) {
            return null;
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
        if (!compatible) {
            return null;
        }
        if (screencharacterone == 0) {
            if (repeatList.size() > 0) {
                screencharacterone = repeatList.get(0);
                repeatList.remove(0);
            }
        }
        if (screencharactertwo == 0) {
            if (repeatList.size() > 0) {
                screencharactertwo = repeatList.get(0);
                repeatList.remove(0);
            }
        }
        return new TeamGroupListModel(elementone, screencharacterone, elementtwo, screencharactertwo, null, 0);
    }

    private TeamGroupListModel compatibleThree(TeamGroupElementModel elementone, TeamGroupElementModel elementtwo, TeamGroupElementModel elementthree) {
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
                            }
                        } else if (screencharacterthree != 0) {
                            if (repeatListac.size() == 1) {
                                screencharacterone = repeatListac.get(0);
                                repeatListac.remove(0);
                            } else if (repeatListab.size() == 1) {
                                screencharacterone = repeatListab.get(0);
                                repeatListab.remove(0);
                            }
                        } else {
                            if (repeatListab.size() == 1) {
                                screencharacterone = repeatListab.get(0);
                                repeatListab.remove(0);
                            } else if (repeatListac.size() == 1) {
                                screencharacterone = repeatListac.get(0);
                                repeatListac.remove(0);
                            }
                        }
                    }
                }
            }
            if (screencharactertwo == 0) {
                if (repeatListabc.size() == 1) {
                    screencharactertwo = repeatListabc.get(0);
                    repeatListabc.remove(0);
                    repeatListac.add(screencharactertwo);
                } else {
                    if (repeatListab.size() == 1) {
                        screencharactertwo = repeatListab.get(0);
                        repeatListab.remove(0);
                    } else if (repeatListbc.size() == 2 || repeatListbc.size() == 1) {
                        screencharactertwo = repeatListbc.get(0);
                        repeatListbc.remove(0);
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
                }
            }
            if (MyApplication.testing) {
                if (repeatListabc.size() > 0 || repeatListab.size() > 0 || repeatListac.size() > 0 || repeatListbc.size() > 0 || screencharacterone == 0 || screencharactertwo == 0 || screencharacterthree == 0) {
                    Utils.logE(TAG, "buildElementList");
                    throw new RuntimeException("分刀计算遗漏 elementone:" + elementone + " elementtwo:" + elementtwo + " elementthree:" + elementthree);
                }
            }
            return new TeamGroupListModel(elementone, screencharacterone, elementtwo, screencharactertwo, elementthree, screencharacterthree);
        }
        return null;
    }
}
