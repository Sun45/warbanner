package cn.sun45.warbanner.clanwar;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.document.database.setup.SetupDataBase;
import cn.sun45.warbanner.document.database.setup.models.TeamCustomizeModel;
import cn.sun45.warbanner.document.database.setup.models.TeamGroupCollectionModel;
import cn.sun45.warbanner.document.database.setup.models.TeamGroupScreenModel;
import cn.sun45.warbanner.document.database.setup.models.TeamListShowModel;
import cn.sun45.warbanner.document.database.source.SourceDataBase;
import cn.sun45.warbanner.document.database.source.models.TeamModel;
import cn.sun45.warbanner.framework.MyApplication;
import cn.sun45.warbanner.server.ServerManager;
import cn.sun45.warbanner.ui.views.teamgrouplist.TeamGroupListModel;
import cn.sun45.warbanner.user.UserManager;

/**
 * Created by Sun45 on 2021/6/28
 * 会战帮助类
 */
public class ClanwarHelper {
    /**
     * 获取阵容信息展示数据模型
     *
     * @return
     */
    public static TeamListShowModel getTeamListShowModel() {
        int userId = UserManager.getInstance().getCurrentUserId();
        TeamListShowModel teamListShowModel = SetupDataBase.getInstance().setupDao().quaryTeamListShow(userId);
        if (teamListShowModel == null) {
            teamListShowModel = new TeamListShowModel();
            teamListShowModel.setUserId(userId);
            SetupDataBase.getInstance().setupDao().insertTeamListShow(teamListShowModel);
        }
        return teamListShowModel;
    }

    /**
     * 设置阵容信息展示链接展示
     *
     * @param linkShow 链接展示
     */
    public static void setTeamListShowLinkShow(boolean linkShow) {
        TeamListShowModel teamListShowModel = getTeamListShowModel();
        teamListShowModel.setLinkShow(linkShow);
        SetupDataBase.getInstance().setupDao().insertTeamListShow(teamListShowModel);
    }

    /**
     * 设置阵容信息展示阶段筛选
     *
     * @param teamListStage 阶段筛选
     */
    public static void setTeamListShowStage(int teamListStage) {
        TeamListShowModel teamListShowModel = getTeamListShowModel();
        teamListShowModel.setTeamListStage(teamListStage);
        SetupDataBase.getInstance().setupDao().insertTeamListShow(teamListShowModel);
    }

    /**
     * 设置阵容信息展示BOSS筛选
     *
     * @param teamListBoss BOSS筛选
     */
    public static void setTeamListShowBoss(int teamListBoss) {
        TeamListShowModel teamListShowModel = getTeamListShowModel();
        teamListShowModel.setTeamListBoss(teamListBoss);
        SetupDataBase.getInstance().setupDao().insertTeamListShow(teamListShowModel);
    }

    /**
     * 设置阵容信息展示刀型筛选
     *
     * @param teamListType 刀型筛选
     */
    public static void setTeamListShowType(int teamListType) {
        TeamListShowModel teamListShowModel = getTeamListShowModel();
        teamListShowModel.setTeamListType(teamListType);
        SetupDataBase.getInstance().setupDao().insertTeamListShow(teamListShowModel);
    }

    /**
     * 查找阵容自定义信息
     *
     * @param teamModel
     * @param teamCustomizeModels
     */
    public static TeamCustomizeModel findCustomizeModel(TeamModel teamModel, List<TeamCustomizeModel> teamCustomizeModels) {
        TeamCustomizeModel teamCustomizeModel = null;
        if (teamModel != null && teamCustomizeModels != null) {
            for (TeamCustomizeModel model : teamCustomizeModels) {
                if (model.getTeamId() == teamModel.getId()) {
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
        return SetupDataBase.getInstance().setupDao().queryTeamCustomize(teamModel.getId());
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
            teamCustomizeModel.setTeamId(teamModel.getId());
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
            teamCustomizeModel.setTeamId(teamModel.getId());
            teamCustomizeModel.setDamage(damage);
            return saveTeamCustomizeModel(teamCustomizeModel);
        } else {
            teamCustomizeModel.setDamage(damage);
            return saveTeamCustomizeModel(teamCustomizeModel);
        }
    }

    /**
     * 移除阵容伤害定制
     *
     * @param teamCustomizeModel
     */
    public static TeamCustomizeModel removeCustomizeTeamDamage(TeamCustomizeModel teamCustomizeModel) {
        teamCustomizeModel.setDamage(-1);
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
        SetupDataBase.getInstance().setupDao().deleteTeamCustomize(teamCustomizeModel.getTeamId());
    }

    /**
     * 保存阵容自定义信息
     *
     * @param teamCustomizeModel
     */
    private static TeamCustomizeModel saveTeamCustomizeModel(TeamCustomizeModel teamCustomizeModel) {
        if (customizeEffective(teamCustomizeModel)) {
            SetupDataBase.getInstance().setupDao().insertTeamCustomize(teamCustomizeModel);
            return teamCustomizeModel;
        } else {
            deleteCustomizeModel(teamCustomizeModel);
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
     * 获得分刀收藏的阵容信息列表
     *
     * @param teamGroupCollectionModel
     */
    public static List<TeamModel> getTeamGroupCollectionTeamList(TeamGroupCollectionModel teamGroupCollectionModel) {
        List<TeamModel> list = new ArrayList<>();
        TeamModel teamone = SourceDataBase.getInstance().sourceDao().queryTeam(ServerManager.getInstance().getLang(), teamGroupCollectionModel.getTeamoneId());
        TeamModel teamtwo = SourceDataBase.getInstance().sourceDao().queryTeam(ServerManager.getInstance().getLang(), teamGroupCollectionModel.getTeamtwoId());
        TeamModel teamthree = SourceDataBase.getInstance().sourceDao().queryTeam(ServerManager.getInstance().getLang(), teamGroupCollectionModel.getTeamthreeId());
        list.add(teamone);
        list.add(teamtwo);
        list.add(teamthree);
        return list;
    }

    /**
     * 判断分刀数据已收藏
     *
     * @param teamGroupListModel
     */
    public static boolean isCollect(TeamGroupListModel teamGroupListModel) {
        TeamModel teamone = teamGroupListModel.getTeamone();
        TeamModel teamtwo = teamGroupListModel.getTeamtwo();
        TeamModel teamthree = teamGroupListModel.getTeamthree();
        boolean collected = false;
        List<TeamGroupCollectionModel> collectionlist = getCollectionlist();
        for (TeamGroupCollectionModel teamGroupCollectionModel : collectionlist) {
            if (teamGroupCollectionModel.getTeamoneId() == (teamone != null ? teamone.getId() : 0)) {
                if (teamGroupCollectionModel.getTeamtwoId() == (teamtwo != null ? teamtwo.getId() : 0)) {
                    if (teamGroupCollectionModel.getTeamthreeId() == (teamthree != null ? teamthree.getId() : 0)) {
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
        List<TeamGroupCollectionModel> collectionlist = SetupDataBase.getInstance().setupDao().queryAllTeamGroupCollection(UserManager.getInstance().getCurrentUserId());
        return collectionlist;
    }

    /**
     * 分刀收藏
     *
     * @param teamGroupListModel 分刀数据
     * @param collect            收藏
     */
    public static void collect(TeamGroupListModel teamGroupListModel, boolean collect) {
        TeamModel teamone = teamGroupListModel.getTeamone();
        TeamModel teamtwo = teamGroupListModel.getTeamtwo();
        TeamModel teamthree = teamGroupListModel.getTeamthree();
        int userId = UserManager.getInstance().getCurrentUserId();
        if (collect) {
            TeamGroupCollectionModel teamGroupCollectionModel = new TeamGroupCollectionModel();
            teamGroupCollectionModel.setUserId(userId);
            if (teamone != null) {
                teamGroupCollectionModel.setTeamoneId(teamone.getId());
                teamGroupCollectionModel.setBorrowindexone(teamGroupListModel.getBorrowindexone());
            }
            if (teamtwo != null) {
                teamGroupCollectionModel.setTeamtwoId(teamtwo.getId());
                teamGroupCollectionModel.setBorrowindextwo(teamGroupListModel.getBorrowindextwo());
            }
            if (teamthree != null) {
                teamGroupCollectionModel.setTeamthreeId(teamthree.getId());
                teamGroupCollectionModel.setBorrowindexthree(teamGroupListModel.getBorrowindexthree());
            }
            teamGroupCollectionModel.setTime(MyApplication.getTimecurrent() + "");
            SetupDataBase.getInstance().setupDao().insertTeamGroupCollection(teamGroupCollectionModel);
        } else {
            SetupDataBase.getInstance().setupDao().deleteTeamGroupCollection(userId,
                    teamone != null ? teamone.getId() : 0,
                    teamtwo != null ? teamtwo.getId() : 0,
                    teamthree != null ? teamthree.getId() : 0);
        }
    }

    /**
     * 获取分刀信息筛选数据
     */
    public static TeamGroupScreenModel getScreenModel() {
        int userId = UserManager.getInstance().getCurrentUserId();
        TeamGroupScreenModel teamGroupScreenModel = SetupDataBase.getInstance().setupDao().queryAllTeamGroupScreen(userId);
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
        SetupDataBase.getInstance().setupDao().insertTeamGroupScreen(screenModel);
    }
}
