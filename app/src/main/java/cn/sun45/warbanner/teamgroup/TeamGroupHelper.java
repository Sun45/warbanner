package cn.sun45.warbanner.teamgroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cn.sun45.pcrteamanalyser.analyse.AnalyseConfig;
import cn.sun45.pcrteamanalyser.analyse.AnalyseResult;
import cn.sun45.pcrteamanalyser.analyse.Analyser;
import cn.sun45.pcrteamanalyser.model.AnalyseTeam;
import cn.sun45.warbanner.character.CharacterHelper;
import cn.sun45.warbanner.clanwar.ClanwarHelper;
import cn.sun45.warbanner.document.database.setup.models.ScreenCharacterModel;
import cn.sun45.warbanner.document.database.setup.models.TeamCustomizeModel;
import cn.sun45.warbanner.document.database.setup.models.TeamGroupScreenModel;
import cn.sun45.warbanner.document.database.setup.models.TeamGroupScreenUsedCharacterModel;
import cn.sun45.warbanner.document.database.source.models.TeamModel;
import cn.sun45.warbanner.document.statics.charactertype.CharacterOwnType;
import cn.sun45.warbanner.document.statics.charactertype.CharacterUseType;
import cn.sun45.warbanner.framework.MyApplication;
import cn.sun45.warbanner.stage.StageManager;
import cn.sun45.warbanner.ui.views.teamgrouplist.TeamGroupListModel;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2023/4/4
 * 分刀帮助类
 */
public class TeamGroupHelper {
    private static final String TAG = "TeamGroupHelper";

    //分刀中断阈值
    public static final int interruptsize = 10_0000;

    private boolean characterscreenenable;

    public TeamGroupHelper(boolean characterscreenenable) {
        this.characterscreenenable = characterscreenenable;
    }

    public List<TeamGroupListModel> build(List<TeamModel> teamModels, List<TeamCustomizeModel> teamCustomizeModels) {
        List<TeamGroupScreenUsedCharacterModel> usedCharacterModelList = CharacterHelper.getUsedCharacterList();
        List<Integer> usingList = new ArrayList<>();
        List<Integer> usedList = new ArrayList<>();
        for (TeamGroupScreenUsedCharacterModel teamGroupScreenUsedCharacterModel : usedCharacterModelList) {
            if (teamGroupScreenUsedCharacterModel.getType() == CharacterUseType.TYPE_USING.getScreenType().getType()) {
                usingList.add(teamGroupScreenUsedCharacterModel.getCharacterId());
            } else if (teamGroupScreenUsedCharacterModel.getType() == CharacterUseType.TYPE_USED.getScreenType().getType()) {
                usedList.add(teamGroupScreenUsedCharacterModel.getCharacterId());
            }
        }
        TeamGroupScreenModel teamGroupScreenModel = ClanwarHelper.getScreenModel();
        TeamGroupConfigureModel teamGroupConfigureModel = new TeamGroupConfigureModel();
        if (teamGroupScreenModel.isTeamoneenable()) {
            TeamGroupConfigureModel.Configure teamOneConfigure = teamGroupConfigureModel.new Configure(
                    teamGroupScreenModel.getTeamonestage(), teamGroupScreenModel.getTeamoneboss() + 1, teamGroupScreenModel.getTeamoneauto(),
                    teamGroupScreenModel.getTeamonecharacteroneid(), teamGroupScreenModel.getTeamonecharactertwoid(), teamGroupScreenModel.getTeamonecharacterthreeid(), teamGroupScreenModel.getTeamonecharacterfourid(), teamGroupScreenModel.getTeamonecharacterfiveid(),
                    teamGroupScreenModel.getTeamoneborrowindex(), teamGroupScreenModel.isTeamoneextra()
            );
            teamGroupConfigureModel.setTeamOneConfigure(teamOneConfigure);
        }
        if (teamGroupScreenModel.isTeamtwoenable()) {
            TeamGroupConfigureModel.Configure teamtwoConfigure = teamGroupConfigureModel.new Configure(
                    teamGroupScreenModel.getTeamtwostage(), teamGroupScreenModel.getTeamtwoboss() + 1, teamGroupScreenModel.getTeamtwoauto(),
                    teamGroupScreenModel.getTeamtwocharacteroneid(), teamGroupScreenModel.getTeamtwocharactertwoid(), teamGroupScreenModel.getTeamtwocharacterthreeid(), teamGroupScreenModel.getTeamtwocharacterfourid(), teamGroupScreenModel.getTeamtwocharacterfiveid(),
                    teamGroupScreenModel.getTeamtwoborrowindex(), teamGroupScreenModel.isTeamtwoextra()
            );
            teamGroupConfigureModel.setTeamTwoConfigure(teamtwoConfigure);
        }
        if (teamGroupScreenModel.isTeamthreeenable()) {
            TeamGroupConfigureModel.Configure teamthreeConfigure = teamGroupConfigureModel.new Configure(
                    teamGroupScreenModel.getTeamthreestage(), teamGroupScreenModel.getTeamthreeboss() + 1, teamGroupScreenModel.getTeamthreeauto(),
                    teamGroupScreenModel.getTeamthreecharacteroneid(), teamGroupScreenModel.getTeamthreecharactertwoid(), teamGroupScreenModel.getTeamthreecharacterthreeid(), teamGroupScreenModel.getTeamthreecharacterfourid(), teamGroupScreenModel.getTeamthreecharacterfiveid(),
                    teamGroupScreenModel.getTeamthreeborrowindex(), teamGroupScreenModel.isTeamthreeextra()
            );
            teamGroupConfigureModel.setTeamThreeConfigure(teamthreeConfigure);
        }
        return build(teamModels, teamCustomizeModels, usingList, usedList, teamGroupConfigureModel);
    }

    public List<TeamGroupListModel> build(List<TeamModel> teamModels, List<TeamCustomizeModel> teamCustomizeModels,
                                          List<Integer> usingList, List<Integer> usedList, TeamGroupConfigureModel teamGroupConfigureModel) {
        Utils.logD(TAG, "build");
        List<ScreenCharacterModel> screenCharacterModelList = CharacterHelper.getScreenCharacterList();
        Map<Integer, Integer> screenCharacterMap = new TreeMap<>();
        if (characterscreenenable) {
            for (ScreenCharacterModel screenCharacterModel : screenCharacterModelList) {
                screenCharacterMap.put(screenCharacterModel.getCharacterId(), screenCharacterModel.getType());
            }
        }
        List<TeamGroupElementModel> elementModels = new ArrayList<>();
        for (TeamModel teamModel : teamModels) {
            TeamCustomizeModel teamCustomizeModel = ClanwarHelper.findCustomizeModel(teamModel, teamCustomizeModels);
            if (teamCustomizeModel != null && teamCustomizeModel.isBlock()) {
                continue;
            }
            List<Integer> idlist = Arrays.asList(teamModel.getCharacterone(), teamModel.getCharactertwo(), teamModel.getCharacterthree(), teamModel.getCharacterfour(), teamModel.getCharacterfive());
            int screencharacter = 0;
            boolean notuseable = false;
            for (int i = 0; i < idlist.size(); i++) {
                int id = idlist.get(i);
                if (screenCharacterMap.containsKey(id)) {
                    if (screenCharacterMap.get(id) == CharacterOwnType.TYPE_LACK.getScreenType().getType()) {
                        if (screencharacter != 0) {
                            notuseable = true;
                            break;
                        } else {
                            screencharacter = id;
                        }
                    } else {
                        notuseable = true;
                        break;
                    }
                }
                if (usedList != null && usedList.contains(id)) {
                    if (screencharacter != 0) {
                        notuseable = true;
                        break;
                    } else {
                        screencharacter = id;
                    }
                }
            }
            if (notuseable) {
                continue;
            }
            TeamGroupElementModel elementModel = new TeamGroupElementModel();
            elementModel.setIdlist(idlist);
            elementModel.setScreencharacter(screencharacter);
            elementModel.setTeamModel(teamModel);
            elementModel.setTeamCustomizeModel(teamCustomizeModel);
            elementModels.add(elementModel);
        }
        if (elementModels.isEmpty()) {
            return null;
        }
        List<TeamGroupElementModel> elementModelListOne = new ArrayList<>();
        TeamGroupConfigureModel.Configure teamOneConfigure = teamGroupConfigureModel.getTeamOneConfigure();
        if (teamOneConfigure != null) {
            int borrowid = 0;
            switch (teamOneConfigure.getBorrowindex()) {
                case 0:
                    borrowid = teamOneConfigure.getCharacteroneid();
                    break;
                case 1:
                    borrowid = teamOneConfigure.getCharactertwoid();
                    break;
                case 2:
                    borrowid = teamOneConfigure.getCharacterthreeid();
                    break;
                case 3:
                    borrowid = teamOneConfigure.getCharacterfourid();
                    break;
                case 4:
                    borrowid = teamOneConfigure.getCharacterfiveid();
                    break;
                default:
                    break;
            }
            for (TeamGroupElementModel teamGroupElementModel : elementModels) {
                TeamGroupElementModel copyTeamGroupElementModel = teamGroupElementModel.getCopy();
                if (fit(copyTeamGroupElementModel,
                        teamOneConfigure.isExtra(), usingList,
                        teamOneConfigure.getStage(), teamOneConfigure.getBoss(), teamOneConfigure.getAuto(),
                        teamOneConfigure.getCharacteroneid(), teamOneConfigure.getCharactertwoid(), teamOneConfigure.getCharacterthreeid(), teamOneConfigure.getCharacterfourid(), teamOneConfigure.getCharacterfiveid(),
                        borrowid)) {
                    elementModelListOne.add(copyTeamGroupElementModel);
                }
            }
            if (elementModelListOne.isEmpty()) {
                return null;
            }
        }
        List<TeamGroupElementModel> elementModelListTwo = new ArrayList<>();
        TeamGroupConfigureModel.Configure teamTwoConfigure = teamGroupConfigureModel.getTeamTwoConfigure();
        if (teamTwoConfigure != null) {
            int borrowid = 0;
            switch (teamTwoConfigure.getBorrowindex()) {
                case 0:
                    borrowid = teamTwoConfigure.getCharacteroneid();
                    break;
                case 1:
                    borrowid = teamTwoConfigure.getCharactertwoid();
                    break;
                case 2:
                    borrowid = teamTwoConfigure.getCharacterthreeid();
                    break;
                case 3:
                    borrowid = teamTwoConfigure.getCharacterfourid();
                    break;
                case 4:
                    borrowid = teamTwoConfigure.getCharacterfiveid();
                    break;
                default:
                    break;
            }
            for (TeamGroupElementModel teamGroupElementModel : elementModels) {
                TeamGroupElementModel copyTeamGroupElementModel = teamGroupElementModel.getCopy();
                if (fit(copyTeamGroupElementModel,
                        teamTwoConfigure.isExtra(), usingList,
                        teamTwoConfigure.getStage(), teamTwoConfigure.getBoss(), teamTwoConfigure.getAuto(),
                        teamTwoConfigure.getCharacteroneid(), teamTwoConfigure.getCharactertwoid(), teamTwoConfigure.getCharacterthreeid(), teamTwoConfigure.getCharacterfourid(), teamTwoConfigure.getCharacterfiveid(),
                        borrowid)) {
                    elementModelListTwo.add(copyTeamGroupElementModel);
                }
            }
            if (elementModelListTwo.isEmpty()) {
                return null;
            }
        }
        List<TeamGroupElementModel> elementModelListThree = new ArrayList<>();
        TeamGroupConfigureModel.Configure teamThreeConfigure = teamGroupConfigureModel.getTeamThreeConfigure();
        if (teamThreeConfigure != null) {
            int borrowid = 0;
            switch (teamThreeConfigure.getBorrowindex()) {
                case 0:
                    borrowid = teamThreeConfigure.getCharacteroneid();
                    break;
                case 1:
                    borrowid = teamThreeConfigure.getCharactertwoid();
                    break;
                case 2:
                    borrowid = teamThreeConfigure.getCharacterthreeid();
                    break;
                case 3:
                    borrowid = teamThreeConfigure.getCharacterfourid();
                    break;
                case 4:
                    borrowid = teamThreeConfigure.getCharacterfiveid();
                    break;
                default:
                    break;
            }
            for (TeamGroupElementModel teamGroupElementModel : elementModels) {
                TeamGroupElementModel copyTeamGroupElementModel = teamGroupElementModel.getCopy();
                if (fit(copyTeamGroupElementModel,
                        teamThreeConfigure.isExtra(), usingList,
                        teamThreeConfigure.getStage(), teamThreeConfigure.getBoss(), teamThreeConfigure.getAuto(),
                        teamThreeConfigure.getCharacteroneid(), teamThreeConfigure.getCharactertwoid(), teamThreeConfigure.getCharacterthreeid(), teamThreeConfigure.getCharacterfourid(), teamThreeConfigure.getCharacterfiveid(),
                        borrowid)) {
                    elementModelListThree.add(copyTeamGroupElementModel);
                }
            }
            if (elementModelListThree.isEmpty()) {
                return null;
            }
        }
        AnalyseConfig config = new AnalyseConfig<TeamGroupElementModel, TeamGroupListModel>() {
            @Override
            public AnalyseTeam buildAnalyseTeam(TeamGroupElementModel model) {
                List<Integer> idlist = model.getIdlist();
                int[] ids = new int[idlist.size()];
                for (int i = 0; i < idlist.size(); i++) {
                    ids[i] = idlist.get(i);
                }
                return new AnalyseTeam(ids, model.getDamage(), model.getScreencharacter());
            }

            @Override
            public TeamGroupListModel buildResult(AnalyseResult<TeamGroupElementModel> result) {
                AnalyseTeam<TeamGroupElementModel> teamOne = result.getTeamOne();
                AnalyseTeam<TeamGroupElementModel> teamTwo = result.getTeamTwo();
                AnalyseTeam<TeamGroupElementModel> teamThree = result.getTeamThree();
                TeamGroupElementModel elementone = null;
                int idone = 0;
                if (!teamOne.isEmpty()) {
                    elementone = teamOne.getModel();
                    idone = result.getBorrowIdOne();
                }
                TeamGroupElementModel elementtwo = null;
                int idtwo = 0;
                if (!teamTwo.isEmpty()) {
                    elementtwo = teamTwo.getModel();
                    idtwo = result.getBorrowIdTwo();
                }
                TeamGroupElementModel elementthree = null;
                int idthree = 0;
                if (!teamThree.isEmpty()) {
                    elementthree = teamThree.getModel();
                    idthree = result.getBorrowIdThree();
                }
                return new TeamGroupListModel(elementone, idone, elementtwo, idtwo, elementthree, idthree);
            }
        };
        config.testing = MyApplication.testing;
        config.interruptSize = interruptsize;
        Analyser<TeamGroupElementModel, TeamGroupListModel> analyser = new Analyser(config);
        return analyser.analyseWithModel(elementModelListOne, elementModelListTwo, elementModelListThree);
    }

    private boolean fit(TeamGroupElementModel teamGroupElementModel,
                        boolean extra, List<Integer> usingList,
                        int stage, int boss, int auto,
                        int characteroneid, int charactertwoid, int characterthreeid, int characterfourid, int characterfiveid,
                        int borrowid) {
        List<Integer> idlist = teamGroupElementModel.getIdlist();
        int screencharacter = teamGroupElementModel.getScreencharacter();
        if (borrowid != 0) {
            if (screencharacter != 0) {
                if (screencharacter != borrowid) {
                    return false;
                }
            } else {
                screencharacter = borrowid;
            }
        }
        TeamModel teamModel = teamGroupElementModel.getTeamModel();
        if (!StageManager.getInstance().matchTeamModel(teamModel, stage)) {
            return false;
        }
        if (boss != 0 && Integer.valueOf(teamModel.getBoss().substring(1, 2)) != boss) {
            return false;
        }
        if (auto != 0) {
            if (auto == 1 && !teamModel.isAuto()) {
                return false;
            }
            if (auto == 2 && (teamModel.isAuto() || teamModel.isFinish())) {
                return false;
            }
            if (auto == 3 && !teamModel.isFinish()) {
                return false;
            }
        }
        if (characteroneid != 0 && !idlist.contains(characteroneid)) {
            return false;
        }
        if (charactertwoid != 0 && !idlist.contains(charactertwoid)) {
            return false;
        }
        if (characterthreeid != 0 && !idlist.contains(characterthreeid)) {
            return false;
        }
        if (characterfourid != 0 && !idlist.contains(characterfourid)) {
            return false;
        }
        if (characterfiveid != 0 && !idlist.contains(characterfiveid)) {
            return false;
        }
        if (!extra) {
            if (usingList != null) {
                for (int id : idlist) {
                    for (int usingId : usingList) {
                        if (id == usingId) {
                            if (screencharacter != 0 && screencharacter != id) {
                                return false;
                            } else {
                                screencharacter = id;
                            }
                        }
                    }
                }
            }
        }
        teamGroupElementModel.setScreencharacter(screencharacter);
        return true;
    }
}
