package cn.sun45.warbanner.ui.views.combinationlist.selectbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.List;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2023/9/24
 * 套餐列表选择器
 */
public class CombinationListSelectBar extends FrameLayout {
    private static final String TAG = "CombinationSelectBar";
    private int halfwidth;
    private int halfsize;
    private int totalheight;

    private CombinationListSelectBarListener listener;

    private int count;

    private List<CombinationListSelectItem> listSelectItems;

    private DotView mDot;

    public CombinationListSelectBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Utils.logD(TAG, "CombinationSelectBar");
        halfwidth = Utils.dip2px(getContext(), 42);
        halfsize = Utils.dip2px(getContext(), 10);
        totalheight = Utils.getScreenSize()[0] * 2 / 3;
    }

    public void setListener(CombinationListSelectBarListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(halfwidth * 2, totalheight + halfsize * 2);
    }

    public void init(int count, List<CombinationListSelectItem> listSelectItems) {
        Utils.logD(TAG, "init count:" + count);
        if (itemChange(count, listSelectItems)) {
            this.count = count;
            this.listSelectItems = listSelectItems;
            removeAllViews();
            View bg = new View(getContext());
            bg.setLayoutParams(new FrameLayout.LayoutParams(halfsize * 2, totalheight, Gravity.CENTER_HORIZONTAL));
            bg.setBackgroundResource(R.drawable.listselectbar_bg);
            addView(bg);
            if (listSelectItems != null) {
                for (CombinationListSelectItem listSelectItem : listSelectItems) {
                    addView(new Holder(listSelectItem).getView());
                }
            }
            mDot = new DotView(getContext());
            addView(mDot);
        }
    }

    private boolean itemChange(int count, List<CombinationListSelectItem> listSelectItems) {
        if (this.count != count) {
            return true;
        }
        if (this.listSelectItems == null && listSelectItems == null) {
            return false;
        }
        if (this.listSelectItems == null || listSelectItems == null) {
            return true;
        }
        if (this.listSelectItems.size() != listSelectItems.size()) {
            return true;
        }
        for (int i = 0; i < listSelectItems.size(); i++) {
            if (!this.listSelectItems.get(i).equals(listSelectItems.get(i))) {
                return true;
            }
        }
        return false;
    }

    public void scroll(int first, int last) {
        Utils.logD(TAG, "scroll first:" + first + " last:" + last);
        if (mDot != null) {
            int center = (first + last) / 2;
            mDot.setCurrentPosition(center);
        }
    }

    private class Holder {
        private TextView text;
        private CombinationListSelectItem listSelectItem;

        public Holder(CombinationListSelectItem listSelectItem) {
            this.listSelectItem = listSelectItem;
            text = new TextView(getContext());
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);
            int margintop = 0;
            if (count == 1) {
                margintop = (int) (totalheight * 1.0f / 2);
            } else {
                margintop = (int) (totalheight * 1.0f / (count - 1) * listSelectItem.getPosition());
            }
            layoutParams.setMargins(0, margintop, 0, 0);
            text.setLayoutParams(layoutParams);
            text.setText(listSelectItem.getText());
        }

        public View getView() {
            return text;
        }
    }

    private class DotView extends View {
        private int dotRadius;
        private int dotStrokeWidth;

        private Paint paint;

        private int currentPosition;

        private boolean seeking;

        public DotView(Context context) {
            super(context);
            setClickable(true);
            dotRadius = Utils.dip2px(getContext(), 5);
            dotStrokeWidth = Utils.dip2px(getContext(), 3);
            paint = new Paint();
            paint.setColor(Utils.getAttrColor(getContext(), R.attr.colorSecondary));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(dotStrokeWidth);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setAntiAlias(true);
        }

        public void setCurrentPosition(int position) {
            if (!seeking) {
                dosetCurrentPosition(position);
            }
        }

        private void dosetCurrentPosition(int position) {
            if (currentPosition != position) {
                currentPosition = position;
                postInvalidate();
            }
        }

        private int calucatePosition(float deviation) {
            int position;
            if (count == 1) {
                position = 1;
            } else {
                position = (int) ((deviation - halfsize) / totalheight * (count - 1));
            }
            if (position < 0) {
                position = 0;
            } else if (position >= count) {
                position = count - 1;
            }
            return position;
        }

        private float calucateDeviation(int position) {
            float deviation;
            if (count == 1) {
                deviation = totalheight * 1.0f / 2;
            } else {
                deviation = totalheight * 1.0f / (count - 1) * position;
            }
            deviation += halfsize;
            return (int) deviation;
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    seeking = true;
                case MotionEvent.ACTION_MOVE:
                    float y = event.getY();
                    int position = calucatePosition(y);
                    dosetCurrentPosition(position);
                    listener.seek(position);
                    break;
                case MotionEvent.ACTION_UP:
                    seeking = false;
                    break;
            }
            return true;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            float deviation = calucateDeviation(currentPosition);
            canvas.drawCircle(halfwidth, deviation, dotRadius, paint);
            canvas.drawPoint(halfwidth, deviation, paint);
        }
    }
}
