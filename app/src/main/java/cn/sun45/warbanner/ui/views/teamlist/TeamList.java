package cn.sun45.warbanner.ui.views.teamlist;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.document.StaticHelper;
import cn.sun45.warbanner.document.db.clanwar.TeamCustomizeModel;
import cn.sun45.warbanner.document.db.clanwar.TeamModel;
import cn.sun45.warbanner.document.db.setup.ScreenCharacterModel;
import cn.sun45.warbanner.document.db.source.CharacterModel;
import cn.sun45.warbanner.document.db.source.ClanWarModel;
import cn.sun45.warbanner.ui.views.teamgrouplist.TeamGroupListModel;

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

    public void setListener(TeamListListener listener) {
        adapter.setListener(listener);
    }

    public void setData(TeamGroupListModel teamGroupListModel) {
        List<TeamListTeamModel> list = new ArrayList<>();
        list.add(new TeamListTeamModel(teamGroupListModel.getTeamone(), teamGroupListModel.getBorrowindexone()));
        list.add(new TeamListTeamModel(teamGroupListModel.getTeamtwo(), teamGroupListModel.getBorrowindextwo()));
        list.add(new TeamListTeamModel(teamGroupListModel.getTeamthree(), teamGroupListModel.getBorrowindexthree()));
        adapter.setList(list);
        adapter.setShowlink(true);
        adapter.notifyDataSetChanged();
    }

    public void setData(List<TeamModel> list, ClanWarModel clanWarModel, boolean showlink, int stageSelection, int bossSelection, int typeSelection) {
        List<TeamListBossModel> teamWithBossList = new ArrayList<>();
        for (int stage = 1; stage <= StaticHelper.STAGE_COUNT; stage++) {
            for (int boss = 1; boss <= StaticHelper.BOSS_COUNT; boss++) {
                String name = null;
                String iconUrl = null;
                switch (boss) {
                    case 1:
                        name = clanWarModel.getBossonename();
                        iconUrl = clanWarModel.getBossoneiconurl();
                        break;
                    case 2:
                        name = clanWarModel.getBosstwoname();
                        iconUrl = clanWarModel.getBosstwoiconurl();
                        break;
                    case 3:
                        name = clanWarModel.getBossthreename();
                        iconUrl = clanWarModel.getBossthreeiconurl();
                        break;
                    case 4:
                        name = clanWarModel.getBossfourname();
                        iconUrl = clanWarModel.getBossfouriconurl();
                        break;
                    case 5:
                        name = clanWarModel.getBossfivename();
                        iconUrl = clanWarModel.getBossfiveiconurl();
                        break;
                }
                teamWithBossList.add(fillBossModel(new TeamListBossModel(stage, boss, name, iconUrl), list, stage, boss));
            }
        }
        adapter.setTeamWithBossList(teamWithBossList);
        adapter.setShowlink(showlink);
        adapter.setStageSelection(stageSelection);
        adapter.setBossSelection(bossSelection);
        adapter.setTypeSelection(typeSelection);
        adapter.notifyDataSetChanged();
    }

    public TeamListBossModel fillBossModel(TeamListBossModel teamListBossModel, List<TeamModel> list, int stage, int boss) {
        List<TeamListTeamModel> teamModels = new ArrayList<>();
        for (TeamModel teamModel : list) {
            if (teamModel.getStage() == stage && teamModel.getBoss().substring(1, 2).equals(boss + "") && teamModel.getType() == 1) {
                teamModels.add(new TeamListTeamModel(teamModel));
            }
        }
        for (TeamModel teamModel : list) {
            if (teamModel.getStage() == stage && teamModel.getBoss().substring(1, 2).equals(boss + "") && teamModel.getType() == 2) {
                teamModels.add(new TeamListTeamModel(teamModel));
            }
        }
        for (TeamModel teamModel : list) {
            if (teamModel.getStage() == stage && teamModel.getBoss().substring(1, 2).equals(boss + "") && teamModel.getType() == 3) {
                teamModels.add(new TeamListTeamModel(teamModel));
            }
        }
        teamListBossModel.setTeamModels(teamModels);
        return teamListBossModel;
    }

    public void notifyShowLink(boolean showlink) {
        adapter.setShowlink(showlink);
        adapter.notifyDataSetChanged();
    }

    public void notifyStageSelect(int stageSelection) {
        scrollToPosition(0);
        adapter.setStageSelection(stageSelection);
        adapter.notifyDataSetChanged();
    }

    public void notifyBossSelect(int bossSelection) {
        adapter.setBossSelection(bossSelection);
        adapter.notifyDataSetChanged();
    }

    public void notifyTypeSelect(int typeSelection) {
        adapter.setTypeSelection(typeSelection);
        adapter.notifyDataSetChanged();
    }

    public void notifyCustomize(List<TeamCustomizeModel> teamCustomizeModels) {
        adapter.setTeamCustomizeModels(teamCustomizeModels);
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
