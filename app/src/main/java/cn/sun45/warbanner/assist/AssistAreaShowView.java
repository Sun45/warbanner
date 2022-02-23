package cn.sun45.warbanner.assist;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.widget.TextView;

import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2022/2/23
 * 辅助区域展示控件
 */
public class AssistAreaShowView extends AssistView {
    public AssistAreaShowView(Context context) {
        super(context);
    }

    @Override
    protected View buildView(Context context) {
        return new AreaShowView(context);
    }

    @Override
    public int getWidth() {
        return Utils.getScreenSize()[0];
    }

    @Override
    public int getHeight() {
        return Utils.getScreenSize()[1];
    }

    public class AreaShowView extends TextView {
        private Paint paint;

        public AreaShowView(Context context) {
            super(context);
            paint = new Paint();
            paint.setAntiAlias(true);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            paint.setColor(0xaaFFF9C4);
            AssistDataModel assistDataModel = AssistManager.getInstance().getDataModel();
            drawArea(canvas, assistDataModel.getCharacterOne(), paint);
            drawArea(canvas, assistDataModel.getCharacterTwo(), paint);
            drawArea(canvas, assistDataModel.getCharacterThree(), paint);
            drawArea(canvas, assistDataModel.getCharacterFour(), paint);
            drawArea(canvas, assistDataModel.getCharacterFive(), paint);
        }

        private void drawArea(Canvas canvas, Rect rect, Paint paint) {
            canvas.drawRect(rect, paint);
        }
    }
}
