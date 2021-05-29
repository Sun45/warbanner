package cn.sun45.warbanner.ui.views.web;

import android.graphics.Bitmap;

/**
 * Created by Sun45 on 2019/10/29.
 * 网页浏览器监听
 */
public interface MyWebListener {
    void onWebStart();

    void onWebfinish();

    void onReceivedTitle(String title);

    void onReceivedIcon(Bitmap bitmap);

    void onRequest(String url);

    void onWebError();
}
