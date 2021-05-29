package cn.sun45.warbanner.ui.views.web;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.Map;

import cn.sun45.warbanner.util.Utils;


/**
 * Created by Sun45 on 2019/10/29.
 * 网页浏览器
 */
public class MyWeb extends WebView {
    private String TAG = "MyWeb";
    private MyWebListener listener;

    //请求地址前半部分
    private String head;

    public void setListener(MyWebListenerImp listener) {
        this.listener = listener;
    }

    public void setListener(MyWebListener listener) {
        this.listener = listener;
    }

    public MyWeb(Context context) {
        super(context);
        init();
    }

    public MyWeb(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyWeb(Context context, MyWebListener listener) {
        super(context);
        this.listener = listener;
        init();
    }

    @Override
    public void loadUrl(String url) {
        super.loadUrl(url);
        if (TextUtils.isEmpty(head)) {
            head = url.substring(0, url.lastIndexOf("/"));
        }
    }

    @Override
    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        super.loadUrl(url, additionalHttpHeaders);
        if (TextUtils.isEmpty(head)) {
            head = url.substring(0, url.lastIndexOf("/"));
        }
    }

    protected void init() {
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView webView, String url, String message, JsResult result) {
                Utils.logD(TAG, "onJsAlert " + message);
                return false;
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                Utils.logD(TAG, "onReceivedTitle");
                super.onReceivedTitle(view, title);
                if (listener != null) {
                    listener.onReceivedTitle(title);
                }
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                Utils.logD(TAG, "onReceivedIcon");
                super.onReceivedIcon(view, icon);
                if (listener != null) {
                    listener.onReceivedIcon(icon);
                }
            }
        });
        setWebViewClient(new WebViewClient() {
            //网页重定向
            boolean mIsRedirect;

            //加载失败
            boolean error;

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {//页面开始加载
                Utils.logD(TAG, "onPageStarted url:" + url);
                mIsRedirect = false;
                error = false;
                super.onPageStarted(view, url, favicon);
                if (listener != null) {
                    listener.onWebStart();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {//页面加载完成
                Utils.logD(TAG, "onPageFinished url:" + url);
                super.onPageFinished(view, url);
//                if (mIsRedirect) {
//                    return;
//                }
                if (listener != null) {
                    listener.onReceivedTitle(getTitle());
                    if (!error) {
                        listener.onWebfinish();
                    }
                }
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView webView, String url) {
                Utils.logD(TAG, "shouldInterceptRequest url:" + url);
                if (listener != null) {
                    listener.onRequest(url);
                }
                return super.shouldInterceptRequest(webView, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Utils.logD(TAG, "shouldOverrideUrlLoading url:" + url);
                mIsRedirect = true;
//                view.loadUrl(url);
//                return true;
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Utils.logD(TAG, "onReceivedError errorCode:" + errorCode + " description:" + description + " failingUrl:" + failingUrl);
                super.onReceivedError(view, errorCode, description, failingUrl);
                if (!TextUtils.isEmpty(failingUrl) && failingUrl.substring(0, failingUrl.lastIndexOf("/")).contains(head)) {
                    onError();
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                onReceivedError(view, error.getErrorCode(), error.getDescription().toString(), request.getUrl().toString());
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                String url = request.getUrl().getPath();
                Utils.logD(TAG, "onReceivedHttpError url:" + url);
                int statusCode = errorResponse.getStatusCode();
                if (!request.isForMainFrame() && url.endsWith("/favicon.ico")) {
                } else if (404 == statusCode || 500 == statusCode) {
                    Utils.logD(TAG, "onReceivedHttpError");
                    super.onReceivedHttpError(view, request, errorResponse);
                    onError();
                }
            }

            private void onError() {
                error = true;
                if (listener != null) {
                    listener.onWebError();
                }
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // 接受所有网站的证书，忽略SSL错误，执行访问网页
                handler.proceed();
            }
        });
        setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Utils.logD(TAG, "onDownloadStart url:" + url);
            }
        });

        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);//允许使用js
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        String appCachePath = getContext().getCacheDir().getAbsolutePath();
        webSettings.setAppCachePath(appCachePath);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);

//        if (ConnectivityReceiver.withNetWork()) {
//            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
//        } else {
////            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存，只从网络获取数据.
//        }

        //支持屏幕缩放
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        //不显示webview缩放按钮
        webSettings.setDisplayZoomControls(false);

//        webSettings.setUseWideViewPort(true);
//        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

//        webSettings.setTextSize(WebSettings.TextSize.SMALLEST);

//        webSettings.setSavePassword(true);

        webSettings.setAllowContentAccess(false);
        webSettings.setSaveFormData(false);

        setHorizontalScrollBarEnabled(false);
        setVerticalScrollBarEnabled(false);

        final String USER_AGENT_STRING = webSettings.getUserAgentString() + " Rong/2.0";
        webSettings.setUserAgentString(USER_AGENT_STRING);
        webSettings.setPluginState(WebSettings.PluginState.ON);

        if (Build.VERSION.SDK_INT >= 19) {
            webSettings.setLoadsImagesAutomatically(true);
        } else {
            webSettings.setLoadsImagesAutomatically(false);
        }

//        if (Build.VERSION.SDK_INT >= 19) // KITKAT
//        {
//            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//Android 5.0上Webview默认不允许加载Http与Https混合内容
            //两者都可以
            webSettings.setMixedContentMode(webSettings.getMixedContentMode());
            //mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        WebView.setWebContentsDebuggingEnabled(true);
    }

    public boolean back() {
        Utils.logD(TAG, "back");
        if (canGoBack()) {
            goBack();
            return true;
        }
        return false;
    }

    public void clearCache() {
        clearCache(true);
        clearHistory();
    }

    public void clearCookie() {
        CookieSyncManager.createInstance(getContext());
        CookieSyncManager.getInstance().startSync();
        CookieManager.getInstance().removeSessionCookie();
    }

    /**
     * 这个两个在 API level 21 被抛弃
     * CookieManager.getInstance().removeSessionCookie();
     * CookieManager.getInstance().removeAllCookie();
     * <p>
     * 推荐使用这两个， level 21 新加的
     * CookieManager.getInstance().removeSessionCookies();
     * CookieManager.getInstance().removeAllCookies();
     **/
    public void removeCookies() {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.flush();
        } else {
            CookieSyncManager.createInstance(getContext());
            CookieSyncManager.getInstance().sync();
        }
    }
}