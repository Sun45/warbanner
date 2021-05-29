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

    @Override
    protected Class getProviderClass() {
        return ClanWarProvider.class;
    }
}
