package cn.sun45.warbanner.ui.views.teamlist;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.sun45.warbanner.document.db.clanwar.TeamModel;
import cn.sun45.warbanner.document.db.setup.ScreenCharacterModel;
import cn.sun45.warbanner.document.db.source.CharacterModel;

/**
 * Created by Sun45 on 2021/5/20
 * 阵容列表
 */
public class TeamList extends RecyclerView {

    private TeamListAdapter adapter;

    public TeamList(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TeamListAdapter(getContext());
        setAdapter(adapter);
    }

    public void setData(List<TeamModel> list) {
        adapter.setList(list);
        adapter.notifyDataSetChanged();
    }

    public void setData(List<TeamModel> list, boolean showlink, int showtype) {
        adapter.setList(list);
        adapter.setShowlink(showlink);
        adapter.setShowtype(showtype);
        adapter.notifyDataSetChanged();
    }

    public void notifyShowLink(boolean showlink) {
        adapter.setShowlink(showlink);
        adapter.notifyDataSetChanged();
    }

    public void notifyShowtype(int showtype) {
        scrollToPosition(0);
        adapter.setShowtype(showtype);
        adapter.notifyDataSetChanged();
    }

    public void notifyCharacter(List<CharacterModel> characterModels) {
        adapter.setCharacterModels(characterModels);
        adapter.notifyDataSetChanged();
    }

    public void setScreenFunction(boolean screenFunction) {
        adapter.setScreenfunction(screenFunction);
        adapter.notifyDataSetChanged();
    }

    public void notifyScreenCharacter(boolean screenFunction, List<ScreenCharacterModel> screenCharacterModels) {
        adapter.setScreenfunction(screenFunction);
        adapter.setScreenCharacterModels(screenCharacterModels);
        adapter.notifyDataSetChanged();
    }
}
