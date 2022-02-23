package cn.sun45.warbanner.assist;

import android.content.Context;
import android.view.View;

/**
 * Created by Sun45 on 2022/2/23
 * 辅助控件
 */
public abstract class AssistView {
    private View mRoot;

    public AssistView(Context context) {
        mRoot = buildView(context);
    }

    //构建展示内容
    protected abstract View buildView(Context context);

    //获取展示内容
    public final View getView() {
        return mRoot;
    }

    //获取宽
    public abstract int getWidth();

    //获取高
    public abstract int getHeight();
}
