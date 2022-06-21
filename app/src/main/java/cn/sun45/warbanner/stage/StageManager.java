package cn.sun45.warbanner.stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.sun45.warbanner.document.database.source.models.TeamModel;
import cn.sun45.warbanner.document.statics.Locale;
import cn.sun45.warbanner.server.ServerManager;

/**
 * Created by Sun45 on 2022/6/11
 * 阶段管理
 */
public class StageManager {
    //单例对象
    private static StageManager instance;

    public static StageManager getInstance() {
        if (instance == null) {
            synchronized (StageManager.class) {
                if (instance == null) {
                    instance = new StageManager();
                }
            }
        }
        return instance;
    }

    public int[][] getStage() {
        return ServerManager.getInstance().getLocal().getStage();
    }

    public int getStageCount() {
        return getStage().length;
    }

    public int[] getStageArray(int stagePosition) {
        return getStage()[stagePosition];
    }

    public String getStageDescription(int stagePosition) {
        int[] array = getStageArray(stagePosition);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (i != 0) {
                stringBuilder.append("+");
            }
            stringBuilder.append((char) ('A' + array[i] - 1));
        }
        stringBuilder.append("面");
        return stringBuilder.toString();
    }

    public List<String> getStageDescriptionList() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < getStageCount(); i++) {
            list.add(getStageDescription(i));
        }
        return list;
    }

    public boolean matchTeamModel(TeamModel teamModel, int stagePosition) {
        int[] stageArray = getStageArray(stagePosition);
        for (int stage : stageArray) {
            if (stage==teamModel.getStage()) {
                return true;
            }
        }
        return false;
    }
}
