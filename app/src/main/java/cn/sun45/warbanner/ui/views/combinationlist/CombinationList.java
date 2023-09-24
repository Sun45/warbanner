package cn.sun45.warbanner.ui.views.combinationlist;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import cn.sun45.warbanner.document.database.source.models.CharacterModel;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2023/9/23
 * 套餐列表
 */
public class CombinationList extends RecyclerView {
    private static final String TAG = "CombinationList";
    private LinearLayoutManager layoutManager;

    private CombinationListAdapter adapter;
    public CombinationListListener listener;

    public CombinationList(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setVerticalScrollBarEnabled(false);
        layoutManager = new LinearLayoutManager(getContext());
        setLayoutManager(layoutManager);
        adapter = new CombinationListAdapter(getContext());
        setAdapter(adapter);
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Utils.logD(TAG, "onScrollStateChanged newState:" + newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Utils.logD(TAG, "onScrolled dx:" + dx + " dy:" + dy);
                CombinationList.this.onScrolled();
            }
        });
    }

    public void setListener(CombinationListListener listener) {
        adapter.setListener(listener);
        this.listener = listener;
    }

    private void onScrolled() {
        int first = layoutManager.findFirstVisibleItemPosition();
        int last = layoutManager.findLastVisibleItemPosition();
        listener.onScrolled(first, last);
    }

    private void dataNotify() {
        if (adapter.getItemCount() > 0) {
            adapter.notifyDataSetChanged();
            if (listener != null) {
                listener.dataSet(adapter.getItemCount(), adapter.getListSelectItemList());
            }
            onScrolled();
        }
    }

    public void setCharacterModels(List<CharacterModel> characterModels) {
        adapter.setCharacterModels(characterModels);
    }

    public void setData(List<CombinationListModel> list) {
        scrollToPosition(0);
        adapter.setList(list);
        dataNotify();
    }
}
