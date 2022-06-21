package cn.sun45.warbanner.teamgroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cn.sun45.warbanner.clanwar.ClanwarHelper;
import cn.sun45.warbanner.document.database.setup.models.ScreenCharacterModel;
import cn.sun45.warbanner.document.database.setup.models.TeamCustomizeModel;
import cn.sun45.warbanner.document.database.setup.models.TeamGroupScreenModel;
import cn.sun45.warbanner.document.database.source.models.TeamModel;
import cn.sun45.warbanner.document.statics.ScreenCharacter;
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

    public List<TeamGroupListModel> build(List<ScreenCharacterModel> screenCharacterModelList, List<TeamModel> teamModels, List<TeamCustomizeModel> teamCustomizeModels) {
        TeamGroupScreenModel teamGroupScreenModel = ClanwarHelper.getScreenModel();
        return buildElementList(screenCharacterModelList, teamModels, teamCustomizeModels, teamGroupScreenModel);
    }

    /**
     * 构建分刀元素列表
     */
    private List<TeamGroupListModel> buildElementList(List<ScreenCharacterModel> screenCharacterModelList, List<TeamModel> teamModels, List<TeamCustomizeModel> teamCustomizeModels, TeamGroupScreenModel teamGroupScreenModel) {
        Utils.logD(TAG, "buildElementList");
        Map<Integer, Integer> screenCharacterMap = new TreeMap<>();
        for (ScreenCharacterModel screenCharacterModel : screenCharacterModelList) {
            screenCharacterMap.put(screenCharacterModel.getCharacterId(), screenCharacterModel.getType());
        }
        List<TeamGroupElementModel> elementModels = new ArrayList<>();
        for (TeamModel teamModel : teamModels) {
            TeamCustomizeModel teamCustomizeModel = ClanwarHelper.findCustomizeModel(teamModel, teamCustomizeModels);
            if (teamCustomizeModel != null && teamCustomizeModel.isBlock()) {
                continue;
            }
            List<Integer> idlist = Arrays.asList(teamModel.getCharacterone(), teamModel.getCharactertwo(), teamModel.getCharacterthree(), teamModel.getCharacterfour(), teamModel.getCharacterfive());
            int screencharacter = 0;
            if (characterscreenenable) {
                boolean notuseable = false;
                for (int i = 0; i < idlist.size(); i++) {
                    int id = idlist.get(i);
                    if (screenCharacterMap.containsKey(id)) {
                        if (screenCharacterMap.get(id) == ScreenCharacter.TYPE_LACK.getType()) {
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
        for (TeamGroupElementModel teamGroupElementModel : elementModels) {
            if (fit(teamGroupElementModel, teamGroupScreenModel.getTeamonestage(), teamGroupScreenModel.getTeamoneboss(), teamGroupScreenModel.getTeamoneauto(), teamGroupScreenModel.getTeamonecharacteroneid(), teamGroupScreenModel.getTeamonecharactertwoid(), teamGroupScreenModel.getTeamonecharacterthreeid(), teamGroupScreenModel.getTeamonecharacterfourid(), teamGroupScreenModel.getTeamonecharacterfiveid())) {
                elementModelListOne.add(teamGroupElementModel);
            }
        }
        List<TeamGroupElementModel> elementModelListTwo = new ArrayList<>();
        for (TeamGroupElementModel teamGroupElementModel : elementModels) {
            if (fit(teamGroupElementModel, teamGroupScreenModel.getTeamtwostage(), teamGroupScreenModel.getTeamtwoboss(), teamGroupScreenModel.getTeamtwoauto(), teamGroupScreenModel.getTeamtwocharacteroneid(), teamGroupScreenModel.getTeamtwocharactertwoid(), teamGroupScreenModel.getTeamtwocharacterthreeid(), teamGroupScreenModel.getTeamtwocharacterfourid(), teamGroupScreenModel.getTeamtwocharacterfiveid())) {
                elementModelListTwo.add(teamGroupElementModel);
            }
        }
        List<TeamGroupElementModel> elementModelListThree = new ArrayList<>();
        for (TeamGroupElementModel teamGroupElementModel : elementModels) {
            if (fit(teamGroupElementModel, teamGroupScreenModel.getTeamthreestage(), teamGroupScreenModel.getTeamthreeboss(), teamGroupScreenModel.getTeamthreeauto(), teamGroupScreenModel.getTeamthreecharacteroneid(), teamGroupScreenModel.getTeamthreecharactertwoid(), teamGroupScreenModel.getTeamthreecharacterthreeid(), teamGroupScreenModel.getTeamthreecharacterfourid(), teamGroupScreenModel.getTeamthreecharacterfiveid())) {
                elementModelListThree.add(teamGroupElementModel);
            }
        }
        if (elementModelListOne.isEmpty() || elementModelListTwo.isEmpty() || elementModelListThree.isEmpty()) {
            return null;
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
        return buildTeamGroupList(min * 3, max * 3, elementModelListOne, elementModelListTwo, elementModelListThree);
    }

    private boolean fit(TeamGroupElementModel teamGroupElementModel, int stage, int boss, int auto, int characteroneid, int charactertwoid, int characterthreeid, int characterfourid, int characterfiveid) {
        List<Integer> idlist = teamGroupElementModel.getIdlist();
        TeamModel teamModel = teamGroupElementModel.getTeamModel();
        if (!StageManager.getInstance().matchTeamModel(teamModel, stage)) {
            return false;
        }
        if (Integer.valueOf(teamModel.getBoss().substring(1, 2)) != boss + 1) {
            return false;
        }
        if (auto != 0 && (teamModel.isAuto() ? 1 : 2) != auto) {
            return false;
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
        return true;
    }

    /**
     * 构建分刀数据
     */
    private List<TeamGroupListModel> buildTeamGroupList(int min, int max, List<TeamGroupElementModel> elementModelListOne, List<TeamGroupElementModel> elementModelListTwo, List<TeamGroupElementModel> elementModelListThree) {
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
                if (compatible(one, two)) {
                    for (int k = 0; k < elementModelListThree.size(); k++) {
                        TeamGroupElementModel three = elementModelListThree.get(k);
                        if (elementModelListOne.contains(three) && elementModelListThree.contains(one) && one.getTeamModel().getId() - three.getTeamModel().getId() >= 0) {
                            continue;
                        }
                        if (elementModelListTwo.contains(three) && elementModelListThree.contains(two) && two.getTeamModel().getId() - three.getTeamModel().getId() >= 0) {
                            continue;
                        }
                        TeamGroupListModel teamGroupListModel = compatible(one, two, three);
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

    private boolean compatible(TeamGroupElementModel elementone, TeamGroupElementModel elementtwo) {
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

    private TeamGroupListModel compatible(TeamGroupElementModel elementone, TeamGroupElementModel elementtwo, TeamGroupElementModel elementthree) {
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
                    repeatListac.add(screencharactertwo);
                } else {
                    if (repeatListab.size() == 1) {
                        screencharactertwo = repeatListab.get(0);
                        repeatListab.remove(0);
                    } else if (repeatListbc.size() == 2 || repeatListbc.size() == 1) {
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
                    Utils.logE(TAG, "buildElementList");
                    throw new RuntimeException("分刀计算遗漏 elementone:" + elementone + " elementtwo:" + elementtwo + " elementthree:" + elementthree);
                }
            }
            return new TeamGroupListModel(elementone, screencharacterone, elementtwo, screencharactertwo, elementthree, screencharacterthree);
        }
        return null;
    }
}
