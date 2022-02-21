package cn.sun45.warbanner.ui.views.teamlist;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.document.db.clanwar.TeamCustomizeModel;
import cn.sun45.warbanner.document.db.clanwar.TeamModel;
import cn.sun45.warbanner.document.db.setup.ScreenCharacterModel;
import cn.sun45.warbanner.document.db.source.CharacterModel;
import cn.sun45.warbanner.document.db.source.ClanWarModel;
import cn.sun45.warbanner.ui.views.teamgrouplist.TeamGroupListListener;
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

    public void setData(List<TeamModel> list, ClanWarModel clanWarModel, boolean showlink, int autoScreen, int showtype) {
        List<TeamListTeamModel> teamModelOnelist = new ArrayList<>();
        List<TeamListTeamModel> teamModelTwolist = new ArrayList<>();
        List<TeamListTeamModel> teamModelThreelist = new ArrayList<>();
        List<TeamListTeamModel> teamModelFourlist = new ArrayList<>();
        for (TeamModel teamModel : list) {
            switch (teamModel.getStage()) {
                case 1:
                    teamModelOnelist.add(new TeamListTeamModel(teamModel));
                    break;
                case 2:
                    teamModelTwolist.add(new TeamListTeamModel(teamModel));
                    break;
                case 3:
                    teamModelThreelist.add(new TeamListTeamModel(teamModel));
                    break;
                case 4:
                    teamModelFourlist.add(new TeamListTeamModel(teamModel));
                    break;
                default:
                    break;
            }
        }
        adapter.setList(
                fillList(new ArrayList<>(), clanWarModel, teamModelOnelist),
                fillList(new ArrayList<>(), clanWarModel, teamModelTwolist),
                fillList(new ArrayList<>(), clanWarModel, teamModelThreelist),
                fillList(new ArrayList<>(), clanWarModel, teamModelFourlist));
        adapter.setShowlink(showlink);
        adapter.setAutoScreen(autoScreen);
        adapter.setShowtype(showtype);
        adapter.notifyDataSetChanged();
    }

    private List<Object> fillList(List<Object> list, ClanWarModel clanWarModel, List<TeamListTeamModel> teamModellist) {
        if (clanWarModel != null) {
            list.add(new TeamListBossModel(clanWarModel.getBossonename(), clanWarModel.getBossoneiconurl()));
        }
        for (TeamListTeamModel teamListTeamModel : teamModellist) {
            String boss = teamListTeamModel.getTeamModel().getBoss().substring(1, 2);
            boolean auto = teamListTeamModel.getTeamModel().isAuto();
            if (boss.equals("1") && !auto) {
                list.add(teamListTeamModel);
            }
        }
        for (TeamListTeamModel teamListTeamModel : teamModellist) {
            String boss = teamListTeamModel.getTeamModel().getBoss().substring(1, 2);
            boolean auto = teamListTeamModel.getTeamModel().isAuto();
            if (boss.equals("1") && auto) {
                list.add(teamListTeamModel);
            }
        }
        if (clanWarModel != null) {
            list.add(new TeamListBossModel(clanWarModel.getBosstwoname(), clanWarModel.getBosstwoiconurl()));
        }
        for (TeamListTeamModel teamListTeamModel : teamModellist) {
            String boss = teamListTeamModel.getTeamModel().getBoss().substring(1, 2);
            boolean auto = teamListTeamModel.getTeamModel().isAuto();
            if (boss.equals("2") && !auto) {
                list.add(teamListTeamModel);
            }
        }
        for (TeamListTeamModel teamListTeamModel : teamModellist) {
            String boss = teamListTeamModel.getTeamModel().getBoss().substring(1, 2);
            boolean auto = teamListTeamModel.getTeamModel().isAuto();
            if (boss.equals("2") && auto) {
                list.add(teamListTeamModel);
            }
        }
        if (clanWarModel != null) {
            list.add(new TeamListBossModel(clanWarModel.getBossthreename(), clanWarModel.getBossthreeiconurl()));
        }
        for (TeamListTeamModel teamListTeamModel : teamModellist) {
            String boss = teamListTeamModel.getTeamModel().getBoss().substring(1, 2);
            boolean auto = teamListTeamModel.getTeamModel().isAuto();
            if (boss.equals("3") && !auto) {
                list.add(teamListTeamModel);
            }
        }
        for (TeamListTeamModel teamListTeamModel : teamModellist) {
            String boss = teamListTeamModel.getTeamModel().getBoss().substring(1, 2);
            boolean auto = teamListTeamModel.getTeamModel().isAuto();
            if (boss.equals("3") && auto) {
                list.add(teamListTeamModel);
            }
        }
        if (clanWarModel != null) {
            list.add(new TeamListBossModel(clanWarModel.getBossfourname(), clanWarModel.getBossfouriconurl()));
        }
        for (TeamListTeamModel teamListTeamModel : teamModellist) {
            String boss = teamListTeamModel.getTeamModel().getBoss().substring(1, 2);
            boolean auto = teamListTeamModel.getTeamModel().isAuto();
            if (boss.equals("4") && !auto) {
                list.add(teamListTeamModel);
            }
        }
        for (TeamListTeamModel teamListTeamModel : teamModellist) {
            String boss = teamListTeamModel.getTeamModel().getBoss().substring(1, 2);
            boolean auto = teamListTeamModel.getTeamModel().isAuto();
            if (boss.equals("4") && auto) {
                list.add(teamListTeamModel);
            }
        }
        if (clanWarModel != null) {
            list.add(new TeamListBossModel(clanWarModel.getBossfivename(), clanWarModel.getBossfiveiconurl()));
        }
        for (TeamListTeamModel teamListTeamModel : teamModellist) {
            String boss = teamListTeamModel.getTeamModel().getBoss().substring(1, 2);
            boolean auto = teamListTeamModel.getTeamModel().isAuto();
            if (boss.equals("5") && !auto) {
                list.add(teamListTeamModel);
            }
        }
        for (TeamListTeamModel teamListTeamModel : teamModellist) {
            String boss = teamListTeamModel.getTeamModel().getBoss().substring(1, 2);
            boolean auto = teamListTeamModel.getTeamModel().isAuto();
            if (boss.equals("5") && auto) {
                list.add(teamListTeamModel);
            }
        }
        return list;
    }

    public void notifyShowLink(boolean showlink) {
        adapter.setShowlink(showlink);
        adapter.notifyDataSetChanged();
    }

    public void notifyAutoScreen(int autoScreen) {
        adapter.setAutoScreen(autoScreen);
        adapter.notifyDataSetChanged();
    }

    public void notifyShowtype(int showtype) {
        scrollToPosition(0);
        adapter.setShowtype(showtype);
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
