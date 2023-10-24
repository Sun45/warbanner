package cn.sun45.warbanner.combination;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.character.CharacterHelper;
import cn.sun45.warbanner.clanwar.ClanwarHelper;
import cn.sun45.warbanner.document.database.setup.models.ScreenCharacterModel;
import cn.sun45.warbanner.document.database.setup.models.TeamCustomizeModel;
import cn.sun45.warbanner.document.database.source.models.TeamModel;
import cn.sun45.warbanner.document.preference.SetupPreference;
import cn.sun45.warbanner.document.statics.charactertype.CharacterOwnType;
import cn.sun45.warbanner.ui.views.combinationlist.CombinationGroupModel;
import cn.sun45.warbanner.ui.views.combinationlist.CombinationListModel;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2023/10/5
 * 套餐帮助类
 */
public class CombinationHelper {
    private static final String TAG = "CombinationHelper";

    //最小时间阈值
    private static final int TIMEMIN = 110;
    //最大时间阈值
    private static final int TIMEMAX = 160;

//    //生成中断阈值
//    public static final int interruptsize = 10;

    //阵容比对结果存储map
    private Map<String, Set<String>[]> compareMap;
    private long totalstarttime;

    public List<CombinationGroupModel> build(int stage, int auto, List<TeamModel> teamModels, List<TeamCustomizeModel> teamCustomizeModels) {
        Utils.logD(TAG, "build stage:" + stage + " auto:" + auto);
        totalstarttime = System.currentTimeMillis();
        compareMap = new HashMap<>();
        Map<Integer, Integer> screenCharacterMap = new TreeMap<>();
        if (new SetupPreference().isCharacterscreenenable()) {
            List<ScreenCharacterModel> screenCharacterModelList = CharacterHelper.getScreenCharacterList();
            for (ScreenCharacterModel screenCharacterModel : screenCharacterModelList) {
                screenCharacterMap.put(screenCharacterModel.getCharacterId(), screenCharacterModel.getType());
            }
        }
        List<CombinationElementModel> elementBossOne = new ArrayList<>();
        List<CombinationElementModel> elementBossTwo = new ArrayList<>();
        List<CombinationElementModel> elementBossThree = new ArrayList<>();
        List<CombinationElementModel> elementBossFour = new ArrayList<>();
        List<CombinationElementModel> elementBossFive = new ArrayList<>();
        for (TeamModel teamModel : teamModels) {
            if (teamModel.isFinish()) {
                continue;
            }
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
            switch (teamModel.getBoss().substring(1)) {
                case "1":
                    elementAdd(elementBossOne, elementModel);
                    break;
                case "2":
                    elementAdd(elementBossTwo, elementModel);
                    break;
                case "3":
                    elementAdd(elementBossThree, elementModel);
                    break;
                case "4":
                    elementAdd(elementBossFour, elementModel);
                    break;
                case "5":
                    elementAdd(elementBossFive, elementModel);
                    break;
                default:
                    break;
            }
        }
        Utils.logD(TAG, "elementBossOne:" + elementBossOne.size() + " elementBossTwo:" + elementBossTwo.size()
                + " elementBossThree:" + elementBossThree.size() + " elementBossFour:" + elementBossFour.size() + " elementBossFive:" + elementBossFive.size());
        List<CombinationGroupModel> groupModels = new ArrayList<>();
        final CountDownLatch latch = new CountDownLatch(7);
        //1+4 2+3 5+5
        //1+4 2+5 3+3
        //1+5 2+3 4+4
        //1+5 2+4 3+3
        goAnalyse(0, "1+4 2+3 5+5", R.drawable.ic_combination_group_pic_1, elementBossOne, elementBossFour, elementBossTwo, elementBossThree, elementBossFive, elementBossFive, groupModels, latch);
        goAnalyse(1, "1+4 2+5 3+3", R.drawable.ic_combination_group_pic_2, elementBossOne, elementBossFour, elementBossTwo, elementBossFive, elementBossThree, elementBossThree, groupModels, latch);
        goAnalyse(2, "1+5 2+3 4+4", R.drawable.ic_combination_group_pic_3, elementBossOne, elementBossFive, elementBossTwo, elementBossThree, elementBossFour, elementBossFour, groupModels, latch);
        goAnalyse(3, "1+5 2+4 3+3", R.drawable.ic_combination_group_pic_4, elementBossOne, elementBossFive, elementBossTwo, elementBossFour, elementBossThree, elementBossThree, groupModels, latch);
        //2+4 3+5 1+?
        //2+5 3+4 1+?
        //2+3 4+5 1+?
        goAnalyse(4, "2+4 3+5 1+?", R.drawable.ic_combination_group_pic_5, elementBossTwo, elementBossFour, elementBossThree, elementBossFive, elementBossOne, null, groupModels, latch);
        goAnalyse(5, "2+5 3+4 1+?", R.drawable.ic_combination_group_pic_6, elementBossTwo, elementBossFive, elementBossThree, elementBossFour, elementBossOne, null, groupModels, latch);
        goAnalyse(6, "2+3 4+5 1+?", R.drawable.ic_combination_group_pic_7, elementBossTwo, elementBossThree, elementBossFour, elementBossFive, elementBossOne, null, groupModels, latch);
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<CombinationGroupModel> sortGroupModels = new ArrayList<>();
        for (CombinationGroupModel combinationGroupModel : groupModels) {
            int order = combinationGroupModel.getOrder();
            int position = -1;
            for (int i = 0; i < sortGroupModels.size(); i++) {
                if (order < sortGroupModels.get(i).getOrder()) {
                    position = i;
                    break;
                }
            }
            if (position == -1) {
                sortGroupModels.add(combinationGroupModel);
            } else {
                sortGroupModels.add(position, combinationGroupModel);
            }
        }
        Utils.logD(TAG, "totaltime:" + (System.currentTimeMillis() - totalstarttime));
        return sortGroupModels;
    }

    //列表元素添加
    private void elementAdd(List<CombinationElementModel> list, CombinationElementModel model) {
        int position = -1;
        for (int i = 0; i < list.size(); i++) {
            if (model.getReturnTime() > list.get(i).getReturnTime()) {
                position = i;
                break;
            }
        }
        if (position == -1) {
            list.add(model);
        } else {
            list.add(position, model);
        }
    }

    private void goAnalyse(int order, String description, int picSrc,
                           List<CombinationElementModel> elementModelsia, List<CombinationElementModel> elementModelsib
            , List<CombinationElementModel> elementModelsja, List<CombinationElementModel> elementModelsjb
            , List<CombinationElementModel> elementModelska, List<CombinationElementModel> elementModelskb
            , List<CombinationGroupModel> groupModels, CountDownLatch latch) {
        new Thread() {
            @Override
            public void run() {
                long starttime = System.currentTimeMillis();
                List<CombinationListModel> list = analyse(elementModelsia, elementModelsib, elementModelsja, elementModelsjb, elementModelska, elementModelskb);
                CombinationGroupModel combinationGroupModel;
                if (list != null && !list.isEmpty()) {
                    combinationGroupModel = new CombinationGroupModel(order, description, picSrc, list);
                    groupModels.add(combinationGroupModel);
                    Utils.logD(TAG, description + " size:" + list.size() + " " + "time:" + (System.currentTimeMillis() - starttime));
                } else {
                    Utils.logD(TAG, description + " empty " + "time:" + (System.currentTimeMillis() - starttime));
                }
                latch.countDown();
                super.run();
            }
        }.start();
    }

    private List<CombinationListModel> analyse(
            List<CombinationElementModel> elementModelsia, List<CombinationElementModel> elementModelsib
            , List<CombinationElementModel> elementModelsja, List<CombinationElementModel> elementModelsjb
            , List<CombinationElementModel> elementModelska, List<CombinationElementModel> elementModelskb) {
        List<CombinationListModel> list = new ArrayList<>();
        for (int i = 5; i >= 2; i--) {
            CombinationListModel combinationListModel = null;
            for (int j = 5; j >= 2; j--) {
                combinationListModel = analyse(elementModelsia, elementModelsib, elementModelsja, elementModelsjb, elementModelska, elementModelskb, i, j);
                if (combinationListModel != null) {
                    break;
                }
            }
            if (combinationListModel != null) {
                list.add(combinationListModel);
            }
        }
        return list;
    }

    private CombinationListModel analyse(
            List<CombinationElementModel> elementModelsia, List<CombinationElementModel> elementModelsib
            , List<CombinationElementModel> elementModelsja, List<CombinationElementModel> elementModelsjb
            , List<CombinationElementModel> elementModelska, List<CombinationElementModel> elementModelskb
            , int teamRepeatSizeOne, int teamRepeatSizeTwo) {
        for (int ia = 0; ia < elementModelsia.size(); ia++) {
            CombinationElementModel elementia = elementModelsia.get(ia);
            for (int ib = 0; ib < elementModelsib.size(); ib++) {
                CombinationElementModel elementib = elementModelsib.get(ib);
                if (!compareTeam(elementia, elementib, teamRepeatSizeOne)) {
                    continue;
                }
                for (int ja = 0; ja < elementModelsja.size(); ja++) {
                    CombinationElementModel elementja = elementModelsja.get(ja);
                    if (!compareMix(elementia, elementja, compareMap) || !compareMix(elementib, elementja, compareMap)) {
                        continue;
                    }
                    for (int jb = 0; jb < elementModelsjb.size(); jb++) {
                        CombinationElementModel elementjb = elementModelsjb.get(jb);
                        if (!compareTeam(elementja, elementjb, teamRepeatSizeTwo)) {
                            continue;
                        }
                        if (!compareMix(elementia, elementjb, compareMap) || !compareMix(elementib, elementjb, compareMap)) {
                            continue;
                        }
                        for (int ka = 0; ka < elementModelska.size(); ka++) {
                            CombinationElementModel elementka = elementModelska.get(ka);
                            if (!compareMix(elementia, elementka, compareMap) || !compareMix(elementib, elementka, compareMap)
                                    || !compareMix(elementja, elementka, compareMap) || !compareMix(elementjb, elementka, compareMap)) {
                                continue;
                            }
                            if (elementModelskb != null) {
                                for (int kb = ka; kb < elementModelskb.size(); kb++) {
                                    CombinationElementModel elementkb = elementModelskb.get(kb);
                                    if (!compareTeam(elementka, elementkb, -1)) {
                                        continue;
                                    }
                                    if (!compareMix(elementia, elementkb, compareMap) || !compareMix(elementib, elementkb, compareMap)
                                            || !compareMix(elementja, elementkb, compareMap) || !compareMix(elementjb, elementkb, compareMap)) {
                                        continue;
                                    }
                                    int[] borrowIds = new int[6];
                                    if (compare(elementia, elementib, elementja, elementjb, elementka, elementkb, borrowIds)) {
                                        CombinationListModel combinationListModel = new CombinationListModel(
                                                elementia, elementib, elementja, elementjb, elementka, elementkb, borrowIds);
                                        return combinationListModel;
                                    }
                                }
                            } else {
                                int[] borrowIds = new int[6];
                                if (compare(elementia, elementib, elementja, elementjb, elementka, null, borrowIds)) {
                                    CombinationListModel combinationListModel = new CombinationListModel(
                                            elementia, elementib, elementja, elementjb, elementka, null, borrowIds);
                                    return combinationListModel;
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    //同一刀的元素配对
    public boolean compareTeam(CombinationElementModel elementOne, CombinationElementModel elementTwo, int repeatSize) {
        if (elementOne.getReturnTime() + elementTwo.getReturnTime() < TIMEMIN || elementOne.getReturnTime() + elementTwo.getReturnTime() > TIMEMAX) {
            return false;
        }
        if (repeatSize != -1 && getRepeatIds(elementOne.getIdlist(), elementTwo.getIdlist()).size() != repeatSize) {
            return false;
        } else if (getRepeatIds(elementOne.getIdlist(), elementTwo.getIdlist()).size() < 2) {
            return false;
        }
        return true;
    }

    //不同刀的元素配对
    public boolean compareMix(CombinationElementModel elementOne, CombinationElementModel elementTwo, Map<String, Set<String>[]> compareMap) {
        String snOne = elementOne.getTeamModel().getSn();
        String snTwo = elementTwo.getTeamModel().getSn();
        if (snOne.equals(snTwo)) {
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

    public boolean compare(CombinationElementModel elementOne, CombinationElementModel elementTwo
            , CombinationElementModel elementThree, CombinationElementModel elementFour
            , CombinationElementModel elementFive, CombinationElementModel elementSix
            , int[] borrowIds) {
        Set<Integer> idlista = new HashSet<>();
        idlista.addAll(elementOne.getIdlist());
        idlista.addAll(elementTwo.getIdlist());
        Set<Integer> idlistb = new HashSet<>();
        idlistb.addAll(elementThree.getIdlist());
        idlistb.addAll(elementFour.getIdlist());
        Set<Integer> idlistc = new HashSet<>();
        idlistc.addAll(elementFive.getIdlist());
        if (elementSix != null) {
            idlistc.addAll(elementSix.getIdlist());
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
        int borrowIdSix = 0;
        if (elementSix != null) {
            borrowIdSix = elementSix.getScreencharacter();
        }
        List<Integer> repeatIdListSix = new ArrayList<>();
        if (elementSix != null) {
            repeatIdListSix = getRepeatIds(elementSix.getIdlist(), idlista, idlistb);
        }
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
                                    if (elementSix != null && elementSix.getScreencharacter() == 0) {
                                        continue;
                                    } else {
                                        borrowIndexSix = repeatIdListSix.size();
                                    }
                                } else {
                                    borrowIdSix = repeatIdListSix.get(borrowIndexSix);
                                }
                                Set<Integer> seta = new HashSet<>();
                                for (Integer id : repeatIdListOne) {
                                    if (id != -1 && id != borrowIdOne) {
                                        seta.add(id);
                                    }
                                }
                                for (Integer id : repeatIdListTwo) {
                                    if (id != -1 && id != borrowIdTwo) {
                                        seta.add(id);
                                    }
                                }
                                boolean find = false;
                                Set<Integer> setb = new HashSet<>();
                                for (Integer id : repeatIdListThree) {
                                    if (id != -1 && id != borrowIdThree) {
                                        if (seta.contains(id)) {
                                            find = true;
                                            break;
                                        }
                                        setb.add(id);
                                    }
                                }
                                if (find) {
                                    continue;
                                }
                                for (Integer id : repeatIdListFour) {
                                    if (id != -1 && id != borrowIdFour) {
                                        if (seta.contains(id)) {
                                            find = true;
                                            break;
                                        }
                                        setb.add(id);
                                    }
                                }
                                if (find) {
                                    continue;
                                }
                                for (Integer id : repeatIdListFive) {
                                    if (id != -1 && id != borrowIdFive) {
                                        if (seta.contains(id) || setb.contains(id)) {
                                            find = true;
                                            break;
                                        }
                                    }
                                }
                                if (find) {
                                    continue;
                                }
                                for (Integer id : repeatIdListSix) {
                                    if (id != -1 && id != borrowIdSix) {
                                        if (seta.contains(id) || setb.contains(id)) {
                                            find = true;
                                            break;
                                        }
                                    }
                                }
                                if (find) {
                                    continue;
                                }
                                borrowIds[0] = borrowIdOne;
                                borrowIds[1] = borrowIdTwo;
                                borrowIds[2] = borrowIdThree;
                                borrowIds[3] = borrowIdFour;
                                borrowIds[4] = borrowIdFive;
                                borrowIds[5] = borrowIdSix;
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
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
}
