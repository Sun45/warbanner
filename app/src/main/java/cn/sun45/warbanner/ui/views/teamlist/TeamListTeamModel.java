package cn.sun45.warbanner.ui.views.teamlist;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.document.db.clanwar.TeamModel;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2021/6/2
 * 阵容列表阵容信息数据模型
 */
public class TeamListTeamModel {
    private TeamModel teamModel;
    private int borrowindex;
    private List<TeamListRemarkModel> remarkModels;

    public TeamListTeamModel(TeamModel teamModel) {
        this(teamModel, -1);
    }

    public TeamListTeamModel(TeamModel teamModel, int borrowindex) {
        this.teamModel = teamModel;
        this.borrowindex = borrowindex;
        remarkModels = new ArrayList<>();
        try {
            JSONArray remarkarray = new JSONArray(teamModel.getRemarks());
            for (int i = 0; i < remarkarray.length(); i++) {
                JSONObject object = remarkarray.optJSONObject(i);
                String content = object.optString("content");
                content = Utils.replaceBlank(content);
                String link = object.optString("link");
                if (!TextUtils.isEmpty(content) && !TextUtils.isEmpty(link)) {
                    TeamListRemarkModel remarkModel = new TeamListRemarkModel();
                    remarkModel.setContent(content);
                    remarkModel.setLink(link);
                    remarkModels.add(remarkModel);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public TeamModel getTeamModel() {
        return teamModel;
    }

    public int getBorrowindex() {
        return borrowindex;
    }

    public List<TeamListRemarkModel> getRemarkModels() {
        return remarkModels;
    }
}
