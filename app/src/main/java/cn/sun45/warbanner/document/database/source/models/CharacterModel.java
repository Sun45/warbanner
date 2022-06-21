package cn.sun45.warbanner.document.database.source.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

/**
 * Created by Sun45 on 2022/6/9
 * 角色信息数据模型
 */
@Entity(tableName = "character", primaryKeys = {"lang", "id"})
public class CharacterModel {
    @NonNull
    @ColumnInfo
    public String lang;

    @NonNull
    @ColumnInfo
    private int id;

    @ColumnInfo
    private int group;

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

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
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
        return "CharacterModel{" +
                "lang='" + lang + '\'' +
                ", id=" + id +
                ", group=" + group +
                ", name='" + name + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                '}';
    }
}
