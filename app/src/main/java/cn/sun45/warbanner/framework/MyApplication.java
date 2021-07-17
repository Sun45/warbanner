package cn.sun45.warbanner.framework;

import android.app.Application;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;

import java.util.HashMap;

import cn.sun45.warbanner.datamanager.source.SourceDataProcessHelper;
import cn.sun45.warbanner.document.preference.AppPreference;
import cn.sun45.warbanner.framework.image.ImageRequester;
import cn.sun45.warbanner.framework.record.ErrorRecordManager;
import cn.sun45.warbanner.framework.ui.BaseActivity;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2021/5/19
 */
public class MyApplication extends Application implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "MyApplication";

    public static final boolean testing = false;

    public static MyApplication application;

    private static BaseActivity currentActivity;

    //系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        this.application = this;

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);

        ImageRequester.init(this);

        SourceDataProcessHelper.init(this);
    }

    /**
     * 设置当前Activity
     *
     * @param currentActivity
     */
    public static void setCurrentActivity(BaseActivity currentActivity) {
        MyApplication.currentActivity = currentActivity;
    }

    /**
     * 获取当前Activity
     *
     * @return
     */
    public static BaseActivity getCurrentActivity() {
        return currentActivity;
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     *
     * @param thread 线程
     * @param ex     异常
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        handleException(ex);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mDefaultHandler.uncaughtException(thread, ex);
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param throwable 异常
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(final Throwable throwable) {
        if (throwable == null) {
            return false;
        }
        String errorinfo = Utils.printError(throwable);
        Log.e(TAG, errorinfo);
        ErrorRecordManager.getInstance().save(errorinfo);
        new AppPreference().setAbnormal_exit(true);
        return true;
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static long getTimecurrent() {
        return System.currentTimeMillis();
    }
}
