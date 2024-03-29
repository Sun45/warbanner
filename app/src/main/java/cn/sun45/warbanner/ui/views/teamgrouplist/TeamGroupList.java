package cn.sun45.warbanner.ui.views.teamgrouplist;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import cn.sun45.warbanner.document.database.setup.models.TeamCustomizeModel;
import cn.sun45.warbanner.document.database.source.models.CharacterModel;
import cn.sun45.warbanner.framework.ui.BaseVerticalRecyclerView;

/**
 * Created by Sun45 on 2021/5/30
 * 分刀列表
 */
public class TeamGroupList extends BaseVerticalRecyclerView {
    private TeamGroupListAdapter adapter;

    public TeamGroupList(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setVerticalScrollBarEnabled(false);
        setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TeamGroupListAdapter(getContext());
        setAdapter(adapter);
    }

    public void setListener(TeamGroupListListener listener) {
        adapter.setListener(listener);
    }

    public void setCharacterModels(List<CharacterModel> characterModels) {
        adapter.setCharacterModels(characterModels);
    }

    public void setData(List<TeamGroupListModel> list) {
        scrollToPosition(0);
        adapter.setList(list);
        adapter.notifyDataSetChanged();
    }

    public void notifyCustomize(List<TeamCustomizeModel> teamCustomizeModels) {
        adapter.setTeamCustomizeModels(teamCustomizeModels);
        adapter.notifyDataSetChanged();
    }
}
