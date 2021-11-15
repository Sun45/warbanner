package cn.sun45.warbanner.framework.logic;

import java.io.File;

import cn.sun45.warbanner.framework.MyApplication;
import cn.sun45.warbanner.util.FileUtil;
import cn.sun45.warbanner.util.Utils;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by Sun45 on 2019/10/28.
 * 网络请求基础类
 */
public class BaseLogic {
    private static final String TAG = "BaseLogic";

    protected static OkHttpClient mOkHttpClient;

    public static void init() {
        if (mOkHttpClient == null) {
            synchronized (BaseLogic.class) {
                if (mOkHttpClient == null) {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    File cachefile = new File(FileUtil.getExternalFilesDir("OkHttpClient"));
                    builder.cache(new Cache(cachefile, FileUtil.calculateDiskCacheSize(cachefile)));
                    if (MyApplication.testing) {
                        // Log信息拦截器
                        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);//这里可以选择拦截级别
                        //设置 Debug Log 模式
                        builder.addInterceptor(loggingInterceptor);
                    }
                    mOkHttpClient = builder.build();
                }
            }
        }
    }

    protected void logD(String msg) {
        Utils.logD(this.getClass().getName(), msg);
    }

    /**
     * 生成请求
     *
     * @param baseUrl
     * @return
     */
    protected Retrofit retrofit(String baseUrl) {
        if (!baseUrl.endsWith("/") && baseUrl.contains("/")) {
            String str = baseUrl.substring(0, baseUrl.lastIndexOf("/") + 1);
            if (!(str.equals("http://") || str.equals("https://"))) {
                baseUrl = str;
            }
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(mOkHttpClient)
                .addConverterFactory(new ConverterFactory())
                .build();
        return retrofit;
    }

    protected String getUserAgent() {
        return Utils.getPackageName() + " " + Utils.getVersionName() + " " + System.getProperty("http.agent");
    }
}
