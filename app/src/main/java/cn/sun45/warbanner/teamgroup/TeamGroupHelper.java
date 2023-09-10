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
        Utils.logD(TAG, "build");
        List<ScreenCharacterModel> screenCharacterModelList = CharacterHelper.getScreenCharacterList();
        List<TeamGroupScreenUsedCharacterModel> usedCharacterModelList = CharacterHelper.getUsedCharacterList();
        TeamGroupScreenModel teamGroupScreenModel = ClanwarHelper.getScreenModel();
        Map<Integer, Integer> screenCharacterMap = new TreeMap<>();
        if (characterscreenenable) {
            for (ScreenCharacterModel screenCharacterModel : screenCharacterModelList) {
                screenCharacterMap.put(screenCharacterModel.getCharacterId(), screenCharacterModel.getType());
            }
        }
        List<Integer> usingList = new ArrayList<>();
        List<Integer> usedList = new ArrayList<>();
        for (TeamGroupScreenUsedCharacterModel teamGroupScreenUsedCharacterModel : usedCharacterModelList) {
            if (teamGroupScreenUsedCharacterModel.getType() == CharacterUseType.TYPE_USING.getScreenType().getType()) {
                usingList.add(teamGroupScreenUsedCharacterModel.getCharacterId());
            } else if (teamGroupScreenUsedCharacterModel.getType() == CharacterUseType.TYPE_USED.getScreenType().getType()) {
                usedList.add(teamGroupScreenUsedCharacterModel.getCharacterId());
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
                if (usedList.contains(id)) {
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
        if (teamGroupScreenModel.isTeamoneenable()) {
            int borrowid = 0;
            switch (teamGroupScreenModel.getTeamoneborrowindex()) {
                case 0:
                    borrowid = teamGroupScreenModel.getTeamonecharacteroneid();
                    break;
                case 1:
                    borrowid = teamGroupScreenModel.getTeamonecharactertwoid();
                    break;
                case 2:
                    borrowid = teamGroupScreenModel.getTeamonecharacterthreeid();
                    break;
                case 3:
                    borrowid = teamGroupScreenModel.getTeamonecharacterfourid();
                    break;
                case 4:
                    borrowid = teamGroupScreenModel.getTeamonecharacterfiveid();
                    break;
                default:
                    break;
            }
            for (TeamGroupElementModel teamGroupElementModel : elementModels) {
                TeamGroupElementModel copyTeamGroupElementModel = teamGroupElementModel.getCopy();
                if (fit(copyTeamGroupElementModel,
                        teamGroupScreenModel.isTeamoneextra(), usingList,
                        teamGroupScreenModel.getTeamonestage(), teamGroupScreenModel.getTeamoneboss(), teamGroupScreenModel.getTeamoneauto(),
                        teamGroupScreenModel.getTeamonecharacteroneid(), teamGroupScreenModel.getTeamonecharactertwoid(), teamGroupScreenModel.getTeamonecharacterthreeid(), teamGroupScreenModel.getTeamonecharacterfourid(), teamGroupScreenModel.getTeamonecharacterfiveid(),
                        borrowid)) {
                    elementModelListOne.add(copyTeamGroupElementModel);
                }
            }
            if (elementModelListOne.isEmpty()) {
                return null;
            }
        }
        List<TeamGroupElementModel> elementModelListTwo = new ArrayList<>();
        if (teamGroupScreenModel.isTeamtwoenable()) {
            int borrowid = 0;
            switch (teamGroupScreenModel.getTeamtwoborrowindex()) {
                case 0:
                    borrowid = teamGroupScreenModel.getTeamtwocharacteroneid();
                    break;
                case 1:
                    borrowid = teamGroupScreenModel.getTeamtwocharactertwoid();
                    break;
                case 2:
                    borrowid = teamGroupScreenModel.getTeamtwocharacterthreeid();
                    break;
                case 3:
                    borrowid = teamGroupScreenModel.getTeamtwocharacterfourid();
                    break;
                case 4:
                    borrowid = teamGroupScreenModel.getTeamtwocharacterfiveid();
                    break;
                default:
                    break;
            }
            for (TeamGroupElementModel teamGroupElementModel : elementModels) {
                TeamGroupElementModel copyTeamGroupElementModel = teamGroupElementModel.getCopy();
                if (fit(copyTeamGroupElementModel,
                        teamGroupScreenModel.isTeamtwoextra(), usingList,
                        teamGroupScreenModel.getTeamtwostage(), teamGroupScreenModel.getTeamtwoboss(), teamGroupScreenModel.getTeamtwoauto(),
                        teamGroupScreenModel.getTeamtwocharacteroneid(), teamGroupScreenModel.getTeamtwocharactertwoid(), teamGroupScreenModel.getTeamtwocharacterthreeid(), teamGroupScreenModel.getTeamtwocharacterfourid(), teamGroupScreenModel.getTeamtwocharacterfiveid(),
                        borrowid)) {
                    elementModelListTwo.add(copyTeamGroupElementModel);
                }
            }
            if (elementModelListTwo.isEmpty()) {
                return null;
            }
        }
        List<TeamGroupElementModel> elementModelListThree = new ArrayList<>();
        if (teamGroupScreenModel.isTeamthreeenable()) {
            int borrowid = 0;
            switch (teamGroupScreenModel.getTeamthreeborrowindex()) {
                case 0:
                    borrowid = teamGroupScreenModel.getTeamthreecharacteroneid();
                    break;
                case 1:
                    borrowid = teamGroupScreenModel.getTeamthreecharactertwoid();
                    break;
                case 2:
                    borrowid = teamGroupScreenModel.getTeamthreecharacterthreeid();
                    break;
                case 3:
                    borrowid = teamGroupScreenModel.getTeamthreecharacterfourid();
                    break;
                case 4:
                    borrowid = teamGroupScreenModel.getTeamthreecharacterfiveid();
                    break;
                default:
                    break;
            }
            for (TeamGroupElementModel teamGroupElementModel : elementModels) {
                TeamGroupElementModel copyTeamGroupElementModel = teamGroupElementModel.getCopy();
                if (fit(copyTeamGroupElementModel,
                        teamGroupScreenModel.isTeamthreeextra(), usingList,
                        teamGroupScreenModel.getTeamthreestage(), teamGroupScreenModel.getTeamthreeboss(), teamGroupScreenModel.getTeamthreeauto(),
                        teamGroupScreenModel.getTeamthreecharacteroneid(), teamGroupScreenModel.getTeamthreecharactertwoid(), teamGroupScreenModel.getTeamthreecharacterthreeid(), teamGroupScreenModel.getTeamthreecharacterfourid(), teamGroupScreenModel.getTeamthreecharacterfiveid(),
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
        if (Integer.valueOf(teamModel.getBoss().substring(1, 2)) != boss + 1) {
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
        teamGroupElementModel.setScreencharacter(screencharacter);
        return true;
    }
}
