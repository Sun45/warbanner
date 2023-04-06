package cn.sun45.pcrteamanalyser.test;

public class TestResult {
    private TestTeam teamA;
    private TestTeam teamB;
    private TestTeam teamC;
    private int borrowA;
    private int borrowB;
    private int borrowC;

    public TestTeam getTeamA() {
        return teamA;
    }

    public void setTeamA(TestTeam teamA) {
        this.teamA = teamA;
    }

    public TestTeam getTeamB() {
        return teamB;
    }

    public void setTeamB(TestTeam teamB) {
        this.teamB = teamB;
    }

    public TestTeam getTeamC() {
        return teamC;
    }

    public void setTeamC(TestTeam teamC) {
        this.teamC = teamC;
    }

    public int getBorrowA() {
        return borrowA;
    }

    public void setBorrowA(int borrowA) {
        this.borrowA = borrowA;
    }

    public int getBorrowB() {
        return borrowB;
    }

    public void setBorrowB(int borrowB) {
        this.borrowB = borrowB;
    }

    public int getBorrowC() {
        return borrowC;
    }

    public void setBorrowC(int borrowC) {
        this.borrowC = borrowC;
    }

    @Override
    public String toString() {
        return "TestResult [teamA=" + teamA + ", teamB=" + teamB + ", teamC=" + teamC + ", borrowA=" + borrowA
                + ", borrowB=" + borrowB + ", borrowC=" + borrowC + "]";
    }

}
