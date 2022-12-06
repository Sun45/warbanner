package cn.sun45.warbanner.document.database.setup.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by Sun45 on 2022/6/9
 * 分刀信息筛选数据模型
 */
@Entity(tableName = "teamgroupscreen")
public class TeamGroupScreenModel {
    @PrimaryKey
    @ColumnInfo
    private int userId;

    @ColumnInfo
    private int teamonestage;
    @ColumnInfo
    private int teamoneboss;
    @ColumnInfo
    private int teamoneauto;
    @ColumnInfo
    private int teamonecharacteroneid;
    @ColumnInfo
    private int teamonecharactertwoid;
    @ColumnInfo
    private int teamonecharacterthreeid;
    @ColumnInfo
    private int teamonecharacterfourid;
    @ColumnInfo
    private int teamonecharacterfiveid;
    @ColumnInfo
    private int teamoneborrowindex;
    @ColumnInfo
    private boolean teamoneextra;
    @ColumnInfo
    private boolean teamoneenable;

    @ColumnInfo
    private int teamtwostage;
    @ColumnInfo
    private int teamtwoboss;
    @ColumnInfo
    private int teamtwoauto;
    @ColumnInfo
    private int teamtwocharacteroneid;
    @ColumnInfo
    private int teamtwocharactertwoid;
    @ColumnInfo
    private int teamtwocharacterthreeid;
    @ColumnInfo
    private int teamtwocharacterfourid;
    @ColumnInfo
    private int teamtwocharacterfiveid;
    @ColumnInfo
    private int teamtwoborrowindex;
    @ColumnInfo
    private boolean teamtwoextra;
    @ColumnInfo
    private boolean teamtwoenable;

    @ColumnInfo
    private int teamthreestage;
    @ColumnInfo
    private int teamthreeboss;
    @ColumnInfo
    private int teamthreeauto;
    @ColumnInfo
    private int teamthreecharacteroneid;
    @ColumnInfo
    private int teamthreecharactertwoid;
    @ColumnInfo
    private int teamthreecharacterthreeid;
    @ColumnInfo
    private int teamthreecharacterfourid;
    @ColumnInfo
    private int teamthreecharacterfiveid;
    @ColumnInfo
    private int teamthreeborrowindex;
    @ColumnInfo
    private boolean teamthreeextra;
    @ColumnInfo
    private boolean teamthreeenable;

    public TeamGroupScreenModel() {
        teamoneborrowindex = -1;
        teamoneenable = true;

        teamtwoborrowindex = -1;
        teamtwoenable = true;

        teamthreeborrowindex = -1;
        teamthreeenable = true;
    }

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

    public int getTeamoneborrowindex() {
        return teamoneborrowindex;
    }

    public void setTeamoneborrowindex(int teamoneborrowindex) {
        this.teamoneborrowindex = teamoneborrowindex;
    }

    public boolean isTeamoneextra() {
        return teamoneextra;
    }

    public void setTeamoneextra(boolean teamoneextra) {
        this.teamoneextra = teamoneextra;
    }

    public boolean isTeamoneenable() {
        return teamoneenable;
    }

    public void setTeamoneenable(boolean teamoneenable) {
        this.teamoneenable = teamoneenable;
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

    public int getTeamtwoborrowindex() {
        return teamtwoborrowindex;
    }

    public void setTeamtwoborrowindex(int teamtwoborrowindex) {
        this.teamtwoborrowindex = teamtwoborrowindex;
    }

    public boolean isTeamtwoextra() {
        return teamtwoextra;
    }

    public void setTeamtwoextra(boolean teamtwoextra) {
        this.teamtwoextra = teamtwoextra;
    }

    public boolean isTeamtwoenable() {
        return teamtwoenable;
    }

    public void setTeamtwoenable(boolean teamtwoenable) {
        this.teamtwoenable = teamtwoenable;
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

    public int getTeamthreeborrowindex() {
        return teamthreeborrowindex;
    }

    public void setTeamthreeborrowindex(int teamthreeborrowindex) {
        this.teamthreeborrowindex = teamthreeborrowindex;
    }

    public boolean isTeamthreeextra() {
        return teamthreeextra;
    }

    public void setTeamthreeextra(boolean teamthreeextra) {
        this.teamthreeextra = teamthreeextra;
    }

    public boolean isTeamthreeenable() {
        return teamthreeenable;
    }

    public void setTeamthreeenable(boolean teamthreeenable) {
        this.teamthreeenable = teamthreeenable;
    }
}
