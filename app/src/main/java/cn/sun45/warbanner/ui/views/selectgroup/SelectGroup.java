package cn.sun45.warbanner.ui.views.selectgroup;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2021/7/27
 * 分组选项
 */
public class SelectGroup extends LinearLayout {
    private List<Holder> holders;

    private SelectGroupListener listener;

    private int selectItem;

    public SelectGroup(Context context) {
        super(context);
        init();
    }

    public SelectGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOrientation(LinearLayout.HORIZONTAL);
        setPadding(Utils.dip2px(getContext(), 5), Utils.dip2px(getContext(), 5), Utils.dip2px(getContext(), 5), Utils.dip2px(getContext(), 5));
    }

    public void setListener(SelectGroupListener listener) {
        this.listener = listener;
    }

    public void setData(List<String> selections, int position) {
        removeAllViews();
        holders = new ArrayList<>();
        for (int i = 0; i < selections.size(); i++) {
            String selection = selections.get(i);
            int bg = 0;
            if (i == 0) {
                bg = R.drawable.selectgroup_item_left_bg;
            } else if (i == selections.size() - 1) {
                bg = R.drawable.selectgroup_item_right_bg;
            } else {
                bg = R.drawable.selectgroup_item_center_bg;
            }
            Holder holder = new Holder(i, selection, i != 0, bg, i == position);
            View v = holder.getText();
            addView(v);
            holders.add(holder);
        }
    }

    public int getSelectItem() {
        return selectItem;
    }

    private class Holder {
        private int position;
        private String selection;

        private TextView mText;

        private boolean select;

        public Holder(int position, String selection, boolean margin, int bg, boolean select) {
            this.position = position;
            this.selection = selection;
            mText = new TextView(getContext());
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, Utils.dip2px(getContext(), 30));
            if (margin) {
                layoutParams.setMargins(Utils.dip2px(getContext(), 5), 0, 0, 0);
            }
            mText.setLayoutParams(layoutParams);
            mText.setPadding(Utils.dip2px(getContext(), 5), 0, Utils.dip2px(getContext(), 5), 0);
            mText.setBackgroundResource(bg);
            mText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            mText.setGravity(Gravity.CENTER);
            mText.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Holder.this.select) {
                        selectItem = position;
                        if (listener != null) {
                            listener.select(position);
                        }
                        for (int i = 0; i < holders.size(); i++) {
                            Holder holder = holders.get(i);
                            holder.select(i == Holder.this.position);
                        }
                    }
                }
            });
            select(select);
        }

        public TextView getText() {
            return mText;
        }

        public void select(boolean select) {
            this.select = select;
            if (select) {
                String str = selection;
                SpannableStringBuilder builder = new SpannableStringBuilder(str);
                builder.setSpan(new ForegroundColorSpan(Utils.getAttrColor(getContext(), R.attr.colorSecondary)), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                mText.setText(builder);
            } else {
                mText.setText(selection);
            }
        }
    }
}