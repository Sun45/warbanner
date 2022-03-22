package cn.sun45.warbanner.ui.fragments.assist;

import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.input.DialogInputExtKt;
import com.google.android.material.appbar.MaterialToolbar;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.assist.AssistManager;
import cn.sun45.warbanner.document.preference.SetupPreference;
import cn.sun45.warbanner.framework.ui.BaseActivity;
import cn.sun45.warbanner.framework.ui.BaseFragment;
import cn.sun45.warbanner.util.Utils;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;

/**
 * Created by Sun45 on 2022/2/26
 * 自动连点Fragment
 */
public class AutoClickFragment extends BaseFragment {
    private static final String TAG = "AutoClickFragment";
    private CardView mEnableLay;
    private TextView mEnable;

    private CardView mTapintervalLay;
    private TextView mTapinterval;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_autoclick;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        MaterialToolbar toolbar = mRoot.findViewById(R.id.drop_toolbar);
        ((BaseActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigateUp();
            }
        });
        mEnableLay = mRoot.findViewById(R.id.enable_lay);
        mEnable = mRoot.findViewById(R.id.enable);
        mTapintervalLay = mRoot.findViewById(R.id.tapinterval_lay);
        mTapinterval = mRoot.findViewById(R.id.tapinterval);

        showAutoClick();
        showTapinterval();
        mEnableLay.setOnClickListener(v -> {
            new SetupPreference().setAutoclick(!new SetupPreference().isAutoclick());
            showAutoClick();
        });
        mTapintervalLay.setOnClickListener(v -> {
            MaterialDialog dialog = new MaterialDialog(getContext(), MaterialDialog.getDEFAULT_BEHAVIOR());
            dialog.title(R.string.auto_click_tapinterval_dialog_title, null);
            DialogInputExtKt.input(dialog, null, R.string.auto_click_tapinterval_dialog_hint, String.valueOf(new SetupPreference().getTapinterval()), null, InputType.TYPE_CLASS_NUMBER, null, true, false, new Function2<MaterialDialog, CharSequence, Unit>() {
                @Override
                public Unit invoke(MaterialDialog materialDialog, CharSequence charSequence) {
                    try {
                        int number = Integer.valueOf(charSequence.toString());
                        if (number > 0) {
                            new SetupPreference().setTapinterval(number);
                            AssistManager.getInstance().setTapInterval(number);
                            showTapinterval();
                        }
                    } catch (NumberFormatException e) {
                        String errorinfo = Utils.printError(e);
                        Log.e(TAG, errorinfo);
                    }
                    return null;
                }
            });
            dialog.positiveButton(R.string.auto_click_tapinterval_dialog_confirm, null, null);
            dialog.show();
        });
    }

    private void showAutoClick() {
        SpannableStringBuilder builder = new SpannableStringBuilder(Utils.getString(R.string.auto_click_enable) + " - ");
        String granted;
        int color;
        if (new SetupPreference().isAutoclick()) {
            granted = Utils.getString(R.string.auto_click_enable_on);
            color = Utils.getColor(R.color.green_dark);
        } else {
            granted = Utils.getString(R.string.auto_click_enable_off);
            color = Utils.getColor(R.color.red_dark);
        }
        int start = builder.length();
        int end = start + granted.length();
        builder.append(granted);
        builder.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mEnable.setText(builder, TextView.BufferType.SPANNABLE);
    }

    private void showTapinterval() {
        mTapinterval.setText(Utils.getString(R.string.auto_click_tapinterval) + " - " + new SetupPreference().getTapinterval());
    }

    @Override
    protected void dataRequest() {

    }

    @Override
    protected void onShow() {
    }

    @Override
    protected void onHide() {

    }
}
