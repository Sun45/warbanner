package cn.sun45.warbanner.document.db.clanwar;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.sun45.warbanner.framework.document.db.BaseDbTableModel;
import cn.sun45.warbanner.framework.document.db.annotation.DbTableConfigure;
import cn.sun45.warbanner.framework.document.db.annotation.DbTableParamConfigure;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2021/5/23
 * 阵容信息数据模型
 */
@DbTableConfigure(tablename = "team")
public class TeamModel extends BaseDbTableModel {
    //会战日期 202107
    @DbTableParamConfigure
    private String date;
    //阶段 1,2,3
    @DbTableParamConfigure
    private int stage;

    //阵容编号
    @DbTableParamConfigure
    private String number;
    //阵容排序号
    @DbTableParamConfigure
    private String sortnumber;

    //boss编号(A1)
    @DbTableParamConfigure
    private String boss;

    //伤害数值(5500000)
    @DbTableParamConfigure
    private int damage;
    //伤害省略数值(550)
    @DbTableParamConfigure
    private int ellipsisdamage;

    //自动刀
    @DbTableParamConfigure
    private boolean auto;

    //角色id信息
    @DbTableParamConfigure
    private int characterone;
    @DbTableParamConfigure
    private int charactertwo;
    @DbTableParamConfigure
    private int characterthree;
    @DbTableParamConfigure
    private int characterfour;
    @DbTableParamConfigure
    private int characterfive;

    //简述
    @DbTableParamConfigure
    private String sketch;

    //备注
    @DbTableParamConfigure
    private String remarks;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSortnumber() {
        return sortnumber;
    }

    public void setSortnumber(String sortnumber) {
        this.sortnumber = sortnumber;
    }

    public String getBoss() {
        return boss;
    }

    public void setBoss(String boss) {
        this.boss = boss;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getEllipsisdamage() {
        return ellipsisdamage;
    }

    public void setEllipsisdamage(int ellipsisdamage) {
        this.ellipsisdamage = ellipsisdamage;
    }

    public boolean isAuto() {
        return auto;
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }

    public int getCharacterone() {
        return characterone;
    }

    public void setCharacterone(int characterone) {
        this.characterone = characterone;
    }

    public int getCharactertwo() {
        return charactertwo;
    }

    public void setCharactertwo(int charactertwo) {
        this.charactertwo = charactertwo;
    }

    public int getCharacterthree() {
        return characterthree;
    }

    public void setCharacterthree(int characterthree) {
        this.characterthree = characterthree;
    }

    public int getCharacterfour() {
        return characterfour;
    }

    public void setCharacterfour(int characterfour) {
        this.characterfour = characterfour;
    }

    public int getCharacterfive() {
        return characterfive;
    }

    public void setCharacterfive(int characterfive) {
        this.characterfive = characterfive;
    }

    public String getSketch() {
        return sketch;
    }

    public void setSketch(String sketch) {
        this.sketch = sketch;
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

    public JSONObject getJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("date", date);
            jsonObject.put("stage", stage);
            jsonObject.put("number", number);
            jsonObject.put("sortnumber", sortnumber);
            jsonObject.put("boss", boss);
            jsonObject.put("damage", damage);
            jsonObject.put("ellipsisdamage", ellipsisdamage);
            jsonObject.put("auto", auto);
            jsonObject.put("characterone", characterone);
            jsonObject.put("charactertwo", charactertwo);
            jsonObject.put("characterthree", characterthree);
            jsonObject.put("characterfour", characterfour);
            jsonObject.put("characterfive", characterfive);
            jsonObject.put("sketch", sketch);
            jsonObject.put("remarks", remarks);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public String getShare() {
        String share = number + " " + ellipsisdamage + "w";
        try {
            JSONArray sketchArray = new JSONArray(sketch);
            for (int i = 0; i < sketchArray.length(); i++) {
                share += "\n" + sketchArray.get(i);
            }
            JSONArray remarkarray = new JSONArray(remarks);
            for (int i = 0; i < remarkarray.length(); i++) {
                JSONObject object = remarkarray.optJSONObject(i);
                String content = object.optString("content");
                content = Utils.replaceBlank(content);
                String link = object.optString("link");
                JSONArray comments = object.optJSONArray("comments");
                JSONArray images = object.optJSONArray("images");
                if (!TextUtils.isEmpty(content)) {
                    share += "\n" + content;
                }
                if (!TextUtils.isEmpty(link)) {
                    share += "\n" + link;
                }
                if (comments != null && comments.length() > 0) {
                    share += "\n" + comments.getString(0);
                }
                if (images != null && images.length() > 0) {
                    share += "\n" + images.getString(0);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return share;
    }
}
