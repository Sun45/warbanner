package cn.sun45.warbanner.ui.views.listselectbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.List;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.framework.image.ImageRequester;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2022/6/20
 * 列表选择器
 */
public class ListSelectBar extends FrameLayout {
    private static final String TAG = "ListSelectBar";
    private int halfsize;
    private int totalheight;

    private ListSelectBarListener listener;

    private int count;

    private List<ListSelectItem> listSelectItems;

    private DotView mDot;

    public ListSelectBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Utils.logD(TAG, "ListSelectBar");
        halfsize = Utils.dip2px(getContext(), 13);
        totalheight = Utils.getScreenSize()[0] * 2 / 3;
        setBackgroundResource(R.drawable.listselectbar_bg);
    }

    public void setListener(ListSelectBarListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(halfsize * 2, totalheight + halfsize * 2);
    }

    public void init(int count, List<ListSelectItem> listSelectItems) {
        Utils.logD(TAG, "init count:" + count);
        if (itemChange(count, listSelectItems)) {
            this.count = count;
            this.listSelectItems = listSelectItems;
            removeAllViews();
            if (listSelectItems != null) {
                for (ListSelectItem listSelectItem : listSelectItems) {
                    addView(new Holder(listSelectItem).getView());
                }
            }
            mDot = new DotView(getContext());
            addView(mDot);
        }
    }

    private boolean itemChange(int count, List<ListSelectItem> listSelectItems) {
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
        private ImageView pic;
        private ListSelectItem listSelectItem;

        public Holder(ListSelectItem listSelectItem) {
            this.listSelectItem = listSelectItem;
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(halfsize * 2, halfsize * 2);
            int margintop = 0;
            if (count == 1) {
                margintop = (int) (totalheight * 1.0f / 2);
            } else {
                margintop = (int) (totalheight * 1.0f / (count - 1) * listSelectItem.getPosition());
            }
            layoutParams.setMargins(0, margintop, 0, 0);
            String picUrl = listSelectItem.getPicUrl();
            int picSrc = listSelectItem.getPicSrc();
            pic = new ImageView(getContext());
            pic.setLayoutParams(layoutParams);
            if (!TextUtils.isEmpty(picUrl)) {
                ImageRequester.request(picUrl, R.drawable.ic_character_default).loadRoundImage(pic);
            } else if (picSrc != 0) {
                pic.setImageResource(picSrc);
            }
        }

        public ImageView getView() {
            return pic;
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
            canvas.drawCircle(halfsize, deviation, dotRadius, paint);
            canvas.drawPoint(halfsize, deviation, paint);
        }
    }
}
