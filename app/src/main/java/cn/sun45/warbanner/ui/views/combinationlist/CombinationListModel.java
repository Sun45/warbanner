package cn.sun45.warbanner.ui.views.combinationlist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.combination.CombinationElementModel;
import cn.sun45.warbanner.document.database.source.models.TeamModel;

/**
 * Created by Sun45 on 2023/9/23
 * 套餐列表数据模型
 */
public class CombinationListModel implements Serializable {
    private TeamModel teamone;
    private int returntimeone;
    private List<TeamModel.TimeLine> timelinesone;
    private List<Integer> idlistone;
    private int borrowindexone;
    private TeamModel teamtwo;
    private int returntimetwo;
    private List<TeamModel.TimeLine> timelinestwo;
    private List<Integer> idlisttwo;
    private int borrowindextwo;
    private TeamModel teamthree;
    private int returntimethree;
    private List<TeamModel.TimeLine> timelinesthree;
    private List<Integer> idlistthree;
    private int borrowindexthree;
    private TeamModel teamfour;
    private int returntimefour;
    private List<TeamModel.TimeLine> timelinesfour;
    private List<Integer> idlistfour;
    private int borrowindexfour;
    private TeamModel teamfive;
    private int returntimefive;
    private List<TeamModel.TimeLine> timelinesfive;
    private List<Integer> idlistfive;
    private int borrowindexfive;
    private TeamModel teamsix;
    private int returntimesix;
    private List<TeamModel.TimeLine> timelinessix;
    private List<Integer> idlistsix;
    private int borrowindexsix;

    public CombinationListModel(
            CombinationElementModel elementone, CombinationElementModel elementtwo, CombinationElementModel elementthree,
            CombinationElementModel elementfour, CombinationElementModel elementfive, CombinationElementModel elementsix
            , int[] borrowIds) {
        teamone = elementone.getTeamModel();
        returntimeone = elementone.getReturnTime();
        timelinesone = elementone.getTimeLines();
        idlistone = elementone.getIdlist();
        borrowindexone = idlistone.indexOf((Object) borrowIds[0]);
        teamtwo = elementtwo.getTeamModel();
        returntimetwo = elementtwo.getReturnTime();
        timelinestwo = elementtwo.getTimeLines();
        idlisttwo = elementtwo.getIdlist();
        borrowindextwo = idlisttwo.indexOf((Object) borrowIds[1]);
        teamthree = elementthree.getTeamModel();
        returntimethree = elementthree.getReturnTime();
        timelinesthree = elementthree.getTimeLines();
        idlistthree = elementthree.getIdlist();
        borrowindexthree = idlistthree.indexOf((Object) borrowIds[2]);
        teamfour = elementfour.getTeamModel();
        returntimefour = elementfour.getReturnTime();
        timelinesfour = elementfour.getTimeLines();
        idlistfour = elementfour.getIdlist();
        borrowindexfour = idlistfour.indexOf((Object) borrowIds[3]);
        teamfive = elementfive.getTeamModel();
        returntimefive = elementfive.getReturnTime();
        timelinesfive = elementfive.getTimeLines();
        idlistfive = elementfive.getIdlist();
        borrowindexfive = idlistfive.indexOf((Object) borrowIds[4]);
        if (elementsix != null) {
            teamsix = elementsix.getTeamModel();
            returntimesix = elementsix.getReturnTime();
            timelinessix = elementsix.getTimeLines();
            idlistsix = elementsix.getIdlist();
            borrowindexsix = idlistsix.indexOf((Object) borrowIds[5]);
        }
    }

    public ArrayList<Integer> getUsedCharacterList() {
        ArrayList<Integer> usedCharacterList = new ArrayList<>();
        if (idlistone != null) {
            for (Integer id : idlistone) {
                if (!usedCharacterList.contains(id)) {
                    usedCharacterList.add(id);
                }
            }
        }
        if (idlisttwo != null) {
            for (Integer id : idlisttwo) {
                if (!usedCharacterList.contains(id)) {
                    usedCharacterList.add(id);
                }
            }
        }
        if (idlistthree != null) {
            for (Integer id : idlistthree) {
                if (!usedCharacterList.contains(id)) {
                    usedCharacterList.add(id);
                }
            }
        }
        if (idlistfour != null) {
            for (Integer id : idlistfour) {
                if (!usedCharacterList.contains(id)) {
                    usedCharacterList.add(id);
                }
            }
        }
        if (idlistfive != null) {
            for (Integer id : idlistfive) {
                if (!usedCharacterList.contains(id)) {
                    usedCharacterList.add(id);
                }
            }
        }
        if (idlistsix != null) {
            for (Integer id : idlistsix) {
                if (!usedCharacterList.contains(id)) {
                    usedCharacterList.add(id);
                }
            }
        }
        return usedCharacterList;
    }

    public TeamModel getTeamone() {
        return teamone;
    }

    public int getReturntimeone() {
        return returntimeone;
    }

    public List<TeamModel.TimeLine> getTimelinesone() {
        return timelinesone;
    }

    public List<Integer> getIdlistone() {
        return idlistone;
    }

    public int getBorrowindexone() {
        return borrowindexone;
    }

    public TeamModel getTeamtwo() {
        return teamtwo;
    }

    public int getReturntimetwo() {
        return returntimetwo;
    }

    public List<TeamModel.TimeLine> getTimelinestwo() {
        return timelinestwo;
    }

    public List<Integer> getIdlisttwo() {
        return idlisttwo;
    }

    public int getBorrowindextwo() {
        return borrowindextwo;
    }

    public TeamModel getTeamthree() {
        return teamthree;
    }

    public int getReturntimethree() {
        return returntimethree;
    }

    public List<TeamModel.TimeLine> getTimelinesthree() {
        return timelinesthree;
    }

    public List<Integer> getIdlistthree() {
        return idlistthree;
    }

    public int getBorrowindexthree() {
        return borrowindexthree;
    }

    public TeamModel getTeamfour() {
        return teamfour;
    }

    public int getReturntimefour() {
        return returntimefour;
    }

    public List<TeamModel.TimeLine> getTimelinesfour() {
        return timelinesfour;
    }

    public List<Integer> getIdlistfour() {
        return idlistfour;
    }

    public int getBorrowindexfour() {
        return borrowindexfour;
    }

    public TeamModel getTeamfive() {
        return teamfive;
    }

    public int getReturntimefive() {
        return returntimefive;
    }

    public List<TeamModel.TimeLine> getTimelinesfive() {
        return timelinesfive;
    }

    public List<Integer> getIdlistfive() {
        return idlistfive;
    }

    public int getBorrowindexfive() {
        return borrowindexfive;
    }

    public TeamModel getTeamsix() {
        return teamsix;
    }

    public int getReturntimesix() {
        return returntimesix;
    }

    public List<TeamModel.TimeLine> getTimelinessix() {
        return timelinessix;
    }

    public List<Integer> getIdlistsix() {
        return idlistsix;
    }

    public int getBorrowindexsix() {
        return borrowindexsix;
    }
}
