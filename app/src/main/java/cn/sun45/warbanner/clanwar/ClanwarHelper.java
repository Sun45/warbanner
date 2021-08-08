package cn.sun45.warbanner.clanwar;

import java.util.List;

import cn.sun45.warbanner.document.db.clanwar.TeamCustomizeModel;
import cn.sun45.warbanner.document.db.clanwar.TeamGroupCollectionModel;
import cn.sun45.warbanner.document.db.clanwar.TeamGroupScreenModel;
import cn.sun45.warbanner.document.db.clanwar.TeamModel;
import cn.sun45.warbanner.document.db.source.ClanWarModel;
import cn.sun45.warbanner.framework.MyApplication;
import cn.sun45.warbanner.framework.document.db.DbHelper;
import cn.sun45.warbanner.ui.views.teamgrouplist.TeamGroupListModel;
import cn.sun45.warbanner.user.UserManager;

/**
 * Created by Sun45 on 2021/6/28
 * 会战帮助类
 */
public class ClanwarHelper {
    /**
     * 获取当期会战日期
     *
     * @return
     */
    public static String getCurrentClanWarDate() {
        String date = null;
        ClanWarModel clanWarModel = getCurrentClanWarModel();
        if (clanWarModel != null) {
            date = clanWarModel.getDate();
        }
        return date;
    }

    /**
     * 获取当期会战信息
     *
     * @return
     */
    public static ClanWarModel getCurrentClanWarModel() {
        ClanWarModel model = null;
        List<ClanWarModel> clanWarModelList = DbHelper.query(MyApplication.application, ClanWarModel.class);
        if (clanWarModelList != null && !clanWarModelList.isEmpty()) {
            model = clanWarModelList.get(0);
        }
        return model;
    }

    /**
     * 查找阵容自定义信息
     *
     * @param teamModel
     * @param teamCustomizeModels
     */
    public static TeamCustomizeModel findCustomizeModel(TeamModel teamModel, List<TeamCustomizeModel> teamCustomizeModels) {
        TeamCustomizeModel teamCustomizeModel = null;
        if (teamCustomizeModels != null) {
            for (TeamCustomizeModel model : teamCustomizeModels) {
                if (model.getDate().equals(teamModel.getDate()) && model.getNumber().equals(teamModel.getNumber())) {
                    teamCustomizeModel = model;
                    break;
                }
            }
        }
        return teamCustomizeModel;
    }

    /**
     * 获取阵容自定义信息
     *
     * @param teamModel
     */
    public static TeamCustomizeModel getCustomizeModel(TeamModel teamModel) {
        List<TeamCustomizeModel> teamCustomizeList = DbHelper.query(MyApplication.application, TeamCustomizeModel.class,
                new String[]{"date", "number"}, new String[]{teamModel.getDate(), teamModel.getNumber()});
        TeamCustomizeModel teamCustomizeModel = null;
        if (teamCustomizeList != null && !teamCustomizeList.isEmpty()) {
            teamCustomizeModel = teamCustomizeList.get(0);
        }
        return teamCustomizeModel;
    }

    /**
     * 阵容屏蔽定制
     *
     * @param teamModel
     * @param teamCustomizeModel
     */
    public static TeamCustomizeModel customizeTeamBlock(TeamModel teamModel, TeamCustomizeModel teamCustomizeModel) {
        if (teamCustomizeModel == null) {
            teamCustomizeModel = new TeamCustomizeModel();
            teamCustomizeModel.setDate(teamModel.getDate());
            teamCustomizeModel.setNumber(teamModel.getNumber());
            teamCustomizeModel.setBlock(!teamCustomizeModel.isBlock());
            return saveTeamCustomizeModel(teamCustomizeModel);
        } else {
            teamCustomizeModel.setBlock(!teamCustomizeModel.isBlock());
            return saveTeamCustomizeModel(teamCustomizeModel);
        }
    }

    /**
     * 阵容伤害定制
     *
     * @param teamModel
     * @param teamCustomizeModel
     * @param damage
     */
    public static TeamCustomizeModel customizeTeamDamage(TeamModel teamModel, TeamCustomizeModel teamCustomizeModel, int damage) {
        if (teamCustomizeModel == null) {
            teamCustomizeModel = new TeamCustomizeModel();
            teamCustomizeModel.setDate(teamModel.getDate());
            teamCustomizeModel.setNumber(teamModel.getNumber());
            teamCustomizeModel.setEllipsisdamage(damage);
            return saveTeamCustomizeModel(teamCustomizeModel);
        } else {
            teamCustomizeModel.setEllipsisdamage(damage);
            return saveTeamCustomizeModel(teamCustomizeModel);
        }
    }

    /**
     * 移除阵容伤害定制
     *
     * @param teamCustomizeModel
     */
    public static TeamCustomizeModel removeCustomizeTeamDamage(TeamCustomizeModel teamCustomizeModel) {
        teamCustomizeModel.setEllipsisdamage(-1);
        return saveTeamCustomizeModel(teamCustomizeModel);
    }

    /**
     * 阵容定制有效
     *
     * @param teamCustomizeModel
     */
    private static boolean customizeEffective(TeamCustomizeModel teamCustomizeModel) {
        return teamCustomizeModel.isBlock() || teamCustomizeModel.damageEffective();
    }

    /**
     * 删除阵容自定义信息
     *
     * @param teamCustomizeModel
     */
    private static void deleteCustomizeModel(TeamCustomizeModel teamCustomizeModel) {
        DbHelper.delete(MyApplication.application, TeamCustomizeModel.class,
                new String[]{"date", "number"}, new String[]{teamCustomizeModel.getDate(), teamCustomizeModel.getNumber()});
    }

    /**
     * 保存阵容自定义信息
     *
     * @param teamCustomizeModel
     */
    private static TeamCustomizeModel saveTeamCustomizeModel(TeamCustomizeModel teamCustomizeModel) {
        deleteCustomizeModel(teamCustomizeModel);
        if (customizeEffective(teamCustomizeModel)) {
            DbHelper.insert(MyApplication.application, teamCustomizeModel);
            return teamCustomizeModel;
        } else {
            return null;
        }
    }

    /**
     * 判断分刀数据已收藏
     *
     * @param teamGroupCollectionModel
     */
    public static boolean isCollect(TeamGroupCollectionModel teamGroupCollectionModel) {
        if (teamGroupCollectionModel.getUserId() == UserManager.getInstance().getCurrentUserId()) {
            return true;
        }
        return false;
    }

    /**
     * 判断分刀数据已收藏
     *
     * @param teamGroupListModel
     */
    public static boolean isCollect(TeamGroupListModel teamGroupListModel) {
        boolean collected = false;
        List<TeamGroupCollectionModel> collectionlist = getCollectionlist();
        for (TeamGroupCollectionModel teamGroupCollectionModel : collectionlist) {
            if (teamGroupCollectionModel.getTeamone().equals(teamGroupListModel.getTeamone().getNumber())) {
                if (teamGroupCollectionModel.getTeamtwo().equals(teamGroupListModel.getTeamtwo().getNumber())) {
                    if (teamGroupCollectionModel.getTeamthree().equals(teamGroupListModel.getTeamthree().getNumber())) {
                        collected = true;
                        break;
                    }
                }
            }
        }
        return collected;
    }

    /**
     * 获取收藏的分刀数据列表
     */
    public static List<TeamGroupCollectionModel> getCollectionlist() {
        List<TeamGroupCollectionModel> collectionlist = DbHelper.query(MyApplication.application, TeamGroupCollectionModel.class, "userId", UserManager.getInstance().getCurrentUserId() + "");
        return collectionlist;
    }

    /**
     * 分刀收藏
     *
     * @param teamGroupListModel 分刀数据
     * @param collect            收藏
     */
    public static void collect(TeamGroupListModel teamGroupListModel, boolean collect) {
        int userId = UserManager.getInstance().getCurrentUserId();
        String date = getCurrentClanWarDate();
        if (collect) {
            TeamGroupCollectionModel teamGroupCollectionModel = new TeamGroupCollectionModel();
            teamGroupCollectionModel.setUserId(userId);
            teamGroupCollectionModel.setDate(date);
            teamGroupCollectionModel.setTeamone(teamGroupListModel.getTeamone().getNumber());
            teamGroupCollectionModel.setBorrowindexone(teamGroupListModel.getBorrowindexone());
            teamGroupCollectionModel.setTeamtwo(teamGroupListModel.getTeamtwo().getNumber());
            teamGroupCollectionModel.setBorrowindextwo(teamGroupListModel.getBorrowindextwo());
            teamGroupCollectionModel.setTeamthree(teamGroupListModel.getTeamthree().getNumber());
            teamGroupCollectionModel.setBorrowindexthree(teamGroupListModel.getBorrowindexthree());
            DbHelper.insert(MyApplication.application, teamGroupCollectionModel);
        } else {
            DbHelper.delete(MyApplication.application, TeamGroupCollectionModel.class
                    , new String[]{"userId", "date", "teamone", "teamtwo", "teamthree"}
                    , new String[]{
                            userId + "",
                            date,
                            teamGroupListModel.getTeamone().getNumber(),
                            teamGroupListModel.getTeamtwo().getNumber(),
                            teamGroupListModel.getTeamthree().getNumber()
                    });
        }
    }

    /**
     * 获取分刀信息筛选数据
     */
    public static TeamGroupScreenModel getScreenModel() {
        int userId = UserManager.getInstance().getCurrentUserId();
        TeamGroupScreenModel teamGroupScreenModel = DbHelper.query(MyApplication.application, TeamGroupScreenModel.class, userId + "");
        if (teamGroupScreenModel == null) {
            teamGroupScreenModel = new TeamGroupScreenModel();
            teamGroupScreenModel.setUserId(userId);
        }
        return teamGroupScreenModel;
    }

    /**
     * 设置分刀信息筛选数据
     *
     * @param screenModel
     */
    public static void setScreenModel(TeamGroupScreenModel screenModel) {
        int userId = UserManager.getInstance().getCurrentUserId();
        TeamGroupScreenModel teamGroupScreenModel = DbHelper.query(MyApplication.application, TeamGroupScreenModel.class, userId + "");
        if (teamGroupScreenModel == null) {
            DbHelper.insert(MyApplication.application, screenModel);
        } else {
            DbHelper.modify(MyApplication.application, screenModel, screenModel.getUserId() + "");
        }
    }
}
