package cn.sun45.warbanner.datamanager.clanwar;

import android.app.Activity;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.sun45.warbanner.framework.logic.RequestListener;
import cn.sun45.warbanner.logic.clanwar.ClanwarLogic;
import cn.sun45.warbanner.logic.clanwar.HtmlDocCellModel;
import cn.sun45.warbanner.ui.views.web.MyWeb;
import cn.sun45.warbanner.ui.views.web.MyWebListenerImp;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2021/5/27
 * 在线文档请求帮助类
 */
public class HtmlDocRequestHelper {
    private static final String TAG = "HtmlDocRequestHelper";

    /**
     * 请求在线文档数据
     *
     * @param activity
     * @param url
     * @param start
     * @param end
     * @param listener
     */
    public static void request(Activity activity, String url, int start, int end, HtmlDocManagerListener listener) {
        Utils.logD(TAG, "request url:" + url + " start:" + start + " end:" + end);
        MyWeb myWeb = new MyWeb(activity);
        myWeb.setListener(new MyWebListenerImp() {
            boolean requested = false;

            @Override
            public void onRequest(String url) {
                if (!requested) {
                    //"https://docs.qq.com/dop-api/get/sheet?tab=s3dwgf&padId=300000000$ZQZZgREoQooR&subId=s3dwgf&startrow=0&endrow=417&xsrf=cf874c7ed6ddf074&_r=0.4841650719923919&outformat=1&normal=1&preview_token=&nowb=1&rev=8891"
                    if (url.startsWith("https://docs.qq.com/dop-api/get/sheet?")) {
                        requested = true;
                        Matcher m = Pattern.compile("startrow=[0-9]+(&|$)").matcher(url);
                        if (m.find()) {
                            url = url.replace(m.group(), "startrow=" + start + "&");
                        }
                        m = Pattern.compile("endrow=[0-9]+(&|$)").matcher(url);
                        if (m.find()) {
                            url = url.replace(m.group(), "endrow=" + end + "&");
                        }
                        getHtmlDocCellModelList(url, listener);
                    }
                }
            }
        });
        myWeb.loadUrl(url);
    }

    /**
     * 在线文档数据网络请求
     *
     * @param url
     * @param listener
     */
    public static void getHtmlDocCellModelList(String url, HtmlDocManagerListener listener) {
        Utils.logD(TAG, "getHtmlDocCellModelList url:" + url);
        new ClanwarLogic().getCellList(url, new RequestListener<List<HtmlDocCellModel>>() {
            @Override
            public void onSuccess(List<HtmlDocCellModel> result) {
                Utils.logD(TAG, "getHtmlDocCellModelList onSuccess");
                listener.get(result);
            }

            @Override
            public void onFailed(String message) {
                Utils.logD(TAG, "getHtmlDocCellModelList onFailed message:" + message);
                listener.fail(message);
            }
        });
    }

    public interface HtmlDocManagerListener {
        void get(List<HtmlDocCellModel> list);

        void fail(String message);
    }
}
