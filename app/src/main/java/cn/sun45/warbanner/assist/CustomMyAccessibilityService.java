package cn.sun45.warbanner.assist;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.Arrays;

import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2022/2/20
 * 自定义无障碍辅助服务
 */
public class CustomMyAccessibilityService extends AccessibilityService {
    private static final String TAG = "CustomMyAccessibilityService";

    private static final String[] SKIP_PACKAGES = new String[]{
            Utils.getPackageName(),
            "com.android.systemui"
    };

    private static final String[] GAME_PACKAGES = new String[]{
            "com.bilibili.priconne",
            "jp.co.cygames.princessconnectredive",
            "tw.sonet.princessconnect",
            "com.kakaogames.pcr",
            "com.ini3.PrincessConnectTH",
            "com.crunchyroll.princessconnectredive"
    };

    @Override
    protected void onServiceConnected() {
        Utils.logD(TAG, "onServiceConnected");
        super.onServiceConnected();
        AssistManager.getInstance().setService(this);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        CharSequence packageName = event.getPackageName();
        Utils.logD(TAG, "onAccessibilityEvent packageName:" + packageName + " eventType:0x" + Integer.toHexString(event.getEventType()));
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            packageName = nodeInfo.getPackageName();
        }
        if (packageName != null && packageName.length() > 0) {
            if (Arrays.asList(SKIP_PACKAGES).contains(packageName)) {
            } else if (Arrays.asList(GAME_PACKAGES).contains(packageName)) {
                AssistManager.getInstance().startShow();
            } else {
                AssistManager.getInstance().stopShow();
            }
        }
    }

    @Override
    public void onInterrupt() {
        Utils.logD(TAG, "onInterrupt");
        AssistManager.getInstance().setService(null);
    }
}
