package cn.sun45.warbanner.ui.views.recordlist;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import cn.sun45.warbanner.framework.ui.BaseVerticalRecyclerView;

/**
 * Created by Sun45 on 2021/6/4
 * 纪录列表
 */
public class RecordList extends BaseVerticalRecyclerView {
    private RecordListAdapter adapter;

    public RecordList(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecordListAdapter(getContext());
        setAdapter(adapter);
    }

    public void setListener(RecordListListener listener) {
        adapter.setListener(listener);
    }

    public void setData(List<RecordListModel> list) {
        adapter.setList(list);
        adapter.notifyDataSetChanged();
    }
}
