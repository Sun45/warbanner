package cn.sun45.warbanner.assist;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.widget.TextView;

/**
 * Created by Sun45 on 2022/2/20
 * 区域展示控件
 */
public class AreaShowView extends TextView {
    private Paint paint;

    private AreaDataModel mAreaDataModel;

    public AreaShowView(Context context, AreaDataModel areaDataModel) {
        super(context);
        this.mAreaDataModel = areaDataModel;
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(0xaaFFF9C4);
        drawArea(canvas, mAreaDataModel.getCharacterOne(), paint);
        drawArea(canvas, mAreaDataModel.getCharacterTwo(), paint);
        drawArea(canvas, mAreaDataModel.getCharacterThree(), paint);
        drawArea(canvas, mAreaDataModel.getCharacterFour(), paint);
        drawArea(canvas, mAreaDataModel.getCharacterFive(), paint);
    }

    private void drawArea(Canvas canvas, Rect rect, Paint paint) {
        canvas.drawRect(rect, paint);
    }
}
