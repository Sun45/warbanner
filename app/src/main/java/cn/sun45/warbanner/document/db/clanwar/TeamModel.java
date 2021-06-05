package cn.sun45.warbanner.document.db.clanwar;

import cn.sun45.warbanner.framework.document.db.BaseDbTableModel;
import cn.sun45.warbanner.framework.document.db.annotation.DbTableConfigure;
import cn.sun45.warbanner.framework.document.db.annotation.DbTableParamConfigure;

/**
 * Created by Sun45 on 2021/5/23
 * 阵容信息数据模型
 */
@DbTableConfigure(tablename = "team")
public class TeamModel extends BaseDbTableModel {
    //唯一值
    @DbTableParamConfigure(iskeyparm = true)
    private String number;

    @DbTableParamConfigure
    private int stage;

    @DbTableParamConfigure
    private String boss;

    @DbTableParamConfigure
    private String damage;
    @DbTableParamConfigure
    private int damagenumber;

    @DbTableParamConfigure
    private boolean auto;

    @DbTableParamConfigure
    private String characterone;
    @DbTableParamConfigure
    private String charactertwo;
    @DbTableParamConfigure
    private String characterthree;
    @DbTableParamConfigure
    private String characterfour;
    @DbTableParamConfigure
    private String characterfive;

    @DbTableParamConfigure
    private String remarks;

    @DbTableParamConfigure
    private boolean collect;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public String getBoss() {
        return boss;
    }

    public void setBoss(String boss) {
        this.boss = boss;
    }

    public String getDamage() {
        return damage;
    }

    public void setDamage(String damage) {
        this.damage = damage;
    }

    public int getDamagenumber() {
        return damagenumber;
    }

    public void setDamagenumber(int damagenumber) {
        this.damagenumber = damagenumber;
    }

    public boolean isAuto() {
        return auto;
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }

    public String getCharacterone() {
        return characterone;
    }

    public void setCharacterone(String characterone) {
        this.characterone = characterone;
    }

    public String getCharactertwo() {
        return charactertwo;
    }

    public void setCharactertwo(String charactertwo) {
        this.charactertwo = charactertwo;
    }

    public String getCharacterthree() {
        return characterthree;
    }

    public void setCharacterthree(String characterthree) {
        this.characterthree = characterthree;
    }

    public String getCharacterfour() {
        return characterfour;
    }

    public void setCharacterfour(String characterfour) {
        this.characterfour = characterfour;
    }

    public String getCharacterfive() {
        return characterfive;
    }

    public void setCharacterfive(String characterfive) {
        this.characterfive = characterfive;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public boolean isCollect() {
        return collect;
    }

    public void setCollect(boolean collect) {
        this.collect = collect;
    }

    @Override
    protected Class getProviderClass() {
        return ClanWarProvider.class;
    }
}
