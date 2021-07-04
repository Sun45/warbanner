package cn.sun45.warbanner.document.db.source;

import cn.sun45.warbanner.framework.document.db.BaseDbTableModel;
import cn.sun45.warbanner.framework.document.db.annotation.DbTableConfigure;
import cn.sun45.warbanner.framework.document.db.annotation.DbTableParamConfigure;

/**
 * Created by Sun45 on 2021/6/14
 * 会战信息数据模型
 */
@DbTableConfigure(tablename = "clanwar")
public class ClanWarModel extends BaseDbTableModel {
    //唯一值
    @DbTableParamConfigure(iskeyparm = true)
    private int id;

    @DbTableParamConfigure
    private String startdate;

    @DbTableParamConfigure
    private String enddate;

    @DbTableParamConfigure
    private String bossonename;
    @DbTableParamConfigure
    private String bossoneiconurl;

    @DbTableParamConfigure
    private String bosstwoname;
    @DbTableParamConfigure
    private String bosstwoiconurl;

    @DbTableParamConfigure
    private String bossthreename;
    @DbTableParamConfigure
    private String bossthreeiconurl;

    @DbTableParamConfigure
    private String bossfourname;
    @DbTableParamConfigure
    private String bossfouriconurl;

    @DbTableParamConfigure
    private String bossfivename;
    @DbTableParamConfigure
    private String bossfiveiconurl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getBossonename() {
        return bossonename;
    }

    public void setBossonename(String bossonename) {
        this.bossonename = bossonename;
    }

    public String getBossoneiconurl() {
        return bossoneiconurl;
    }

    public void setBossoneiconurl(String bossoneiconurl) {
        this.bossoneiconurl = bossoneiconurl;
    }

    public String getBosstwoname() {
        return bosstwoname;
    }

    public void setBosstwoname(String bosstwoname) {
        this.bosstwoname = bosstwoname;
    }

    public String getBosstwoiconurl() {
        return bosstwoiconurl;
    }

    public void setBosstwoiconurl(String bosstwoiconurl) {
        this.bosstwoiconurl = bosstwoiconurl;
    }

    public String getBossthreename() {
        return bossthreename;
    }

    public void setBossthreename(String bossthreename) {
        this.bossthreename = bossthreename;
    }

    public String getBossthreeiconurl() {
        return bossthreeiconurl;
    }

    public void setBossthreeiconurl(String bossthreeiconurl) {
        this.bossthreeiconurl = bossthreeiconurl;
    }

    public String getBossfourname() {
        return bossfourname;
    }

    public void setBossfourname(String bossfourname) {
        this.bossfourname = bossfourname;
    }

    public String getBossfouriconurl() {
        return bossfouriconurl;
    }

    public void setBossfouriconurl(String bossfouriconurl) {
        this.bossfouriconurl = bossfouriconurl;
    }

    public String getBossfivename() {
        return bossfivename;
    }

    public void setBossfivename(String bossfivename) {
        this.bossfivename = bossfivename;
    }

    public String getBossfiveiconurl() {
        return bossfiveiconurl;
    }

    public void setBossfiveiconurl(String bossfiveiconurl) {
        this.bossfiveiconurl = bossfiveiconurl;
    }

    @Override
    protected Class getProviderClass() {
        return SourceProvider.class;
    }

    @Override
    public String toString() {
        return "ClanWarModel{" +
                "id=" + id +
                ", startdate='" + startdate + '\'' +
                ", enddate='" + enddate + '\'' +
                ", bossonename='" + bossonename + '\'' +
                ", bossoneiconurl='" + bossoneiconurl + '\'' +
                ", bosstwoname='" + bosstwoname + '\'' +
                ", bosstwoiconurl='" + bosstwoiconurl + '\'' +
                ", bossthreename='" + bossthreename + '\'' +
                ", bossthreeiconurl='" + bossthreeiconurl + '\'' +
                ", bossfourname='" + bossfourname + '\'' +
                ", bossfouriconurl='" + bossfouriconurl + '\'' +
                ", bossfivename='" + bossfivename + '\'' +
                ", bossfiveiconurl='" + bossfiveiconurl + '\'' +
                '}';
    }
}
