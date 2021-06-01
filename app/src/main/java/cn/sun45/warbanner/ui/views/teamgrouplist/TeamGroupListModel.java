package cn.sun45.warbanner.ui.views.teamgrouplist;

import java.util.List;

import cn.sun45.warbanner.document.db.clanwar.TeamModel;

/**
 * Created by Sun45 on 2021/5/30
 * 分刀列表数据模型
 */
public class TeamGroupListModel {
    private TeamModel teamone;
    private List<Integer> idlistone;
    private int borrowindexone;
    private TeamModel teamtwo;
    private List<Integer> idlisttwo;
    private int borrowindextwo;
    private TeamModel teamthree;
    private List<Integer> idlistthree;
    private int borrowindexthree;

    private int totaldamage;

    private boolean collected;

    public TeamGroupListModel(TeamGroupListElementModel elementone, int idone, TeamGroupListElementModel elementtwo, int idtwo, TeamGroupListElementModel elementthree, int idthree, boolean collected) {
        teamone = elementone.getTeamModel();
        idlistone = elementone.getIdlist();
        borrowindexone = idlistone.indexOf((Object) idone);
        teamtwo = elementtwo.getTeamModel();
        idlisttwo = elementtwo.getIdlist();
        borrowindextwo = idlisttwo.indexOf((Object) idtwo);
        teamthree = elementthree.getTeamModel();
        idlistthree = elementthree.getIdlist();
        borrowindexthree = idlistthree.indexOf((Object) idthree);
        totaldamage = teamone.getDamagenumber() + teamtwo.getDamagenumber() + teamthree.getDamagenumber();
        this.collected = collected;
    }

    public TeamGroupListModel(TeamModel teamone, List<Integer> idlistone, int borrowindexone, TeamModel teamtwo, List<Integer> idlisttwo, int borrowindextwo, TeamModel teamthree, List<Integer> idlistthree, int borrowindexthree) {
        this.teamone = teamone;
        this.idlistone = idlistone;
        this.borrowindexone = borrowindexone;
        this.teamtwo = teamtwo;
        this.idlisttwo = idlisttwo;
        this.borrowindextwo = borrowindextwo;
        this.teamthree = teamthree;
        this.idlistthree = idlistthree;
        this.borrowindexthree = borrowindexthree;
        totaldamage = teamone.getDamagenumber() + teamtwo.getDamagenumber() + teamthree.getDamagenumber();
        collected = true;
    }

    public TeamModel getTeamone() {
        return teamone;
    }

    public List<Integer> getIdlistone() {
        return idlistone;
    }

    public int getBorrowindexone() {
        return borrowindexone;
    }

    public TeamModel getTeamtwo() {
        return teamtwo;
    }

    public List<Integer> getIdlisttwo() {
        return idlisttwo;
    }

    public int getBorrowindextwo() {
        return borrowindextwo;
    }

    public TeamModel getTeamthree() {
        return teamthree;
    }

    public List<Integer> getIdlistthree() {
        return idlistthree;
    }

    public int getBorrowindexthree() {
        return borrowindexthree;
    }

    public int getTotaldamage() {
        return totaldamage;
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }
}
