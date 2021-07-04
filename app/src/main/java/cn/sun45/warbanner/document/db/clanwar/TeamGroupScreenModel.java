package cn.sun45.warbanner.document.db.clanwar;

import cn.sun45.warbanner.framework.document.db.BaseDbTableModel;
import cn.sun45.warbanner.framework.document.db.annotation.DbTableConfigure;
import cn.sun45.warbanner.framework.document.db.annotation.DbTableParamConfigure;

/**
 * Created by Sun45 on 2021/7/4
 * 分刀信息筛选数据模型
 */
@DbTableConfigure(tablename = "teamgroupscreen")
public class TeamGroupScreenModel extends BaseDbTableModel {
    @DbTableParamConfigure(iskeyparm = true)
    private int userId;

    @DbTableParamConfigure
    private int teamonestage;
    @DbTableParamConfigure
    private int teamoneboss;
    @DbTableParamConfigure
    private int teamoneauto;
    @DbTableParamConfigure
    private int teamonecharacteroneid;
    @DbTableParamConfigure
    private int teamonecharactertwoid;
    @DbTableParamConfigure
    private int teamonecharacterthreeid;
    @DbTableParamConfigure
    private int teamonecharacterfourid;
    @DbTableParamConfigure
    private int teamonecharacterfiveid;

    @DbTableParamConfigure
    private int teamtwostage;
    @DbTableParamConfigure
    private int teamtwoboss;
    @DbTableParamConfigure
    private int teamtwoauto;
    @DbTableParamConfigure
    private int teamtwocharacteroneid;
    @DbTableParamConfigure
    private int teamtwocharactertwoid;
    @DbTableParamConfigure
    private int teamtwocharacterthreeid;
    @DbTableParamConfigure
    private int teamtwocharacterfourid;
    @DbTableParamConfigure
    private int teamtwocharacterfiveid;

    @DbTableParamConfigure
    private int teamthreestage;
    @DbTableParamConfigure
    private int teamthreeboss;
    @DbTableParamConfigure
    private int teamthreeauto;
    @DbTableParamConfigure
    private int teamthreecharacteroneid;
    @DbTableParamConfigure
    private int teamthreecharactertwoid;
    @DbTableParamConfigure
    private int teamthreecharacterthreeid;
    @DbTableParamConfigure
    private int teamthreecharacterfourid;
    @DbTableParamConfigure
    private int teamthreecharacterfiveid;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTeamonestage() {
        return teamonestage;
    }

    public void setTeamonestage(int teamonestage) {
        this.teamonestage = teamonestage;
    }

    public int getTeamoneboss() {
        return teamoneboss;
    }

    public void setTeamoneboss(int teamoneboss) {
        this.teamoneboss = teamoneboss;
    }

    public int getTeamoneauto() {
        return teamoneauto;
    }

    public void setTeamoneauto(int teamoneauto) {
        this.teamoneauto = teamoneauto;
    }

    public int getTeamonecharacteroneid() {
        return teamonecharacteroneid;
    }

    public void setTeamonecharacteroneid(int teamonecharacteroneid) {
        this.teamonecharacteroneid = teamonecharacteroneid;
    }

    public int getTeamonecharactertwoid() {
        return teamonecharactertwoid;
    }

    public void setTeamonecharactertwoid(int teamonecharactertwoid) {
        this.teamonecharactertwoid = teamonecharactertwoid;
    }

    public int getTeamonecharacterthreeid() {
        return teamonecharacterthreeid;
    }

    public void setTeamonecharacterthreeid(int teamonecharacterthreeid) {
        this.teamonecharacterthreeid = teamonecharacterthreeid;
    }

    public int getTeamonecharacterfourid() {
        return teamonecharacterfourid;
    }

    public void setTeamonecharacterfourid(int teamonecharacterfourid) {
        this.teamonecharacterfourid = teamonecharacterfourid;
    }

    public int getTeamonecharacterfiveid() {
        return teamonecharacterfiveid;
    }

    public void setTeamonecharacterfiveid(int teamonecharacterfiveid) {
        this.teamonecharacterfiveid = teamonecharacterfiveid;
    }

    public int getTeamtwostage() {
        return teamtwostage;
    }

    public void setTeamtwostage(int teamtwostage) {
        this.teamtwostage = teamtwostage;
    }

    public int getTeamtwoboss() {
        return teamtwoboss;
    }

    public void setTeamtwoboss(int teamtwoboss) {
        this.teamtwoboss = teamtwoboss;
    }

    public int getTeamtwoauto() {
        return teamtwoauto;
    }

    public void setTeamtwoauto(int teamtwoauto) {
        this.teamtwoauto = teamtwoauto;
    }

    public int getTeamtwocharacteroneid() {
        return teamtwocharacteroneid;
    }

    public void setTeamtwocharacteroneid(int teamtwocharacteroneid) {
        this.teamtwocharacteroneid = teamtwocharacteroneid;
    }

    public int getTeamtwocharactertwoid() {
        return teamtwocharactertwoid;
    }

    public void setTeamtwocharactertwoid(int teamtwocharactertwoid) {
        this.teamtwocharactertwoid = teamtwocharactertwoid;
    }

    public int getTeamtwocharacterthreeid() {
        return teamtwocharacterthreeid;
    }

    public void setTeamtwocharacterthreeid(int teamtwocharacterthreeid) {
        this.teamtwocharacterthreeid = teamtwocharacterthreeid;
    }

    public int getTeamtwocharacterfourid() {
        return teamtwocharacterfourid;
    }

    public void setTeamtwocharacterfourid(int teamtwocharacterfourid) {
        this.teamtwocharacterfourid = teamtwocharacterfourid;
    }

    public int getTeamtwocharacterfiveid() {
        return teamtwocharacterfiveid;
    }

    public void setTeamtwocharacterfiveid(int teamtwocharacterfiveid) {
        this.teamtwocharacterfiveid = teamtwocharacterfiveid;
    }

    public int getTeamthreestage() {
        return teamthreestage;
    }

    public void setTeamthreestage(int teamthreestage) {
        this.teamthreestage = teamthreestage;
    }

    public int getTeamthreeboss() {
        return teamthreeboss;
    }

    public void setTeamthreeboss(int teamthreeboss) {
        this.teamthreeboss = teamthreeboss;
    }

    public int getTeamthreeauto() {
        return teamthreeauto;
    }

    public void setTeamthreeauto(int teamthreeauto) {
        this.teamthreeauto = teamthreeauto;
    }

    public int getTeamthreecharacteroneid() {
        return teamthreecharacteroneid;
    }

    public void setTeamthreecharacteroneid(int teamthreecharacteroneid) {
        this.teamthreecharacteroneid = teamthreecharacteroneid;
    }

    public int getTeamthreecharactertwoid() {
        return teamthreecharactertwoid;
    }

    public void setTeamthreecharactertwoid(int teamthreecharactertwoid) {
        this.teamthreecharactertwoid = teamthreecharactertwoid;
    }

    public int getTeamthreecharacterthreeid() {
        return teamthreecharacterthreeid;
    }

    public void setTeamthreecharacterthreeid(int teamthreecharacterthreeid) {
        this.teamthreecharacterthreeid = teamthreecharacterthreeid;
    }

    public int getTeamthreecharacterfourid() {
        return teamthreecharacterfourid;
    }

    public void setTeamthreecharacterfourid(int teamthreecharacterfourid) {
        this.teamthreecharacterfourid = teamthreecharacterfourid;
    }

    public int getTeamthreecharacterfiveid() {
        return teamthreecharacterfiveid;
    }

    public void setTeamthreecharacterfiveid(int teamthreecharacterfiveid) {
        this.teamthreecharacterfiveid = teamthreecharacterfiveid;
    }

    @Override
    protected Class getProviderClass() {
        return ClanWarProvider.class;
    }
}
