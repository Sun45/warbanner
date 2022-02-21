package cn.sun45.warbanner.logic.clanwar;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.clanwar.ClanwarHelper;
import cn.sun45.warbanner.document.StaticHelper;
import cn.sun45.warbanner.document.db.clanwar.TeamModel;
import cn.sun45.warbanner.document.preference.ClanwarPreference;
import cn.sun45.warbanner.framework.logic.BaseLogic;
import cn.sun45.warbanner.framework.logic.RequestListener;
import cn.sun45.warbanner.util.GithubUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Url;

/**
 * Created by Sun45 on 2021/5/23
 * 会战Logic
 */
public class ClanwarLogic extends BaseLogic {
    //在线文档
    interface HtmlDoc {
        @GET
        Call<JSONObject> get(@Url String url);
    }

    //镜像库
    interface Library {
        @Headers("Cache-Control: no-store")
        @GET
        Call<String> getJson(@Url String url);
    }

    /**
     * 获取单元格信息列表
     *
     * @param url 数据地址
     */
    public Call<JSONObject> getCellList(String url, final RequestListener<List<HtmlDocCellModel>> listener) {
        Call call = retrofit(url.substring(0, url.lastIndexOf("/") + 1)).create(HtmlDoc.class).get(url.substring(url.lastIndexOf("/") + 1));
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                JSONObject result = response.body();
                logD("getTeamList result:" + result);
                if (result == null) {
                    listener.onSuccess(null);
                    return;
                }
                List<HtmlDocCellModel> cellModels = new ArrayList<>();
                JSONObject data = result.optJSONObject("data");
                JSONObject initialAttributedText = data.optJSONObject("initialAttributedText");
                JSONArray text = initialAttributedText.optJSONArray("text");
                JSONArray textarray = text.optJSONArray(0);
                JSONArray c = textarray.optJSONArray(textarray.length() - 1).optJSONObject(0).optJSONArray("c");
                JSONObject cellObject = c.optJSONObject(1);
//                if (cellObject != null) {
                int n = 0;
                for (int i = 0; i < cellObject.length(); i++) {
                    HtmlDocCellModel cellModel = new HtmlDocCellModel();
                    JSONObject total = null;
                    do {
                        total = cellObject.optJSONObject(n + "");
                        n++;
                    } while (total == null);
                    JSONArray strarray = total.optJSONArray("2");
                    if (strarray != null) {
                        String content = strarray.optString(1);
                        cellModel.setContent(content);
                    }
                    String link = total.optString("6");
                    cellModel.setLink(link);
                    cellModels.add(cellModel);
                }
//                }
                listener.onSuccess(cellModels);

//                for (int i = list.size() - 1; i >= 0; i--) {
//                    HtmlDocCellModel htmlDocCellModel = list.get(i);
//                    if (TextUtils.isEmpty(htmlDocCellModel.getContent()) && TextUtils.isEmpty(htmlDocCellModel.getLink())) {
//                        list.remove(i);
//                    }
//                }
//                String stageregex = null;
//                switch (stage) {
//                    case 1:
//                        stageregex = "(a|A)";
//                        break;
//                    case 2:
//                        stageregex = "(b|B)";
//                        break;
//                    case 3:
//                        stageregex = "(c|C)";
//                        break;
//                    default:
//                        break;
//                }
//                try {
//                    List<TeamModel> teamModels = new ArrayList<>();
//                    TeamModel teamModel = null;
//                    JSONArray remarks = null;
//                    int currentcount = 0;
//                    for (int i = 0; i < list.size(); i++) {
//                        boolean matchteamstart = false;
//                        if ((i + 7) < list.size()) {
//                            String boss = list.get(i).getContent();
//                            String number = list.get(i + 1).getContent();
//                            String characterone = list.get(i + 2).getContent();
//                            String charactertwo = list.get(i + 3).getContent();
//                            String characterthree = list.get(i + 4).getContent();
//                            String characterfour = list.get(i + 5).getContent();
//                            String characterfive = list.get(i + 6).getContent();
//                            String damage = list.get(i + 7).getContent();
//                            if (!TextUtils.isEmpty(boss) && Pattern.matches("^" + stageregex + bossregex + "$", boss)
//                                    && !TextUtils.isEmpty(number) && Pattern.matches("^" + stageregex + "t?" + bossregex + "[0-9]{2}$", number)
//                                    && !TextUtils.isEmpty(characterone)
//                                    && !TextUtils.isEmpty(charactertwo)
//                                    && !TextUtils.isEmpty(characterthree)
//                                    && !TextUtils.isEmpty(characterfour)
//                                    && !TextUtils.isEmpty(characterfive)
//                                    && !TextUtils.isEmpty(damage) && Pattern.matches("^[0-9]+(w|W)$", damage)) {
//                                matchteamstart = true;
//                                if (teamModel != null) {
//                                    teamModel.setRemarks(remarks.toString());
//                                    teamModels.add(teamModel);
//                                }
//                                currentcount++;
//                                teamModel = new TeamModel();
//                                remarks = new JSONArray();
//                                teamModel.setBoss(boss);
//                                teamModel.setNumber(number);
//                                teamModel.setSortnumber(stage + "-" + (10000 + currentcount));
//                                teamModel.setStage(stage);
//                                teamModel.setCharacterone(characterone);
//                                teamModel.setCharactertwo(charactertwo);
//                                teamModel.setCharacterthree(characterthree);
//                                teamModel.setCharacterfour(characterfour);
//                                teamModel.setCharacterfive(characterfive);
//                                teamModel.setDamage(damage);
//                                damage = damage.replace("w", "");
//                                damage = damage.replace("W", "");
//                                int damagenumber = Integer.valueOf(damage);
//                                teamModel.setDamagenumber(damagenumber);
//                                teamModel.setAuto(Pattern.matches("^" + stageregex + "t" + bossregex + "[0-9]{2}$", number));
//                                i += 7;
//                            }
//                        }
//                        if (!matchteamstart && teamModel != null) {
//                            String content = list.get(i).getContent();
//                            String link = list.get(i).getLink();
//                            if (!TextUtils.isEmpty(content)) {
//                                JSONObject remark = new JSONObject();
//                                remark.put("content", content);
//                                remark.put("link", link);
//                                remarks.put(remark);
//                            }
//                        }
//                    }
//                    if (teamModel != null) {
//                        teamModel.setRemarks(remarks.toString());
//                        teamModels.add(teamModel);
//                    }
//                    for (TeamModel model : teamModels) {
//                        DbHelper.insert(activity, model);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                listener.onFailed(t.getMessage());
            }
        });
        return call;
    }

    /**
     * 获取作业数据列表
     *
     * @param date  会战日期 202107
     * @param stage 阶段 1,2,3,4
     */
    public Call<String> getTeamModelList(String date, int stage, final RequestListener<List<TeamModel>> listener) {
        String path = StaticHelper.CLANWAR_PATH_BASE + date;
        switch (stage) {
            case 1:
                path += StaticHelper.CLANWAR_PATH_ONE;
                break;
            case 2:
                path += StaticHelper.CLANWAR_PATH_TWO;
                break;
            case 3:
                path += StaticHelper.CLANWAR_PATH_THREE;
                break;
            case 4:
                path += StaticHelper.CLANWAR_PATH_ONE;
                break;
            default:
                break;
        }
        String url = GithubUtils.getFileUrl(new ClanwarPreference().getWay(), StaticHelper.CLANWAR_OWNER, StaticHelper.CLANWAR_REPOSITORY, path);
        Call call = retrofit(url).create(Library.class).getJson(url);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                logD("getTeamModelList result:" + result);
                if (TextUtils.isEmpty(result)) {
                    listener.onSuccess(null);
                } else {
                    JSONArray array = null;
                    try {
                        array = new JSONArray(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (array == null) {
                        listener.onSuccess(null);
                    } else {
                        List<TeamModel> teamModelList = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.optJSONObject(i);
                            String boss = jsonObject.optString("boss");
                            String id = jsonObject.optString("id");
                            JSONArray characters = jsonObject.optJSONArray("characters");
                            if (characters == null || characters.length() < 5) {
                                continue;
                            }
                            JSONObject characterone = characters.optJSONObject(0);
                            JSONObject charactertwo = characters.optJSONObject(1);
                            JSONObject characterthree = characters.optJSONObject(2);
                            JSONObject characterfour = characters.optJSONObject(3);
                            JSONObject characterfive = characters.optJSONObject(4);
                            int characteroneid = characterone.optInt("id");
                            JSONArray characteronerequirements = characterone.optJSONArray("requirements");
                            int charactertwoid = charactertwo.optInt("id");
                            JSONArray charactertworequirements = charactertwo.optJSONArray("requirements");
                            int characterthreeid = characterthree.optInt("id");
                            JSONArray characterthreerequirements = characterthree.optJSONArray("requirements");
                            int characterfourid = characterfour.optInt("id");
                            JSONArray characterfourrequirements = characterfour.optJSONArray("requirements");
                            int characterfiveid = characterfive.optInt("id");
                            JSONArray characterfiverequirements = characterfive.optJSONArray("requirements");
                            if (characteroneid == 0 || charactertwoid == 0 || characterthreeid == 0 || characterfourid == 0 || characterfiveid == 0) {
                                continue;
                            }
                            JSONObject time = jsonObject.optJSONObject("time");
                            JSONArray comments = jsonObject.optJSONArray("comments");
                            JSONArray labels = jsonObject.optJSONArray("labels");
                            int damage = jsonObject.optInt("standard_damage");
                            JSONArray sources = jsonObject.optJSONArray("sources");
                            TeamModel teamModel = new TeamModel();
                            teamModel.setDate(date);
                            teamModel.setStage(stage);
                            teamModel.setNumber(id);
                            teamModel.setSortnumber(i + "");
                            teamModel.setBoss(boss);
                            teamModel.setDamage(damage);
                            teamModel.setEllipsisdamage(damage / 10000);
                            teamModel.setAuto(id.contains("t"));
                            teamModel.setCharacterone(characteroneid * 100 + 1);
                            teamModel.setCharactertwo(charactertwoid * 100 + 1);
                            teamModel.setCharacterthree(characterthreeid * 100 + 1);
                            teamModel.setCharacterfour(characterfourid * 100 + 1);
                            teamModel.setCharacterfive(characterfiveid * 100 + 1);
                            if (comments != null) {
                                teamModel.setSketch(comments.toString());
                            }
                            if (sources != null) {
                                try {
                                    JSONArray remarks = new JSONArray();
                                    for (int j = 0; j < sources.length(); j++) {
                                        JSONObject source = sources.optJSONObject(j);
                                        String sourcedescription = source.optString("description");
                                        String sourceauthor = source.optString("author");
                                        JSONObject sourcedamage=source.optJSONObject("damage");
                                        JSONObject sourcetime=source.optJSONObject("time");
                                        JSONArray sourcelinks = source.optJSONArray("links");
                                        JSONArray sourceimages = source.optJSONArray("images");
                                        JSONArray sourcecomments = source.optJSONArray("comments");
                                        JSONObject remark = new JSONObject();
                                        remark.put("content", sourcedescription);
                                        if (sourcelinks != null && sourcelinks.length() > 0) {
                                            remark.put("link", sourcelinks.getString(0));
                                        }
                                        remark.put("images", sourceimages);
                                        remark.put("comments", sourcecomments);
                                        remarks.put(remark);
                                    }
                                    teamModel.setRemarks(remarks.toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            teamModelList.add(teamModel);
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
