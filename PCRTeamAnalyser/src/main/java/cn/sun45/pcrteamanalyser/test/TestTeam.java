package cn.sun45.pcrteamanalyser.test;

import java.util.List;

public class TestTeam {
    private List<Integer> idList;

    private int damage;

    private int borrowId;

    public List<Integer> getIdList() {
        return idList;
    }

    public void setIdList(List<Integer> idList) {
        this.idList = idList;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(int borrowId) {
        this.borrowId = borrowId;
    }

    @Override
    public String toString() {
        return "TestTeam [idList=" + idList + ", damage=" + damage + ", borrowId=" + borrowId + "]";
    }


}
