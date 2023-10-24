package cn.sun45.warbanner.framework.ui;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Sun45 on 2023/10/22
 */
public class BaseVerticalRecyclerView extends RecyclerView {
    public BaseVerticalRecyclerView(@NonNull Context context) {
        super(context);
        init();
    }

    public BaseVerticalRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseVerticalRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setVerticalFadingEdgeEnabled(true);
    }
}
