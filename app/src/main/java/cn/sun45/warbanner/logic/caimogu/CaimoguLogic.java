package cn.sun45.warbanner.logic.caimogu;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.document.database.source.models.BossModel;
import cn.sun45.warbanner.document.database.source.models.CharacterModel;
import cn.sun45.warbanner.document.database.source.models.TeamModel;
import cn.sun45.warbanner.document.statics.StaticHelper;
import cn.sun45.warbanner.framework.logic.BaseLogic;
import cn.sun45.warbanner.framework.logic.RequestListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Sun45 on 2022/6/9
 * 踩蘑菇Logic
 */
public class CaimoguLogic extends BaseLogic {
    interface Source {
        @Headers("x-requested-with: XMLHttpRequest")
        @GET
        Call<String> getBaseData(@Url String url, @Query("data") String data, @Query("lang") String lang);

        @Headers("x-requested-with: XMLHttpRequest")
        @GET
        Call<String> getTeamData(@Url String url, @Query("data") String data, @Query("lang") String lang);
    }

    /**
     * 获取基础数据
     *
     * @param data yyyy-MM
     * @param lang zh-cn zh-tw
     */
    public Call<String> getBaseData(final RequestListener<CaimoguBaseData> listener, String data, String lang) {
        Call call = retrofit(StaticHelper.CAIMOGU_BASE).create(Source.class).getBaseData(StaticHelper.CAIMOGU_BASEDATA_URL, data, lang);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                logD("getBaseData result:" + result);
                if (TextUtils.isEmpty(result)) {
                    listener.onSuccess(null);
                } else {
                    JSONObject object = null;
                    try {
                        object = new JSONObject(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (object == null) {
                        listener.onSuccess(null);
                    } else {
                        CaimoguBaseData caimoguBaseData = new CaimoguBaseData();
                        List<CharacterModel> characterModels = new ArrayList<>();
                        List<BossModel> bossModels = new ArrayList<>();
                        JSONArray dataJsonArray = object.optJSONArray("data");
                        for (int i = 0; i < 3; i++) {
                            JSONArray characterArray = dataJsonArray.optJSONArray(i);
                            if (characterArray != null) {
                                for (int j = 0; j < characterArray.length(); j++) {
                                    JSONObject characterObject = characterArray.optJSONObject(j);
                                    CharacterModel characterModel = new CharacterModel();
                                    characterModel.setLang(lang);
                                    characterModel.setId(characterObject.optInt("id"));
                                    characterModel.setGroup(i);
                                    characterModel.setName(characterObject.optString("iconValue"));
                                    characterModel.setIconUrl(characterObject.optString("iconFilePath"));
                                    characterModels.add(characterModel);
                                }
                            }
                        }
                        for (int i = 3; i < 4; i++) {
                            JSONArray bossArray = dataJsonArray.optJSONArray(i);
                            if (bossArray != null) {
                                for (int j = 0; j < bossArray.length(); j++) {
                                    JSONObject bossObject = bossArray.optJSONObject(j);
                                    BossModel bossModel = new BossModel();
                                    bossModel.setLang(lang);
                                    bossModel.setId(bossObject.optInt("id"));
                                    bossModel.setBossIndex(j);
                                    bossModel.setName(bossObject.optString("iconValue"));
                                    bossModel.setIconUrl(bossObject.optString("iconFilePath"));
                                    bossModels.add(bossModel);
                                }
                            }
                        }
                        caimoguBaseData.setCharacterModels(characterModels);
                        caimoguBaseData.setBossModels(bossModels);
                        listener.onSuccess(caimoguBaseData);
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                listener.onFailed(t.getMessage());
            }
        });
        return call;
    }

    /**
     * 获取作业数据
     *
     * @param data yyyy-MM
     * @param lang zh-cn zh-tw
     */
    public Call<String> getTeamData(final RequestListener<List<TeamModel>> listener, String data, String lang) {
        Call call = retrofit(StaticHelper.CAIMOGU_BASE).create(Source.class).getTeamData(StaticHelper.CAIMOGU_TEAMDATA_URL, data, lang);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                logD("getTeamData result:" + result);
                if (TextUtils.isEmpty(result)) {
                    listener.onSuccess(null);
                } else {
                    JSONObject object = null;
                    try {
                        object = new JSONObject(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (object == null) {
                        listener.onSuccess(null);
                    } else {
                        List<TeamModel> teamModelList = new ArrayList<>();
                        JSONArray dataJsonArray = object.optJSONArray("data");
                        for (int i = 0; i < dataJsonArray.length(); i++) {
                            JSONObject dataJsonArrayObject = dataJsonArray.optJSONObject(i);
                            JSONArray homeworkJsonArray = dataJsonArrayObject.optJSONArray("homework");
                            for (int j = 0; j < homeworkJsonArray.length(); j++) {
                                JSONObject jsonObject = homeworkJsonArray.optJSONObject(j);
                                int id = jsonObject.optInt("id");
                                String sn = jsonObject.optString("sn");
                                if (sn.length() < 4) {
                                    continue;
                                }
                                int stage = 0;
                                switch (sn.substring(0, 1)) {
                                    case "A":
                                    case "a":
                                        stage = 1;
                                        break;
                                    case "B":
                                    case "b":
                                        stage = 2;
                                        break;
                                    case "C":
                                    case "c":
                                        stage = 3;
                                        break;
                                    case "D":
                                    case "d":
                                        stage = 4;
                                        break;
                                    case "E":
                                    case "e":
                                        stage = 5;
                                        break;
                                }
                                String boss = sn.substring(0, 1) + sn.substring(sn.length() - 3, sn.length() - 2);
                                JSONArray unit = jsonObject.optJSONArray("unit");
                                if (unit == null || unit.length() < 5) {
                                    continue;
                                }
                                int characteroneid = unit.optInt(0);
                                int charactertwoid = unit.optInt(1);
                                int characterthreeid = unit.optInt(2);
                                int characterfourid = unit.optInt(3);
                                int characterfiveid = unit.optInt(4);
                                if (characteroneid == 0 || charactertwoid == 0 || characterthreeid == 0 || characterfourid == 0 || characterfiveid == 0) {
                                    continue;
                                }
                                int damage = jsonObject.optInt("damage");
                                String video = jsonObject.optString("video");
                                TeamModel teamModel = new TeamModel();
                                teamModel.setLang(lang);
                                teamModel.setId(id);
                                teamModel.setSn(sn);
                                teamModel.setStage(stage);
                                teamModel.setAuto(sn.contains("t") || sn.contains("T"));
                                teamModel.setFinish(sn.contains("w") || sn.contains("W"));
                                teamModel.setBoss(boss);
                                teamModel.setDamage(damage);
                                teamModel.setCharacterone(characteroneid);
                                teamModel.setCharactertwo(charactertwoid);
                                teamModel.setCharacterthree(characterthreeid);
                                teamModel.setCharacterfour(characterfourid);
                                teamModel.setCharacterfive(characterfiveid);
                                teamModel.setDetail(video);
                                teamModelList.add(teamModel);
                            }
                        }
                        listener.onSuccess(teamModelList);
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                listener.onFailed(t.getMessage());
            }
        });
        return call;
    }
}
