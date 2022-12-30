package cn.sun45.warbanner.logic.app;

import org.json.JSONObject;

import cn.sun45.warbanner.document.statics.StaticHelper;
import cn.sun45.warbanner.framework.logic.BaseLogic;
import cn.sun45.warbanner.framework.logic.RequestListener;
import cn.sun45.warbanner.util.GithubUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Url;

/**
 * Created by Sun45 on 2021/5/29
 * 应用Logic
 */
public class AppLogic extends BaseLogic {
    interface Api {
        @Headers("Cache-Control: no-store")
        @GET
        Call<JSONObject> checkAppVersion(@Header("User-Agent") String userAgent, @Url String url);
    }

    public Call<JSONObject> checkAppVersion(final RequestListener<AppModel> listener) {
        String url = GithubUtils.getFileUrl(GithubUtils.TYPE_RAW, StaticHelper.APK_OWNER, StaticHelper.APK_REPOSITORY, StaticHelper.APK_BRANCH, StaticHelper.APP_UPDATE_LOG_PATH);
        Call<JSONObject> call = retrofit(url).create(Api.class).checkAppVersion(getUserAgent(), url);
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                JSONObject obj = response.body();
                if (obj != null) {
                    AppModel appModel = new AppModel();
                    appModel.setVersionCode(obj.optInt("versionCode"));
                    appModel.setVersionName(obj.optString("versionName"));
                    appModel.setFource(obj.optBoolean("fource"));
                    appModel.setContent(obj.optString("content"));
                    listener.onSuccess(appModel);
                } else {
                    listener.onFailed("onResponse empty");
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                listener.onFailed(t.getMessage());
            }
        });
        return call;
    }
}
