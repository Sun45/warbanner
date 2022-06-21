package cn.sun45.warbanner.document.database.source.models;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.character.CharacterHelper;
import cn.sun45.warbanner.clanwar.ClanwarHelper;
import cn.sun45.warbanner.document.statics.TeamType;

/**
 * Created by Sun45 on 2022/6/9
 * 阵容信息数据模型
 */
@Entity(tableName = "team", primaryKeys = {"lang", "id"})
public class TeamModel implements Serializable {
    @NonNull
    @ColumnInfo
    public String lang;

    @NonNull
    @ColumnInfo
    private int id;

    @ColumnInfo
    private String sn;

    @ColumnInfo
    private int stage;

    @ColumnInfo
    private boolean auto;

    @ColumnInfo
    private boolean finish;

    @ColumnInfo
    private String boss;

    @ColumnInfo
    private int damage;

    @ColumnInfo
    private int characterone;
    @ColumnInfo
    private int charactertwo;
    @ColumnInfo
    private int characterthree;
    @ColumnInfo
    private int characterfour;
    @ColumnInfo
    private int characterfive;

    @ColumnInfo
    private String detail;

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

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public boolean isAuto() {
        return auto;
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    /**
     * 获取刀型
     */
    public TeamType getType() {
        if (auto) {
            return TeamType.AUTO;
        } else if (finish) {
            return TeamType.FINISH;
        } else {
            return TeamType.NORMAL;
        }
    }

    public class TimeLine {
        private String title;
        private String description;
        private String videoUrl;
        private List<Image> imageList;

        public TimeLine() {
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public List<Image> getImageList() {
            return imageList;
        }

        public void setImageList(List<Image> imageList) {
            this.imageList = imageList;
        }

        public String getContent() {
            return title;
        }

        public String getLink() {
            return videoUrl;
        }

        public String getShare() {
            List<String> list = new ArrayList<>();
            if (!TextUtils.isEmpty(title)) {
                list.add(title);
            }
            if (!TextUtils.isEmpty(description)) {
                list.add(description);
            }
            if (!TextUtils.isEmpty(videoUrl)) {
                list.add(videoUrl);
            }
            if (imageList != null) {
                for (Image image : imageList) {
                    list.add(image.getShare());
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                if (i != 0) {
                    stringBuilder.append("\n");
                }
                stringBuilder.append(list.get(i));
            }
            return stringBuilder.toString();
        }
    }

    public class Image {
        private String thumb;

        private String poster;

        public Image(String thumb, String poster) {
            this.thumb = thumb;
            this.poster = poster;
        }

        public String getThumb() {
            if (!TextUtils.isEmpty(thumb)) {
                return thumb;
            }
            return poster;
        }

        public String getPoster() {
            if (!TextUtils.isEmpty(poster)) {
                return poster;
            }
            return thumb;
        }

        public String getShare() {
            String share = null;
            if (!TextUtils.isEmpty(poster)) {
                share = poster;
            } else if (!TextUtils.isEmpty(thumb)) {
                share = thumb;
            }
            return share;
        }
    }

    public List<TimeLine> getTimeLines() {
        List<TimeLine> list = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(detail);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                String text = jsonObject.optString("text");
                String note = jsonObject.optString("note");
                String url = jsonObject.optString("url");
                JSONArray imageJSONArray = jsonObject.optJSONArray("image");
                List<Image> imageList = new ArrayList<>();
                if (imageJSONArray != null) {
                    for (int j = 0; j < imageJSONArray.length(); j++) {
                        JSONObject imageJSONObject = imageJSONArray.getJSONObject(i);
                        String imageurl = imageJSONObject.optString("url");
                        String imagesource = imageJSONObject.optString("source");
                        Image image = new Image(imageurl, imagesource);
                        imageList.add(image);
                    }
                }
                TimeLine timeLine = new TimeLine();
                timeLine.setTitle(text);
                timeLine.setDescription(note);
                timeLine.setVideoUrl(url);
                timeLine.setImageList(imageList);
                list.add(timeLine);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public String getShare(List<CharacterModel> characterModelList) {
        StringBuilder share = new StringBuilder();
        share.append(sn + " " + damage + "w");
        share.append("\n");
        share.append(CharacterHelper.findCharacterById(characterone, characterModelList).getName() +
                CharacterHelper.findCharacterById(charactertwo, characterModelList).getName() +
                CharacterHelper.findCharacterById(characterthree, characterModelList).getName() +
                CharacterHelper.findCharacterById(characterfour, characterModelList).getName() +
                CharacterHelper.findCharacterById(characterfive, characterModelList).getName());
        for (TimeLine timeLine : getTimeLines()) {
            share.append("\n");
            share.append(timeLine.getShare());
        }
        return share.toString();
    }

    @Override
    public String toString() {
        return "TeamModel{" +
                "lang='" + lang + '\'' +
                ", id=" + id +
                ", sn='" + sn + '\'' +
                ", stage=" + stage +
                ", auto=" + auto +
                ", finish=" + finish +
                ", boss='" + boss + '\'' +
                ", damage=" + damage +
                ", characterone=" + characterone +
                ", charactertwo=" + charactertwo +
                ", characterthree=" + characterthree +
                ", characterfour=" + characterfour +
                ", characterfive=" + characterfive +
                ", detail='" + detail + '\'' +
                '}';
    }
}
