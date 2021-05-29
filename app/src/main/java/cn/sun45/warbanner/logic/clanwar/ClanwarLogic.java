package cn.sun45.warbanner.logic.clanwar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.framework.logic.BaseLogic;
import cn.sun45.warbanner.framework.logic.RequestListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
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
                for (int i = 0; i < cellObject.length(); i++) {
                    HtmlDocCellModel cellModel = new HtmlDocCellModel();
                    JSONObject total = cellObject.optJSONObject(i + "");
                    if (total != null) {
                        JSONArray strarray = total.optJSONArray("2");
                        if (strarray != null) {
                            String content = strarray.optString(1);
                            cellModel.setContent(content);
                        }
                        String link = total.optString("6");
                        cellModel.setLink(link);
                    }
                    cellModels.add(cellModel);
                }
//                }
                listener.onSuccess(cellModels);
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                listener.onFailed(t.getMessage());
            }
        });
        return call;
    }
}
