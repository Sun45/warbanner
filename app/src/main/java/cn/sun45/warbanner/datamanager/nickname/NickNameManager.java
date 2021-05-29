package cn.sun45.warbanner.datamanager.nickname;

import android.app.Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import cn.sun45.warbanner.document.StaticHelper;
import cn.sun45.warbanner.framework.MyApplication;
import cn.sun45.warbanner.framework.document.db.DbHelper;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2021/5/24
 * 昵称数据获取管理
 */
public class NickNameManager {
    private static final String TAG = "NickNameManager";

    private Activity activity;

    //单例对象
    private static NickNameManager instance;

    public static void init(Activity activity) {
        if (instance == null) {
            synchronized (NickNameManager.class) {
                if (instance == null) {
                    instance = new NickNameManager(activity);
                }
            }
        }
    }

    public static NickNameManager getInstance() {
        return instance;
    }

    public NickNameManager(Activity activity) {
        this.activity = activity;
    }

    private IActivityCallBack iActivityCallBack;

    public void setiActivityCallBack(IActivityCallBack iActivityCallBack) {
        this.iActivityCallBack = iActivityCallBack;
    }

    /**
     * 昵称数据获取
     */
    public void checkData() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                String url = StaticHelper.NICKNAME_URL;
                try {
                    Document document = Jsoup.connect(url)
                            .timeout(5000)
                            .get();
                    Elements elements = document.select("table[class=js-csv-data csv-data js-file-line-container] tbody tr");
                    Utils.logD(TAG, "size " + elements.size());
                    JSONObject object = new JSONObject();
                    for (Element element : elements) {
                        Elements ems = element.select("td");
                        String id = ems.get(1).text() + "01";
                        String name = ems.get(3).text();
                        String nicknamestr = ems.get(4).text();
                        String[] nicknames = nicknamestr.split(",");
                        JSONArray nicknamearray = new JSONArray();
                        for (String nickname : nicknames) {
                            nicknamearray.put(nickname);
                        }
                        object.put(id, nicknamearray);
                    }
                    Utils.logD(TAG, object.toString());
                } catch (IOException | JSONException e) {
                    Utils.logD(TAG, "IOException e" + e.getMessage());
                    e.printStackTrace();
                }
                if (iActivityCallBack != null) {
                    iActivityCallBack.nickNameGetFinished();
                }
            }
        }.start();
    }

    public interface IActivityCallBack {
        void nickNameGetFinished();
    }
}
