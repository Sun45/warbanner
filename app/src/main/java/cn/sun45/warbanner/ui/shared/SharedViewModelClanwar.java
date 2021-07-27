package cn.sun45.warbanner.ui.shared;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.clanwar.ClanwarHelper;
import cn.sun45.warbanner.document.db.clanwar.TeamCustomizeModel;
import cn.sun45.warbanner.document.db.clanwar.TeamGroupCollectionModel;
import cn.sun45.warbanner.document.db.clanwar.TeamModel;
import cn.sun45.warbanner.framework.MyApplication;
import cn.sun45.warbanner.framework.document.db.DbHelper;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2021/6/30
 * 会战数据
 */
public class SharedViewModelClanwar extends ViewModel {
    public MutableLiveData<List<TeamCustomizeModel>> teamCustomizeList = new MutableLiveData<>();
    public MutableLiveData<List<TeamGroupCollectionModel>> teamGroupCollectionList = new MutableLiveData<>();
    public MutableLiveData<List<TeamModel>> teamList = new MutableLiveData<>();

    public void loadData() {
        String date = ClanwarHelper.getCurrentClanWarDate();

        boolean succeeded = true;
        List<TeamCustomizeModel> customizeList = null;
        List<TeamGroupCollectionModel> collectionlist = null;
        List<TeamModel> teamModelList = null;
        if (!TextUtils.isEmpty(date)) {
            customizeList = DbHelper.query(MyApplication.application, TeamCustomizeModel.class, "date", date);
            collectionlist = DbHelper.query(MyApplication.application, TeamGroupCollectionModel.class, "date", date);
            teamModelList = DbHelper.query(MyApplication.application, TeamModel.class, "date", date);
        }
        if (customizeList == null || customizeList.isEmpty()) {
            succeeded = false;
        }
        if (collectionlist == null || collectionlist.isEmpty()) {
            succeeded = false;
        }
//        if (teamModelList == null || teamModelList.size() <= 3) {
//            try {
//                DbHelper.delete(MyApplication.application, TeamModel.class);
//                JSONArray array = new JSONArray(Utils.getJson(MyApplication.application, "teamgroup.json"));
//                for (int i = 0; i < array.length(); i++) {
//                    JSONObject object = array.optJSONObject(i);
//                    TeamModel teamModel = new TeamModel();
//                    teamModel.setNumber(object.optString("number"));
//                    teamModel.setStage(object.getInt("stage"));
//                    teamModel.setBoss(object.getString("boss"));
//                    teamModel.setDamage(object.optString("damage"));
//                    teamModel.setDamagenumber(object.getInt("damagenumber"));
//                    teamModel.setAuto(object.optBoolean("auto"));
//                    teamModel.setCharacterone(object.optString("characterone"));
//                    teamModel.setCharactertwo(object.optString("charactertwo"));
//                    teamModel.setCharacterthree(object.optString("characterthree"));
//                    teamModel.setCharacterfour(object.optString("characterfour"));
//                    teamModel.setCharacterfive(object.optString("characterfive"));
//                    teamModel.setRemarks(object.optString("remarks"));
//                    teamModel.setCollect(object.optBoolean("collect"));
//                    DbHelper.insert(MyApplication.application, teamModel);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            teamModelList = DbHelper.query(MyApplication.application, TeamModel.class);
//        }
        if (teamModelList == null || teamModelList.isEmpty()) {
            succeeded = false;
        }
        teamCustomizeList.postValue(customizeList);
        teamGroupCollectionList.postValue(collectionlist);
        teamList.postValue(teamModelList);
    }

    public void clearData() {
        teamGroupCollectionList.postValue(new ArrayList<>());
        teamList.postValue(new ArrayList<>());
    }
}
