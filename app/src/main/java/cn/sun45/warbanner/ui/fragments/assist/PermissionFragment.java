package cn.sun45.warbanner.ui.fragments.assist;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.assist.AssistManager;
import cn.sun45.warbanner.framework.ui.BaseFragment;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2022/2/20
 * 权限设置Fragment
 */
public class PermissionFragment extends BaseFragment {
    private CardView mAlertWindowLay;
    private TextView mAlertWindow;

    private CardView mAccessibilityLay;
    private TextView mAccessibility;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_permission;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mAlertWindowLay = mRoot.findViewById(R.id.alert_window_lay);
        mAlertWindow = mRoot.findViewById(R.id.alert_window);
        mAccessibilityLay = mRoot.findViewById(R.id.accessibility_lay);
        mAccessibility = mRoot.findViewById(R.id.accessibility);

        mAlertWindowLay.setOnClickListener(v -> AssistManager.requestAlertWindowPermissions());
        mAccessibilityLay.setOnClickListener(v -> AssistManager.requestAccessibilityPermissions());
    }

    @Override
    protected void dataRequest() {

    }

    @Override
    protected void onShow() {
        {
            SpannableStringBuilder builder = new SpannableStringBuilder(Utils.getString(R.string.user_permission_alert_window) + " - ");
            String granted;
            int color;
            if (AssistManager.canDrawOverlays()) {
                granted = Utils.getString(R.string.user_permission_granted);
                color = Utils.getColor(R.color.green_dark);
            } else {
                granted = Utils.getString(R.string.user_permission_notgranted);
                color = Utils.getColor(R.color.red_dark);
            }
            int start = builder.length();
            int end = start + granted.length();
            builder.append(granted);
            builder.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mAlertWindow.setText(builder, TextView.BufferType.SPANNABLE);
        }
        {
            SpannableStringBuilder builder = new SpannableStringBuilder(Utils.getString(R.string.user_permission_accessibility) + " - ");
            String granted;
            int color;
            if (AssistManager.isAccessibilitySettingsOn()) {
                granted = Utils.getString(R.string.user_permission_granted);
                color = Utils.getColor(R.color.green_dark);
            } else {
                granted = Utils.getString(R.string.user_permission_notgranted);
                color = Utils.getColor(R.color.red_dark);
            }
            int start = builder.length();
            int end = start + granted.length();
            builder.append(granted);
            builder.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mAccessibility.setText(builder, TextView.BufferType.SPANNABLE);
        }
    }

    @Override
    protected void onHide() {
    }
}
