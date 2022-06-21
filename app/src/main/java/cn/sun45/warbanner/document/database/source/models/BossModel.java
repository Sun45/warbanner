package cn.sun45.warbanner.document.database.source.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

/**
 * Created by Sun45 on 2022/6/9
 * boss信息数据模型
 */
@Entity(tableName = "boss", primaryKeys = {"lang", "id"})
public class BossModel {
    @NonNull
    @ColumnInfo
    public String lang;

    @NonNull
    @ColumnInfo
    private int id;

    @ColumnInfo
    private int bossIndex;

    @ColumnInfo
    private String name;

    @ColumnInfo
    private String iconUrl;

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBossIndex() {
        return bossIndex;
    }

    public void setBossIndex(int bossIndex) {
        this.bossIndex = bossIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    @Override
    public String toString() {
        return "BossModel{" +
                "lang='" + lang + '\'' +
                ", id=" + id +
                ", bossIndex=" + bossIndex +
                ", name='" + name + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                '}';
    }
}
