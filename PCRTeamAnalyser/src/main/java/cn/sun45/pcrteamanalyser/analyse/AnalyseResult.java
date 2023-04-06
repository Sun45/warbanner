package cn.sun45.pcrteamanalyser.analyse;

import java.util.Objects;

import cn.sun45.pcrteamanalyser.model.AnalyseTeam;

/**
 * Created by Sun45 on 2023年4月1日
 * <p>
 * 数据分析结果(分刀结果)
 */
public class AnalyseResult<M> {
    // 数据分析模型
    // 数据分析模型组一
    private AnalyseTeam<M> teamOne;
    // 数据分析模型组二
    private AnalyseTeam<M> teamTwo;
    // 数据分析模型组三
    private AnalyseTeam<M> teamThree;

    // 唯一识别码
    private String idCode;

    // 借人位
    // 模型组一借人位唯一Id
    private int borrowIdOne;
    // 模型组二借人位唯一Id
    private int borrowIdTwo;
    // 模型组三借人位唯一Id
    private int borrowIdThree;

    public AnalyseResult(AnalyseTeam<M> teamOne, AnalyseTeam<M> teamTwo, AnalyseTeam<M> teamThree) {
        this.teamOne = teamOne;
        this.teamTwo = teamTwo;
        this.teamThree = teamThree;
        buildIdCode();
    }

    public AnalyseTeam<M> getTeamOne() {
        return teamOne;
    }

    public AnalyseTeam<M> getTeamTwo() {
        return teamTwo;
    }

    public AnalyseTeam<M> getTeamThree() {
        return teamThree;
    }

    public int getBorrowIdOne() {
        return borrowIdOne;
    }

    public void setBorrowIdOne(int borrowIdOne) {
        this.borrowIdOne = borrowIdOne;
    }

    public int getBorrowIdTwo() {
        return borrowIdTwo;
    }

    public void setBorrowIdTwo(int borrowIdTwo) {
        this.borrowIdTwo = borrowIdTwo;
    }

    public int getBorrowIdThree() {
        return borrowIdThree;
    }

    public void setBorrowIdThree(int borrowIdThree) {
        this.borrowIdThree = borrowIdThree;
    }

    /**
     * 获取总伤害
     *
     * @return 总伤害
     */
    public int getTotalDamage() {
        int totalDamage = 0;
        if (!teamOne.isEmpty()) {
            totalDamage += teamOne.getDamage();
        }
        if (!teamTwo.isEmpty()) {
            totalDamage += teamTwo.getDamage();
        }
        if (!teamThree.isEmpty()) {
            totalDamage += teamThree.getDamage();
        }
        return totalDamage;
    }

    /**
     * 构建唯一识别码
     */
    private void buildIdCode() {
        String idCodeOne = teamOne.getIdCode();
        String idCodeTwo = teamTwo.getIdCode();
        String idCodeThree = teamThree.getIdCode();
        int orderOne = 0;
        int orderTwo = 0;
        if (idCodeOne.compareTo(idCodeTwo) > 0) {
            orderOne++;
            orderTwo--;
        } else {
            orderOne--;
            orderTwo++;
        }
        if (idCodeOne.compareTo(idCodeThree) > 0) {
            orderOne++;
        } else {
            orderOne--;
        }
        if (idCodeTwo.compareTo(idCodeThree) > 0) {
            orderTwo++;
        } else {
            orderTwo--;
        }
        StringBuilder sb = new StringBuilder("[");
        if (orderOne == -2) {
            sb.append(idCodeOne);
        } else if (orderTwo == -2) {
            sb.append(idCodeTwo);
        } else {
            sb.append(idCodeThree);
        }
        sb.append("][");
        if (orderOne == 0) {
            sb.append(idCodeOne);
        } else if (orderTwo == 0) {
            sb.append(idCodeTwo);
        } else {
            sb.append(idCodeThree);
        }
        sb.append("][");
        if (orderOne == 2) {
            sb.append(idCodeOne);
        } else if (orderTwo == 2) {
            sb.append(idCodeTwo);
        } else {
            sb.append(idCodeThree);
        }
        sb.append("]");
        idCode = sb.toString();
    }

    public String getIdCode() {
        return idCode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCode);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AnalyseResult other = (AnalyseResult) obj;
        return idCode.equals(other.getIdCode());
    }

    /**
     * 是空结果
     *
     * @return 空结果
     */
    public boolean isEmpty() {
        return teamOne.isEmpty() && teamTwo.isEmpty() && teamThree.isEmpty();
    }

    @Override
    public String toString() {
        return new StringBuilder().append("AnalyseResult [idCode=").append(idCode).append("]").toString();
    }

}
