package cn.sun45.warbanner.ui.views.character.characterlist.characterlist;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by Sun45 on 2021/5/30
 * 角色列表
 */
public class CharacterList extends RecyclerView {
    private CharacterListAdapter adapter;

    public CharacterList(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setVerticalScrollBarEnabled(false);
        setLayoutManager(new GridLayoutManager(getContext(), 5));
        adapter = new CharacterListAdapter(getContext());
        setAdapter(adapter);
    }

    public void setListener(CharacterListListener listener) {
        adapter.setListener(listener);
    }

    public void setData(List<CharacterListModel> list) {
        adapter.setList(list);
        adapter.notifyDataSetChanged();
    }
}
