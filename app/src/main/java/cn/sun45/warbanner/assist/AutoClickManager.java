package cn.sun45.warbanner.assist;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Color;
import android.graphics.Path;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Arrays;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.datamanager.source.SourceManager;
import cn.sun45.warbanner.document.preference.SetupPreference;
import cn.sun45.warbanner.framework.MyApplication;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2022/2/20
 * 连点功能管理
 */
public class AutoClickManager {
    private static final String TAG = "AutoClickManager";

    //单例对象
    private static AutoClickManager instance;

    public static AutoClickManager getInstance() {
        if (instance == null) {
            synchronized (SourceManager.class) {
                if (instance == null) {
                    instance = new AutoClickManager();
                }
            }
        }
        return instance;
    }

    public static boolean hasPermission() {
        return AssistManager.canDrawOverlays() && AssistManager.isAccessibilitySettingsOn();
    }

    private MyAccessibilityService service;

    public void setService(MyAccessibilityService service) {
        this.service = service;
    }

    private boolean show;
    private WindowManager.LayoutParams mPanelLayoutParams;
    private View mPanelLay;
    private CheckBox mAreaShowCheck;
    private CheckBox mButtonLayShowCheck;
    private View mButtonLay;
    private TextView mCharacterOne;
    private TextView mCharacterTwo;
    private TextView mCharacterThree;
    private TextView mCharacterFour;
    private TextView mCharacterFive;

    private int characterSelection;

    private boolean areashow;
    private AreaShowView mAreaShow;

    private boolean areaDataInit;
    private AreaDataModel areaDataModel;

    private void dataInit() {
        if (!areaDataInit) {
            areaDataInit = true;
            areaDataModel = new AreaDataModel();
        }
    }

    private View getShowLay() {
        if (mPanelLay == null) {
            dataInit();
            mPanelLay = LayoutInflater.from(MyApplication.application).inflate(R.layout.autoclick_showlay, null);
            mAreaShowCheck = mPanelLay.findViewById(R.id.area_show_check);
            mAreaShowCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        showArea();
                    } else {
                        hideArea(false);
                    }
                }
            });
            mButtonLayShowCheck = mPanelLay.findViewById(R.id.button_lay_show_check);
            mButtonLayShowCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mButtonLay.setVisibility(View.VISIBLE);
                        mPanelLayoutParams.height = Utils.dip2px(MyApplication.application, 340);
                        updateShow();
                    } else {
                        mButtonLay.setVisibility(View.GONE);
                        mPanelLayoutParams.height = Utils.dip2px(MyApplication.application, 90);
                        updateShow();
                    }
                }
            });
            mButtonLay = mPanelLay.findViewById(R.id.button_lay);
            mCharacterOne = mPanelLay.findViewById(R.id.character_one);
            mCharacterOne.setOnClickListener(v -> {
                Utils.logD(TAG, "one onClick characterSelection:" + characterSelection);
                if (characterSelection != 1) {
                    characterSelection = 1;
                    mCharacterOne.setBackgroundColor(Utils.getColor(R.color.orange_dark));
                    autoTap();
                }
            });
            mCharacterTwo = mPanelLay.findViewById(R.id.character_two);
            mCharacterTwo.setOnClickListener(v -> {
                Utils.logD(TAG, "two onClick characterSelection:" + characterSelection);
                if (characterSelection != 2) {
                    characterSelection = 2;
                    mCharacterTwo.setBackgroundColor(Utils.getColor(R.color.orange_dark));
                    autoTap();
                }
            });
            mCharacterThree = mPanelLay.findViewById(R.id.character_three);
            mCharacterThree.setOnClickListener(v -> {
                Utils.logD(TAG, "three onClick characterSelection:" + characterSelection);
                if (characterSelection != 3) {
                    characterSelection = 3;
                    mCharacterThree.setBackgroundColor(Utils.getColor(R.color.orange_dark));
                    autoTap();
                }
            });
            mCharacterFour = mPanelLay.findViewById(R.id.character_four);
            mCharacterFour.setOnClickListener(v -> {
                Utils.logD(TAG, "four onClick characterSelection:" + characterSelection);
                if (characterSelection != 4) {
                    characterSelection = 4;
                    mCharacterFour.setBackgroundColor(Utils.getColor(R.color.orange_dark));
                    autoTap();
                }
            });
            mCharacterFive = mPanelLay.findViewById(R.id.character_five);
            mCharacterFive.setOnClickListener(v -> {
                Utils.logD(TAG, "five onClick characterSelection:" + characterSelection);
                if (characterSelection != 5) {
                    characterSelection = 5;
                    mCharacterFive.setBackgroundColor(Utils.getColor(R.color.orange_dark));
                    autoTap();
                }
            });
        }
        return mPanelLay;
    }

    private View getAreaShow() {
        if (mAreaShow == null) {
            mAreaShow = new AreaShowView(MyApplication.application, areaDataModel);
            mAreaShow.setLayoutParams(new FrameLayout.LayoutParams(Utils.getScreenwidth(), Utils.getScreenheight()));
        }
        return mAreaShow;
    }

    public void startShow() {
        Utils.logD(TAG, "startShow");
        if (!new SetupPreference().isAutoclick()) {
            Utils.logD(TAG, "自动连点未启用");
            return;
        }
        if (!show) {
            show = true;
            mPanelLayoutParams = AssistManager.addViewToWindow(getShowLay(), Utils.dip2px(MyApplication.application, 120), Utils.dip2px(MyApplication.application, 340), true);
            if (areashow) {
                showArea();
            }
        }
    }

    private void updateShow() {
        Utils.logD(TAG, "updateShow");
        AssistManager.updateWindow(getShowLay(), mPanelLayoutParams);
    }

    public void stopShow() {
        Utils.logD(TAG, "stopShow");
        if (show) {
            show = false;
            hideArea(true);
            AssistManager.removeViewFromWindow(getShowLay());
        }
    }

    private void showArea() {
        Utils.logD(TAG, "showArea");
        if (!areashow) {
            areashow = true;
            AssistManager.addViewToWindow(getAreaShow(), Utils.getScreenwidth(), Utils.getScreenheight(), false);
        }
    }

    private void hideArea(boolean auto) {
        Utils.logD(TAG, "hideArea auto:" + auto);
        if (areashow) {
            if (!auto) {
                areashow = false;
            }
            AssistManager.removeViewFromWindow(getAreaShow());
        }
    }

    public void autoTap() {
        int[] point = areaDataModel.getTapPoint(characterSelection);
        if (point == null) {
            return;
        }
//        Utils.logD(TAG, "autoTap point:" + Arrays.toString(point));
        Path path = new Path();
        path.moveTo(point[0], point[1]);
        GestureDescription.StrokeDescription sd = new GestureDescription.StrokeDescription(path, 0, 10);
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
                characterSelection = 0;
                mCharacterOne.setBackgroundColor(0x00000000);
                mCharacterTwo.setBackgroundColor(0x00000000);
                mCharacterThree.setBackgroundColor(0x00000000);
                mCharacterFour.setBackgroundColor(0x00000000);
                mCharacterFive.setBackgroundColor(0x00000000);
            }
        }, null);
    }
}
