package cn.sun45.pcrteamanalyser.analyse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.sun45.pcrteamanalyser.model.AnalyseTeam;

/**
 * Created by Sun45 on 2023年4月1日
 * <p>
 * 数据分析入口
 */
public class Analyser<M, R> {
    private AnalyseConfig<M, R> config;

    public Analyser() {
        this.config = new AnalyseConfig();
    }

    public Analyser(AnalyseConfig config) {
        this.config = config;
    }

    /**
     * 分析
     *
     * @param modellistOne   接入数据模型组列表一
     * @param modellistTwo   接入数据模型组列表二
     * @param modellistThree 接入数据模型组列表三
     * @return 输出数据模型列表
     */
    public List<R> analyseWithModel(List<M> modellistOne, List<M> modellistTwo, List<M> modellistThree) {
        List<AnalyseTeam> teamlistOne = new ArrayList();
        for (M m : modellistOne) {
            AnalyseTeam team = config.buildAnalyseTeam(m);
            team.setModel(m);
            teamlistOne.add(team);
        }
        List<AnalyseTeam> teamlistTwo = new ArrayList();
        for (M m : modellistTwo) {
            AnalyseTeam team = config.buildAnalyseTeam(m);
            team.setModel(m);
            teamlistTwo.add(team);
        }
        List<AnalyseTeam> teamlistThree = new ArrayList();
        for (M m : modellistThree) {
            AnalyseTeam team = config.buildAnalyseTeam(m);
            team.setModel(m);
            teamlistThree.add(team);
        }
        return analyse(teamlistOne, teamlistTwo, teamlistThree);
    }

    /**
     * 分析
     *
     * @param teamlistOne   数据分析模型组列表一
     * @param teamlistTwo   数据分析模型组列表二
     * @param teamlistThree 数据分析模型组列表三
     * @return 输出数据模型
     */
    public List<R> analyse(List<AnalyseTeam> teamlistOne, List<AnalyseTeam> teamlistTwo,
                           List<AnalyseTeam> teamlistThree) {
        List<R> resultList = new ArrayList();
        if (teamlistOne == null || teamlistOne.isEmpty()) {
            teamlistOne = new ArrayList();
            teamlistOne.add(AnalyseTeam.EMPTY_INSTANCE);
        }
        if (teamlistTwo == null || teamlistTwo.isEmpty()) {
            teamlistTwo = new ArrayList();
            teamlistTwo.add(AnalyseTeam.EMPTY_INSTANCE);
        }
        if (teamlistThree == null || teamlistThree.isEmpty()) {
            teamlistThree = new ArrayList();
            teamlistThree.add(AnalyseTeam.EMPTY_INSTANCE);
        }
        int min = -1;
        int max = -1;
        for (AnalyseTeam team : teamlistOne) {
            if (!team.isEmpty()) {
                if (min == -1 || min > team.getDamage()) {
                    min = team.getDamage();
                }
                if (max == -1 || max < team.getDamage()) {
                    max = team.getDamage();
                }
            }
        }
        for (AnalyseTeam team : teamlistTwo) {
            if (!team.isEmpty()) {
                if (min == -1 || min > team.getDamage()) {
                    min = team.getDamage();
                }
                if (max == -1 || max < team.getDamage()) {
                    max = team.getDamage();
                }
            }
        }
        for (AnalyseTeam team : teamlistThree) {
            if (!team.isEmpty()) {
                if (min == -1 || min > team.getDamage()) {
                    min = team.getDamage();
                }
                if (max == -1 || max < team.getDamage()) {
                    max = team.getDamage();
                }
            }
        }
        max *= 3;
        Set<AnalyseResult>[] resultArray = analyse(teamlistOne, teamlistTwo, teamlistThree, min, max);
        for (int i = resultArray.length - 1; i >= 0; i--) {
            Set<AnalyseResult> set = resultArray[i];
            if (set != null) {
                for (AnalyseResult<M> result : set) {
                    R r = config.buildResult(result);
                    resultList.add(r);
                }
            }
        }
        return resultList;
    }

    /**
     * 分析
     *
     * @param teamlistOne   数据分析模型组列表一
     * @param teamlistTwo   数据分析模型组列表二
     * @param teamlistThree 数据分析模型组列表三
     * @param min           最小总伤害
     * @param max           最大总伤害
     * @return 分析结果数组
     */
    public Set<AnalyseResult>[] analyse(List<AnalyseTeam> teamlistOne, List<AnalyseTeam> teamlistTwo,
                                        List<AnalyseTeam> teamlistThree, int min, int max) {
        Set<AnalyseResult>[] resultArray = new HashSet[max - min + 1];
        int totalCount = 0;
        for (int i = 0; i < teamlistOne.size(); i++) {
            AnalyseTeam teamOne = teamlistOne.get(i);
            for (int j = 0; j < teamlistTwo.size(); j++) {
                AnalyseTeam teamTwo = teamlistTwo.get(j);
                if (compareTwo(teamOne, teamTwo)) {
                    for (int k = 0; k < teamlistThree.size(); k++) {
                        AnalyseTeam teamThree = teamlistThree.get(k);
                        boolean resultInSet = false;
                        AnalyseResult result = new AnalyseResult(teamOne, teamTwo, teamThree);
                        if (!result.isEmpty()) {
                            Set<AnalyseResult> set = resultArray[result.getTotalDamage() - min];
                            if (set != null) {
                                resultInSet = set.contains(result);
                            }
                            if (!resultInSet) {
                                if (compareThree(teamOne, teamTwo, teamThree, result)) {
                                    if (set == null) {
                                        set = new HashSet();
                                        resultArray[result.getTotalDamage() - min] = set;
                                    }
                                    set.add(result);
                                    totalCount++;
                                    if (totalCount >= config.interruptSize) {
                                        return resultArray;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return resultArray;
    }

    /**
     * 比对
     *
     * @param teamOne   数据分析模型组一
     * @param teamTwo   数据分析模型组二
     * @param borrowIds 借人位唯一Id列表
     * @return 比对通过
     */
    public boolean compareTwo(AnalyseTeam teamOne, AnalyseTeam teamTwo, int[]... borrowIds) {
        if (teamOne.isEmpty()) {
            if (borrowIds.length == 1) {
                borrowIds[0][1] = teamTwo.getBorrowId();
            }
            return true;
        } else if (teamTwo.isEmpty()) {
            if (borrowIds.length == 1) {
                borrowIds[0][0] = teamOne.getBorrowId();
            }
            return true;
        }
        List<Integer> repeatIdList = getRepeatIds(teamOne.getIds(), teamTwo.getIds());
        if (repeatIdList.size() > 2) {
            return false;
        }
        int borrowIdOne = teamOne.getBorrowId();
        int borrowIdTwo = teamTwo.getBorrowId();
        int borrowDemand = 0;
        if (borrowIdOne != 0) {
            repeatIdList.remove((Object) borrowIdOne);
            borrowDemand++;
        }
        if (borrowIdTwo != 0) {
            repeatIdList.remove((Object) borrowIdTwo);
            borrowDemand++;
        }
        borrowDemand += repeatIdList.size();
        if (borrowDemand > 2) {
            return false;
        }
        //需要生成借人位唯一Id列表
        if (borrowIds.length == 1) {
            if (repeatIdList.size() > 0) {
                if (borrowIdOne == 0) {
                    borrowIdOne = repeatIdList.get(0);
                    repeatIdList.remove(0);
                }
                if (repeatIdList.size() > 0) {
                    if (borrowIdTwo == 0) {
                        borrowIdTwo = repeatIdList.get(0);
                        repeatIdList.remove(0);
                    }
                }
            }
            borrowIds[0][0] = borrowIdOne;
            borrowIds[0][1] = borrowIdTwo;
        }
        return true;
    }

    /**
     * 比对
     *
     * @param teamOne   数据分析模型组一
     * @param teamTwo   数据分析模型组二
     * @param teamThree 数据分析模型组三
     * @param result    数据分析结果
     * @return 比对通过
     */
    public boolean compareThree(AnalyseTeam teamOne, AnalyseTeam teamTwo, AnalyseTeam teamThree, AnalyseResult result) {
        if (teamOne.isEmpty()) {
            int[] borrowIds = new int[2];
            if (compareTwo(teamTwo, teamThree, borrowIds)) {
                result.setBorrowIdTwo(borrowIds[0]);
                result.setBorrowIdThree(borrowIds[1]);
                return true;
            }
            return false;
        } else if (teamTwo.isEmpty()) {
            int[] borrowIds = new int[2];
            if (compareTwo(teamOne, teamThree, borrowIds)) {
                result.setBorrowIdOne(borrowIds[0]);
                result.setBorrowIdThree(borrowIds[1]);
                return true;
            }
            return false;
        } else if (teamThree.isEmpty()) {
            int[] borrowIds = new int[2];
            if (compareTwo(teamOne, teamTwo, borrowIds)) {
                result.setBorrowIdOne(borrowIds[0]);
                result.setBorrowIdTwo(borrowIds[1]);
                return true;
            }
            return false;
        }

        if (!compareTwo(teamOne, teamThree)) {
            return false;
        }
        List<Integer> repeatIdListAC = getRepeatIds(teamOne.getIds(), teamThree.getIds());
        if (!compareTwo(teamTwo, teamThree)) {
            return false;
        }
        List<Integer> repeatIdListBC = getRepeatIds(teamTwo.getIds(), teamThree.getIds());
        List<Integer> repeatIdListAB = getRepeatIds(teamOne.getIds(), teamTwo.getIds());
        List<Integer> repeatIdListABC = getRepeatIds(repeatIdListAB, teamThree.getIds());
        int repeatIdABC = 0;
        if (repeatIdListABC.size() > 1) {
            return false;
        } else if (repeatIdListABC.size() == 1) {
            repeatIdABC = repeatIdListABC.get(0);
            repeatIdListAB.remove((Object) repeatIdABC);
            repeatIdListBC.remove((Object) repeatIdABC);
            repeatIdListAC.remove((Object) repeatIdABC);
        }
        int borrowIdOne = teamOne.getBorrowId();
        int borrowIdTwo = teamTwo.getBorrowId();
        int borrowIdThree = teamThree.getBorrowId();
        if (borrowIdOne != 0) {
            repeatIdListAB.remove((Object) borrowIdOne);
            repeatIdListAC.remove((Object) borrowIdOne);
            if (repeatIdABC == borrowIdOne) {
                repeatIdABC = 0;
                repeatIdListBC.add(borrowIdOne);
            }
        }
        if (borrowIdTwo != 0) {
            repeatIdListAB.remove((Object) borrowIdTwo);
            repeatIdListBC.remove((Object) borrowIdTwo);
            if (repeatIdABC == borrowIdTwo) {
                repeatIdABC = 0;
                repeatIdListAC.add(borrowIdTwo);
            }
        }
        if (borrowIdThree != 0) {
            repeatIdListAC.remove((Object) borrowIdThree);
            repeatIdListBC.remove((Object) borrowIdThree);
            if (repeatIdABC == borrowIdThree) {
                repeatIdABC = 0;
                repeatIdListAB.add(borrowIdThree);
            }
        }
        int borrowDemand = 0;
        if (borrowIdOne != 0) {
            borrowDemand++;
        }
        if (borrowIdTwo != 0) {
            borrowDemand++;
        }
        if (borrowIdThree != 0) {
            borrowDemand++;
        }
        if (repeatIdABC != 0) {
            borrowDemand += 2;
        }
        borrowDemand += repeatIdListAB.size();
        borrowDemand += repeatIdListAC.size();
        borrowDemand += repeatIdListBC.size();
        if (borrowDemand > 3) {
            return false;
        }
        if (borrowIdOne != 0 && borrowIdTwo != 0) {
            if (!repeatIdListAB.isEmpty()) {
                return false;
            }
        }
        if (borrowIdTwo != 0 && borrowIdThree != 0) {
            if (!repeatIdListBC.isEmpty()) {
                return false;
            }
        }
        if (borrowIdOne != 0 && borrowIdThree != 0) {
            if (!repeatIdListAC.isEmpty()) {
                return false;
            }
        }
        if (borrowIdOne != 0) {
            if (repeatIdListAB.size() == 2 || repeatIdListAC.size() == 2) {
                return false;
            }
            if (repeatIdListAB.size() == 1) {
                borrowIdTwo = repeatIdListAB.get(0);
                repeatIdListAB.remove(0);
            }
            if (repeatIdListAC.size() == 1) {
                borrowIdThree = repeatIdListAC.get(0);
                repeatIdListAC.remove(0);
            }
        }
        if (borrowIdTwo != 0) {
            if (repeatIdListAB.size() == 2 || repeatIdListBC.size() == 2) {
                return false;
            }
            if (repeatIdListAB.size() == 1) {
                borrowIdOne = repeatIdListAB.get(0);
                repeatIdListAB.remove(0);
            }
            if (repeatIdListBC.size() == 1) {
                borrowIdThree = repeatIdListBC.get(0);
                repeatIdListBC.remove(0);
            }
        }
        if (borrowIdThree != 0) {
            if (repeatIdListAC.size() == 2 || repeatIdListBC.size() == 2) {
                return false;
            }
            if (repeatIdListAC.size() == 1) {
                borrowIdOne = repeatIdListAC.get(0);
                repeatIdListAC.remove(0);
            }
            if (repeatIdListBC.size() == 1) {
                borrowIdTwo = repeatIdListBC.get(0);
                repeatIdListBC.remove(0);
            }
        }
        if (borrowIdOne == 0) {
            if (repeatIdABC != 0) {
                borrowIdOne = repeatIdABC;
                repeatIdListBC.add(borrowIdOne);
                repeatIdABC = 0;
            } else {
                if (repeatIdListAB.size() == 2) {
                    borrowIdOne = repeatIdListAB.get(0);
                    repeatIdListAB.remove(0);
                } else if (repeatIdListAC.size() == 2) {
                    borrowIdOne = repeatIdListAC.get(0);
                    repeatIdListAC.remove(0);
                } else if (repeatIdListAB.size() == 1) {
                    borrowIdOne = repeatIdListAB.get(0);
                    repeatIdListAB.remove(0);
                } else if (repeatIdListAC.size() == 1) {
                    borrowIdOne = repeatIdListAC.get(0);
                    repeatIdListAC.remove(0);
                }
            }
        }
        if (borrowIdTwo == 0) {
            if (repeatIdABC != 0) {
                borrowIdTwo = repeatIdABC;
                repeatIdListAC.add(borrowIdTwo);
                repeatIdABC = 0;
            } else {
                if (repeatIdListAB.size() == 1) {
                    borrowIdTwo = repeatIdListAB.get(0);
                    repeatIdListAB.remove(0);
                } else if (repeatIdListBC.size() > 0) {
                    borrowIdTwo = repeatIdListBC.get(0);
                    repeatIdListBC.remove(0);
                }
            }
        }
        if (borrowIdThree == 0) {
            if (repeatIdListAC.size() == 1) {
                borrowIdThree = repeatIdListAC.get(0);
                repeatIdListAC.remove(0);
            } else if (repeatIdListBC.size() == 1) {
                borrowIdThree = repeatIdListBC.get(0);
                repeatIdListBC.remove(0);
            }
        }
        if (config.testing) {
            if (repeatIdABC != 0 || repeatIdListAB.size() > 0 || repeatIdListBC.size() > 0
                    || repeatIdListAC.size() > 0) {
                throw new RuntimeException(
                        "分刀计算遗漏 teamOne:" + teamOne + " teamTwo:" + teamTwo + " teamThree:" + teamThree);
            }
        }
        result.setBorrowIdOne(borrowIdOne);
        result.setBorrowIdTwo(borrowIdTwo);
        result.setBorrowIdThree(borrowIdThree);
        return true;
    }

    /**
     * 获取重复Id列表
     *
     * @param idsOne Id组一
     * @param idsTwo Id组二
     * @return 重复Id列表
     */
    public List<Integer> getRepeatIds(int[] idsOne, int[] idsTwo) {
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

    /**
     * 获取重复Id列表
     *
     * @param idsOne Id组一
     * @param idsTwo Id组二
     * @return 重复Id列表
     */
    public List<Integer> getRepeatIds(List<Integer> idsOne, int[] idsTwo) {
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
}
