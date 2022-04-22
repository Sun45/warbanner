package cn.sun45.warbanner.ui.views.portraittextview;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

/**
 * Created by Sun45 on 2022/4/2
 * 纵向展示文本框
 */
public class PortraitTextView extends TextView {
    public PortraitTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        String text = getText().toString();
        if (!TextUtils.isEmpty(text)) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < text.length(); i++) {
                if (i != 0) {
                    sb.append("\n ");
                } else {
                    sb.append(" ");
                }
                sb.append(text.charAt(i));
                sb.append(" ");
            }
            setText(sb.toString());
        }
    }
}
