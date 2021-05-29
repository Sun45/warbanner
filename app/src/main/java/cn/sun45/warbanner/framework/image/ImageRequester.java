package cn.sun45.warbanner.framework.image;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;

import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cn.sun45.warbanner.framework.MyApplication;
import cn.sun45.warbanner.framework.logic.BaseLogic;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2019/10/29.
 * 图片请求类
 */
public class ImageRequester extends BaseLogic {
    private static final String TAG = "ImageRequester";
    static Picasso singleInstance;

    /**
     * 初始化网络接口请求和图片请求
     *
     * @param context
     */
    public static void init(Context context) {
        init();
        if (singleInstance == null) {
            synchronized (ImageRequester.class) {
                if (singleInstance == null) {
                    singleInstance = new Picasso.Builder(context).downloader(new OkHttp3Downloader(mOkHttpClient)).build();
                    singleInstance.setIndicatorsEnabled(MyApplication.testing);
                    singleInstance.setLoggingEnabled(MyApplication.testing);
                }
            }
        }
    }

    /**
     * 取消图片请求
     *
     * @param tag
     */
    public static void cancel(String tag) {
        singleInstance.cancelTag(tag);
    }

    /**
     * 字符转码
     *
     * @param url url
     * @return
     */
    private static String encode(String url) {
        try {
            url = URLEncoder.encode(url, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * 地址判断和转码
     *
     * @param url url
     * @return
     */
    private static String parseUrl(String url) {
        if (TextUtils.isEmpty(url)) {
//            throw new IllegalArgumentException("url must not be null.");
        } else if (!url.startsWith("http")) {
            url = encode(url);
        }
        Utils.logD(TAG, "url:" + url);
        return url;
    }

    /**
     * 创建图片请求加载器
     *
     * @param url            url
     * @param placeHolderRes 占位图资源
     * @return
     */
    private static RequestCreator makeRequestCreator(String url, int placeHolderRes) {
        RequestCreator requestCreator;
        if (!TextUtils.isEmpty(url) && url.startsWith(File.separator)) {
            requestCreator = singleInstance.load(new File(url));
        } else {
            url = parseUrl(url);
            requestCreator = singleInstance.load(url);
        }
//        requestCreator.noFade();
        if (placeHolderRes == 0) {
            requestCreator.noPlaceholder();
        } else {
            requestCreator.placeholder(placeHolderRes);
            requestCreator.error(placeHolderRes);
        }
        return requestCreator;
    }

    /**
     * 创建请求
     *
     * @param url url
     * @return
     */
    public static ImageRequestLoader request(String url) {
        return new ImageRequestLoader(makeRequestCreator(url, 0));
    }

    /**
     * 创建带占位图的请求
     *
     * @param url            url
     * @param placeHolderRes 占位图资源
     * @return
     */
    public static ImageRequestLoader request(String url, int placeHolderRes) {
        return new ImageRequestLoader(makeRequestCreator(url, placeHolderRes));
    }

    /**
     * 创建按尺寸截取的请求
     *
     * @param url            url
     * @param placeHolderRes 占位图资源
     * @param width          目标宽
     * @param height         目标高
     * @return
     */
    public static ImageRequestLoader requestCrop(String url, int placeHolderRes, int width, int height) {
        RequestCreator requestCreator = makeRequestCreator(url, placeHolderRes);
        requestCreator.resize(width, height).centerCrop();
//            requestCreator.onlyScaleDown();
//            requestCreator.config(Bitmap.Config.RGB_565);
        return new ImageRequestLoader(requestCreator);
    }

    /**
     * 创建按尺寸截取偏上的请求
     *
     * @param url            url
     * @param placeHolderRes 占位图资源
     * @param width          目标宽
     * @param height         目标高
     * @return
     */
    public static ImageRequestLoader requestCropTop(String url, int placeHolderRes, int width, int height) {
        RequestCreator requestCreator = makeRequestCreator(url, placeHolderRes);
        requestCreator.resize(width, height).centerCrop(Gravity.TOP);
//            requestCreator.onlyScaleDown();
//            requestCreator.config(Bitmap.Config.RGB_565);
        return new ImageRequestLoader(requestCreator);
    }

    /**
     * 创建按尺寸适应的请求
     *
     * @param url            url
     * @param placeHolderRes 占位图资源
     * @param width          目标宽
     * @param height         目标高
     * @return
     */
    public static ImageRequestLoader requestInside(String url, int placeHolderRes, int width, int height) {
        RequestCreator requestCreator = makeRequestCreator(url, placeHolderRes);
        requestCreator.resize(width, height).centerInside();
//            requestCreator.onlyScaleDown();
//            requestCreator.config(Bitmap.Config.RGB_565);
        return new ImageRequestLoader(requestCreator);
    }
}
