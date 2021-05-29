package cn.sun45.warbanner.logic.source;

import org.json.JSONObject;

import cn.sun45.warbanner.document.StaticHelper;
import cn.sun45.warbanner.framework.logic.BaseLogic;
import cn.sun45.warbanner.framework.logic.RequestListener;
import cn.sun45.warbanner.util.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Url;

/**
 * Created by Sun45 on 2021/5/23
 * 资源Logic
 */
public class SourceLogic extends BaseLogic {
    interface Api {
        @GET
        Call<JSONObject> checkDatabaseVersion(@Header("User-Agent") String userAgent, @Url String url);
    }

    public Call<JSONObject> checkDatabaseVersion(final RequestListener<Long> listener) {
        Call<JSONObject> call = retrofit(StaticHelper.API_URL).create(Api.class).checkDatabaseVersion(getUserAgent(), StaticHelper.LATEST_VERSION_URL);
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                JSONObject obj = response.body();
                long serverVersion = 0;
                try {
                    serverVersion = obj.getLong("TruthVersion");
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFailed(e.getMessage());
                }
                listener.onSuccess(serverVersion);
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                listener.onFailed(t.getMessage());
            }
        });
        return call;
    }
}
