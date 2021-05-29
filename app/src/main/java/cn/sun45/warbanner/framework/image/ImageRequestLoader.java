package cn.sun45.warbanner.framework.image;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import java.io.IOException;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2019/10/29.
 * 图片请求加载器
 */
public class ImageRequestLoader {
    private RequestCreator requestCreator;

    public ImageRequestLoader(RequestCreator requestCreator) {
        this.requestCreator = requestCreator;
    }

    /**
     * 设置tag
     *
     * @param tag
     * @return
     */
    public ImageRequestLoader tag(String tag) {
        requestCreator.tag(tag);
        return this;
    }

    /**
     * 预加载
     */
    public void preLoad() {
        preLoad(null);
    }

    /**
     * 预加载
     *
     * @param callback 加载监听
     */
    public void preLoad(Callback callback) {
        requestCreator.fetch(callback);
    }

    /**
     * 加载到图片
     *
     * @param image
     */
    public void loadImage(ImageView image) {
        requestCreator.into(image);
    }

    /**
     * 转换成圆形加载到图片
     *
     * @param image
     */
    public void loadCircleImage(ImageView image) {
        requestCreator.transform(new CircleTransformation());
        requestCreator.into(image);
    }

    /**
     * 加载bitmap对象
     *
     * @param target
     */
    public void loadBitMap(Target target) {
        requestCreator.into(target);
    }

    /**
     * 获取bitmap对象
     *
     * @return
     */
    public Bitmap getBimap() {
        Bitmap bitmap = null;
        try {
            bitmap = requestCreator.get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 圆形转换器
     */
    public class CircleTransformation implements Transformation {
        private static final int STROKE_WIDTH = 5;

        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }
            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());
            Canvas canvas = new Canvas(bitmap);
            Paint avatarPaint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            avatarPaint.setShader(shader);
            Paint outlinePaint = new Paint();
            outlinePaint.setColor(Color.WHITE);
            outlinePaint.setStyle(Paint.Style.STROKE);
            outlinePaint.setStrokeWidth(STROKE_WIDTH);
            outlinePaint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, avatarPaint);
            canvas.drawCircle(r, r, r - STROKE_WIDTH / 2, outlinePaint);
            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
}
