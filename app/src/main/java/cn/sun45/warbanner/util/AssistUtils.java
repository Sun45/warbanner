package cn.sun45.warbanner.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;

import cn.sun45.warbanner.assist.AssistManager;
import cn.sun45.warbanner.framework.MyApplication;

/**
 * Created by Sun45 on 2022/2/23
 * 辅助功能方法类
 */
public class AssistUtils {
    private static final String TAG = "AssistUtils";

    /**
     * 拥有系统弹框权限
     */
    public static boolean canDrawOverlays() {
        boolean canDrawOverlays = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(MyApplication.application)) {
                canDrawOverlays = false;
            }
        }
        return canDrawOverlays;
    }

    /**
     * 申请系统弹框权限
     */
    public static void requestAlertWindowPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Utils.logD(TAG, ">=Build.VERSION_CODES.M");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//8.0以上
                Utils.logD(TAG, "8.0以上");
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                MyApplication.getCurrentActivity().startActivity(intent);
            } else {//6.0-8.0
                Utils.logD(TAG, "6.0-8.0");
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + MyApplication.application.getPackageName()));
                MyApplication.getCurrentActivity().startActivity(intent);
            }
        }
    }

    /**
     * 居中添加到窗口
     */
    public static WindowManager.LayoutParams addViewToWindow(View v, int width, int height, boolean touchable) {
        return addViewToWindow(v, width, height, 8388659, touchable);
    }

    /**
     * 添加到窗口
     */
    public static WindowManager.LayoutParams addViewToWindow(View v, int width, int height, int gravity, boolean touchable) {
        if (v.isAttachedToWindow()) {
            return null;
        }
        boolean canDrawOverlays = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(MyApplication.application)) {
                canDrawOverlays = false;
            }
        }
        WindowManager.LayoutParams lp = null;
        if (canDrawOverlays) {
            WindowManager windowManager = (WindowManager) MyApplication.application.getSystemService(Context.WINDOW_SERVICE);
            lp = new WindowManager.LayoutParams();
            lp.width = width;
            lp.height = height;
            lp.type = (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT <= 24) ? WindowManager.LayoutParams.TYPE_TOAST : Build.VERSION.SDK_INT < 26 ? WindowManager.LayoutParams.TYPE_PHONE : WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            lp.format = PixelFormat.TRANSLUCENT;
            lp.windowAnimations = android.R.style.Animation_Toast;
            if (!touchable) {
                lp.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                lp.flags = 17368856;
            } else {
                lp.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                lp.flags = 17368840;
            }
//            lp.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
            lp.dimAmount = -1f;
            lp.gravity = gravity;
//            if (sideleft) {
//                lp.gravity = Gravity.CENTER_VERTICAL | Gravity.LEFT;
//            } else {
//                lp.gravity = 8388659;
//            }
//        lp.buttonBrightness = BRIGHTNESS_OVERRIDE_OFF;
//        lp.systemUiVisibility = SYSTEM_UI_FLAG_LOW_PROFILE;
            windowManager.addView(v, lp);
        }
        return lp;
    }

    /**
     * 更新窗口绘制
     */
    public static void updateWindow(View v, WindowManager.LayoutParams lp) {
        if (v.isAttachedToWindow()) {
            WindowManager windowManager = (WindowManager) MyApplication.application.getSystemService(Context.WINDOW_SERVICE);
            windowManager.updateViewLayout(v, lp);
        }
    }

    /**
     * 从窗口移除
     */
    public static void removeViewFromWindow(View v) {
        if (v.isAttachedToWindow()) {
            WindowManager windowManager = (WindowManager) MyApplication.application.getSystemService(Context.WINDOW_SERVICE);
            if (v != null) {
                windowManager.removeView(v);
            }
        }
    }

    /**
     * 无障碍辅助服务已启动
     */
    public static boolean isAccessibilitySettingsOn() {
        return AssistManager.getInstance().serviceOn();
    }

    /**
     * 申请无障碍服务权限
     */
    public static void requestAccessibilityPermissions() {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.getCurrentActivity().startActivity(intent);
    }
}