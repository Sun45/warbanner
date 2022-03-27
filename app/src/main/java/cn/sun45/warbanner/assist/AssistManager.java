package cn.sun45.warbanner.assist;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.view.Gravity;
import android.view.WindowManager;

import cn.sun45.warbanner.datamanager.source.SourceManager;
import cn.sun45.warbanner.document.preference.SetupPreference;
import cn.sun45.warbanner.framework.MyApplication;
import cn.sun45.warbanner.util.AssistUtils;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2022/2/23
 * 辅助功能管理
 */
public class AssistManager {
    private static final String TAG = "AssistManager";

    //单例对象
    private static AssistManager instance;

    public static AssistManager getInstance() {
        if (instance == null) {
            synchronized (SourceManager.class) {
                if (instance == null) {
                    instance = new AssistManager();
                }
            }
        }
        return instance;
    }

    public AssistManager() {
        init();
    }

    /**
     * 拥有辅助功能权限
     */
    public static boolean hasPermission() {
        return AssistUtils.canDrawOverlays() && AssistUtils.isAccessibilitySettingsOn();
    }

    //无障碍辅助服务
    private CustomMyAccessibilityService service;

    public void setService(CustomMyAccessibilityService service) {
        this.service = service;
    }

    //服务已启动
    public boolean serviceOn() {
        return service != null;
    }

    private boolean show;
    private AssistDataModel mAssistDataModel;
    private int tapInterval;
    private int[] currentTapPoint;

    private AssistPanelView mPanelView;
    private WindowManager.LayoutParams mPanelLayoutParams;
    private AssistPanelView.AssistPanelViewListener mPanelViewListener = new AssistPanelView.AssistPanelViewListener() {
        @Override
        public void shrink() {
            mPanelLayoutParams.width = mPanelView.getWidth();
            mPanelLayoutParams.height = mPanelView.getHeight();
            AssistUtils.updateWindow(mPanelView.getView(), mPanelLayoutParams);
        }

        @Override
        public void areaShow(boolean areaShow) {
            if (areaShow) {
                showArea();
            } else {
                hideArea();
            }
        }

        @Override
        public void characterSelect(int characterSelection) {
            currentTapPoint = mAssistDataModel.getTapPoint(characterSelection);
            autoTap();
        }
    };

    private AssistAreaShowView mAreaShowView;

    private void init() {
        mAssistDataModel = new AssistDataModel();
        tapInterval = new SetupPreference().getTapinterval();
        mPanelView = new AssistPanelView(MyApplication.application, mPanelViewListener);
        mAreaShowView = new AssistAreaShowView(MyApplication.application);
    }

    public AssistDataModel getDataModel() {
        return mAssistDataModel;
    }

    public void setTapInterval(int tapInterval) {
        this.tapInterval = tapInterval;
    }

    public void startShow() {
        synchronized (this) {
            Utils.logD(TAG, "startShow show:" + show);
            if (!new SetupPreference().isAutoclick()) {
                Utils.logD(TAG, "自动连点未启用");
                return;
            }
            if (!show) {
                show = true;
                mPanelLayoutParams = AssistUtils.addViewToWindow(mPanelView.getView(), mPanelView.getWidth(), mPanelView.getHeight(), Gravity.CENTER_VERTICAL | Gravity.LEFT, true);
                if (mPanelView.isAreaShow()) {
                    showArea();
                }
            }
        }
    }

    public void stopShow() {
        synchronized (this) {
            Utils.logD(TAG, "stopShow show:" + show);
            if (show) {
                show = false;
                if (mPanelView.isAreaShow()) {
                    hideArea();
                }
                AssistUtils.removeViewFromWindow(mPanelView.getView());
            }
        }
    }

    private void showArea() {
        Utils.logD(TAG, "showArea");
        int[] screenSize = Utils.getScreenSize();
        AssistUtils.addViewToWindow(mAreaShowView.getView(), mAreaShowView.getWidth(), mAreaShowView.getHeight(), false);
    }

    private void hideArea() {
        Utils.logD(TAG, "hideArea");
        AssistUtils.removeViewFromWindow(mAreaShowView.getView());
    }

    public void autoTap() {
        if (currentTapPoint == null || service == null) {
            return;
        }
        Path path = new Path();
        path.moveTo(currentTapPoint[0], currentTapPoint[1]);
//        Utils.logD(TAG, "autoTap tapInterval:" + tapInterval);
        GestureDescription.StrokeDescription sd = new GestureDescription.StrokeDescription(path, 0, tapInterval);
        service.dispatchGesture(new GestureDescription.Builder().addStroke(sd).build(), new AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
//                Utils.logD(TAG, "onCompleted");
                super.onCompleted(gestureDescription);
                autoTap();
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                Utils.logD(TAG, "onCancelled");
                super.onCancelled(gestureDescription);
                mPanelView.cancelTap();
            }
        }, null);
    }
}