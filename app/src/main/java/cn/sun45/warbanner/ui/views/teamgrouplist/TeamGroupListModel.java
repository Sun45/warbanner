package cn.sun45.warbanner.ui.views.teamgrouplist;

import java.io.Serializable;
import java.util.List;

import cn.sun45.warbanner.document.db.clanwar.TeamCustomizeModel;
import cn.sun45.warbanner.document.db.clanwar.TeamModel;
import cn.sun45.warbanner.teamgroup.TeamGroupElementModel;

/**
 * Created by Sun45 on 2021/5/30
 * 分刀列表数据模型
 */
public class TeamGroupListModel implements Serializable {
    private TeamModel teamone;
    private TeamCustomizeModel teamCustomizeone;
    private List<Integer> idlistone;
    private int borrowindexone;
    private TeamModel teamtwo;
    private TeamCustomizeModel teamCustomizetwo;
    private List<Integer> idlisttwo;
    private int borrowindextwo;
    private TeamModel teamthree;
    private TeamCustomizeModel teamCustomizethree;
    private List<Integer> idlistthree;
    private int borrowindexthree;

    private int totaldamage;

    public TeamGroupListModel(TeamGroupElementModel elementone, int idone, TeamGroupElementModel elementtwo, int idtwo, TeamGroupElementModel elementthree, int idthree) {
        teamone = elementone.getTeamModel();
        teamCustomizeone = elementone.getTeamCustomizeModel();
        idlistone = elementone.getIdlist();
        borrowindexone = idlistone.indexOf((Object) idone);
        teamtwo = elementtwo.getTeamModel();
        teamCustomizetwo = elementtwo.getTeamCustomizeModel();
        idlisttwo = elementtwo.getIdlist();
        borrowindextwo = idlisttwo.indexOf((Object) idtwo);
        teamthree = elementthree.getTeamModel();
        teamCustomizethree = elementthree.getTeamCustomizeModel();
        idlistthree = elementthree.getIdlist();
        borrowindexthree = idlistthree.indexOf((Object) idthree);
        totaldamage = ((teamCustomizeone != null && teamCustomizeone.damageEffective()) ? teamCustomizeone.getEllipsisdamage() : teamone.getEllipsisdamage())
                + ((teamCustomizetwo != null && teamCustomizetwo.damageEffective()) ? teamCustomizetwo.getEllipsisdamage() : teamtwo.getEllipsisdamage())
                + ((teamCustomizethree != null && teamCustomizethree.damageEffective()) ? teamCustomizethree.getEllipsisdamage() : teamthree.getEllipsisdamage());
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
}
