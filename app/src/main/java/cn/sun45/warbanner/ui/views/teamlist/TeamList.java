package cn.sun45.warbanner.ui.views.teamlist;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.document.database.setup.models.ScreenCharacterModel;
import cn.sun45.warbanner.document.database.setup.models.TeamCustomizeModel;
import cn.sun45.warbanner.document.database.source.models.BossModel;
import cn.sun45.warbanner.document.database.source.models.CharacterModel;
import cn.sun45.warbanner.document.database.source.models.TeamModel;
import cn.sun45.warbanner.document.statics.StaticHelper;
import cn.sun45.warbanner.document.statics.TeamType;
import cn.sun45.warbanner.stage.StageManager;
import cn.sun45.warbanner.ui.views.teamgrouplist.TeamGroupListModel;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2021/5/20
 * 阵容列表
 */
public class TeamList extends RecyclerView {
    private static final String TAG = "TeamList";
    private LinearLayoutManager layoutManager;
    private TeamListAdapter adapter;
    public TeamListListener listener;

    public TeamList(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setVerticalScrollBarEnabled(false);
        layoutManager = new LinearLayoutManager(getContext());
        setLayoutManager(layoutManager);
        adapter = new TeamListAdapter(getContext());
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
                TeamList.this.onScrolled();
            }
        });
    }

    public void setListener(TeamListListener listener) {
        adapter.setListener(listener);
        this.listener = listener;
    }

    private void onScrolled() {
        int first = layoutManager.findFirstVisibleItemPosition();
        int last = layoutManager.findLastVisibleItemPosition();
        listener.onScrolled(first, last);
    }

    private void dataNotify() {
        adapter.notifyDataSetChanged();
        if (listener != null) {
            listener.dataSet(adapter.getItemCount(), adapter.getListSelectItemList());
        }
        onScrolled();
    }

    public void setData(TeamGroupListModel teamGroupListModel, List<CharacterModel> characterModels) {
        List<TeamListTeamModel> list = new ArrayList<>();
        list.add(new TeamListTeamModel(teamGroupListModel.getTeamone(), teamGroupListModel.getBorrowindexone()));
        list.add(new TeamListTeamModel(teamGroupListModel.getTeamtwo(), teamGroupListModel.getBorrowindextwo()));
        list.add(new TeamListTeamModel(teamGroupListModel.getTeamthree(), teamGroupListModel.getBorrowindexthree()));
        adapter.setList(list);
        adapter.setCharacterModels(characterModels);
        adapter.setShowlink(true);
        dataNotify();
    }

    public void setData(List<TeamModel> teamModelList, List<BossModel> bossModelList, List<CharacterModel> characterModels,
                        boolean showlink, int stageSelection, int bossSelection, int typeSelection,
                        boolean screenFunction, List<ScreenCharacterModel> screenCharacterModels) {
        List<TeamListBossModel> teamWithBossList = new ArrayList<>();
        for (int stagePosition = 0; stagePosition < StageManager.getInstance().getStageCount(); stagePosition++) {
            for (int boss = 1; boss <= StaticHelper.BOSS_COUNT; boss++) {
                String name = bossModelList.get(boss - 1).getName();
                String iconUrl = bossModelList.get(boss - 1).getIconUrl();
                teamWithBossList.add(fillBossModel(new TeamListBossModel(name, iconUrl), teamModelList, stagePosition, boss));
            }
        }
        adapter.setTeamWithBossList(teamWithBossList);
        adapter.setCharacterModels(characterModels);
        adapter.setShowlink(showlink);
        adapter.setStageSelection(stageSelection);
        adapter.setBossSelection(bossSelection);
        adapter.setTypeSelection(typeSelection);
        adapter.setScreenfunction(screenFunction);
        adapter.setScreenCharacterModels(screenCharacterModels);
        dataNotify();
    }

    public TeamListBossModel fillBossModel(TeamListBossModel teamListBossModel, List<TeamModel> list, int stagePosition, int boss) {
        List<TeamListTeamModel> teamModels = new ArrayList<>();
        for (TeamModel teamModel : list) {
            if (StageManager.getInstance().matchTeamModel(teamModel, stagePosition) && teamModel.getBoss().substring(1, 2).equals(boss + "") && teamModel.getType() == TeamType.AUTO) {
                teamModels.add(new TeamListTeamModel(teamModel));
            }
        }
        for (TeamModel teamModel : list) {
            if (StageManager.getInstance().matchTeamModel(teamModel, stagePosition) && teamModel.getBoss().substring(1, 2).equals(boss + "") && teamModel.getType() == TeamType.NORMAL) {
                teamModels.add(new TeamListTeamModel(teamModel));
            }
        }
        for (TeamModel teamModel : list) {
            if (StageManager.getInstance().matchTeamModel(teamModel, stagePosition) && teamModel.getBoss().substring(1, 2).equals(boss + "") && teamModel.getType() == TeamType.FINISH) {
                teamModels.add(new TeamListTeamModel(teamModel));
            }
        }
        teamListBossModel.setTeamModels(teamModels);
        return teamListBossModel;
    }

    public void notifyShowLink(boolean showlink) {
        adapter.setShowlink(showlink);
        dataNotify();
    }

    public void notifyStageSelect(int stageSelection) {
        scrollToPosition(0);
        adapter.setStageSelection(stageSelection);
        dataNotify();
    }

    public void notifyBossSelect(int bossSelection) {
        adapter.setBossSelection(bossSelection);
        dataNotify();
    }

    public void notifyTypeSelect(int typeSelection) {
        adapter.setTypeSelection(typeSelection);
        dataNotify();
    }

    public void notifyCustomize(List<TeamCustomizeModel> teamCustomizeModels) {
        adapter.setTeamCustomizeModels(teamCustomizeModels);
        dataNotify();
    }
}
