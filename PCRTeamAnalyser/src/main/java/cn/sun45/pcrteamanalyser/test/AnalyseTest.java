package cn.sun45.pcrteamanalyser.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.sun45.pcrteamanalyser.analyse.AnalyseConfig;
import cn.sun45.pcrteamanalyser.analyse.AnalyseResult;
import cn.sun45.pcrteamanalyser.analyse.Analyser;
import cn.sun45.pcrteamanalyser.model.AnalyseTeam;


/**
 * Created by Sun45 on 2023年4月1日
 * <p>
 * 测试
 */
public class AnalyseTest {
    /**
     * 生成尾刀
     */

    // 测试
    public static void main(String[] args) {
        AnalyseTest test = new AnalyseTest();
//		int[] idsOne = { 1068, 1124, 1801, 1104, 1145 };
//		AnalyseTeam teamOne = new AnalyseTeam(idsOne, 1550, 0);
//		int[] idsTwo = { 1103, 1104, 1087, 1065, 1043 };
//		AnalyseTeam teamTwo = new AnalyseTeam(idsTwo, 189, 0);
//		int[] idsThree = { 1068, 1034, 1046, 1159, 1061 };
//		AnalyseTeam teamThree = new AnalyseTeam(idsThree, 1550, 1159);
//		test.testThreeTeam(teamOne, teamTwo, teamThree);

        test.generateTest();

//        test.modelTest();
    }

    public AnalyseTest() {
    }

    // 自动生成的最高id
    private int maxId = 8;

    // 数据分析模型组列表
    private List<AnalyseTeam> teamlist = new ArrayList();

    public void testThreeTeam(AnalyseTeam teamOne, AnalyseTeam teamTwo, AnalyseTeam teamThree) {
        AnalyseConfig config = new AnalyseConfig();
        config.testing = true;

        Analyser analyser = new Analyser(config);

        List<AnalyseTeam> teamlistOne = new ArrayList();
        teamlistOne.add(teamOne);
        List<AnalyseTeam> teamlistTwo = new ArrayList();
        teamlistTwo.add(teamTwo);
        List<AnalyseTeam> teamlistThree = new ArrayList();
        teamlistThree.add(teamThree);

        long startTime = System.currentTimeMillis();
        List<AnalyseResult> resultList = analyser.analyse(teamlistOne, teamlistTwo, teamlistThree);
        System.out.println("resultList size:" + resultList.size());
        System.out.println("timeuse:" + (System.currentTimeMillis() - startTime));
    }

    public void generateTest() {
        AnalyseConfig config = new AnalyseConfig();
        config.testing = true;
        config.interruptSize = 10_0000;

        Analyser analyser = new Analyser(config);

        int bitCount = 1;// 模型位数
        while (bitCount <= 5) {
            divide(new int[bitCount], 0);
            bitCount++;
        }

        System.out.println("max id:" + maxId);
        System.out.println("teamlist size:" + teamlist.size());
        long startTime = System.currentTimeMillis();
        List<AnalyseResult> resultList = analyser.analyse(teamlist, teamlist, teamlist);
        System.out.println("resultList size:" + resultList.size());
        System.out.println("timeuse:" + (System.currentTimeMillis() - startTime));
    }

    // 穷举数据分析模型组
    private void divide(int[] marks, int currentMark) {
        if (currentMark > 0 && marks[currentMark] <= marks[currentMark - 1]) {
            marks[currentMark] = marks[currentMark - 1] + 1;
        }
        if (marks[currentMark] > maxId - (marks.length - currentMark)) {
            if (currentMark > 0) {
                int begin = marks[currentMark - 1];
                for (int i = currentMark; i < marks.length; i++) {
                    marks[i] = begin;
                }
            }
            return;
        }
        if (currentMark == marks.length - 1) {
            match(marks);
        } else {
            divide(marks, currentMark + 1);
        }
        marks[currentMark] = marks[currentMark] + 1;
        divide(marks, currentMark);
    }

    // 组装添加数据分析模型组
    public void match(int[] marks) {
        int[] ids = new int[marks.length];
        int damage = 0;
        for (int i = 0; i < marks.length; i++) {
            int mark = marks[i];
            int id = mark + 1;
            ids[i] = id;
            damage += id;
        }
        teamlist.add(new AnalyseTeam(ids, damage));
    }

    public void modelTest() {
        AnalyseConfig config = new AnalyseConfig<TestTeam, TestResult>() {
            public AnalyseTeam<TestTeam> buildAnalyseTeam(TestTeam model) {
                List<Integer> idList = model.getIdList();
                int[] ids = new int[idList.size()];
                for (int i = 0; i < idList.size(); i++) {
                    ids[i] = idList.get(i);
                }
                return new AnalyseTeam<TestTeam>(ids, model.getDamage(), model.getBorrowId());
            }

            public TestResult buildResult(AnalyseResult<TestTeam> result) {
                AnalyseTeam<TestTeam> teamOne = result.getTeamOne();
                AnalyseTeam<TestTeam> teamTwo = result.getTeamTwo();
                AnalyseTeam<TestTeam> teamThree = result.getTeamThree();
                TestResult testResult = new TestResult();
                if (!teamOne.isEmpty()) {
                    testResult.setTeamA(teamOne.getModel());
                    testResult.setBorrowA(result.getBorrowIdOne());
                }
                if (!teamTwo.isEmpty()) {
                    testResult.setTeamB(teamTwo.getModel());
                    testResult.setBorrowB(result.getBorrowIdTwo());
                }
                if (!teamThree.isEmpty()) {
                    testResult.setTeamC(teamThree.getModel());
                    testResult.setBorrowC(result.getBorrowIdThree());
                }
                return testResult;
            }
        };
        config.testing = true;

        Analyser<TestTeam, TestResult> analyser = new Analyser(config);

        List<Integer> idListA = new ArrayList();
        TestTeam testTeamA = new TestTeam();
        testTeamA.setIdList(Arrays.asList(1, 2, 5));
        testTeamA.setDamage(100);
        TestTeam testTeamB = new TestTeam();
        testTeamB.setIdList(Arrays.asList(2, 3, 6));
        testTeamB.setDamage(200);
        TestTeam testTeamC = new TestTeam();
        testTeamC.setIdList(Arrays.asList(3, 4, 7));
        testTeamC.setDamage(300);
        List<TestTeam> modellist = new ArrayList();
        modellist.add(testTeamA);
        modellist.add(testTeamB);
        modellist.add(testTeamC);

        long startTime = System.currentTimeMillis();
        List<TestResult> resultList = analyser.analyseWithModel(modellist, modellist, modellist);
        System.out.println("resultList size:" + resultList.size());
        System.out.println("timeuse:" + (System.currentTimeMillis() - startTime));
        for (TestResult testResult : resultList) {
            System.out.println(testResult);
        }
    }
}
