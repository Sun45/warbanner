package cn.sun45.warbanner.assist;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;

import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2022/2/20
 * 自定义无障碍辅助服务
 */
public class CustomMyAccessibilityService extends AccessibilityService {
    private static final String TAG = "CustomMyAccessibilityService";

    @Override
    protected void onServiceConnected() {
        Utils.logD(TAG, "onServiceConnected");
        super.onServiceConnected();
        AssistManager.getInstance().setService(this);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        CharSequence packageName = event.getPackageName();
        Utils.logD(TAG, "onAccessibilityEvent packageName:" + packageName);
        if (packageName != null && packageName.equals("com.bilibili.priconne")) {
            AssistManager.getInstance().startShow();
        } else if (packageName != null && packageName.equals(Utils.getPackageName())) {
        } else if (packageName != null && packageName.equals("com.android.systemui")) {
        } else {
            AssistManager.getInstance().stopShow();
        }
    }

    @Override
    public void onInterrupt() {
        Utils.logD(TAG, "onInterrupt");
        AssistManager.getInstance().setService(null);
    }
}
