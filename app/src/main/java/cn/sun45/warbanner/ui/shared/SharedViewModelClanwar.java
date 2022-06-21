package cn.sun45.warbanner.ui.shared;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.clanwar.ClanwarHelper;
import cn.sun45.warbanner.document.database.setup.SetupDataBase;
import cn.sun45.warbanner.document.database.setup.models.TeamCustomizeModel;
import cn.sun45.warbanner.document.database.setup.models.TeamGroupCollectionModel;
import cn.sun45.warbanner.document.database.source.SourceDataBase;
import cn.sun45.warbanner.document.database.source.models.TeamModel;
import cn.sun45.warbanner.server.ServerManager;
import cn.sun45.warbanner.user.UserManager;

/**
 * Created by Sun45 on 2021/6/30
 * 会战数据
 */
public class SharedViewModelClanwar extends ViewModel {
    public MutableLiveData<List<TeamCustomizeModel>> teamCustomizeList = new MutableLiveData<>();
    public MutableLiveData<List<TeamGroupCollectionModel>> teamGroupCollectionList = new MutableLiveData<>();
    public MutableLiveData<List<TeamModel>> teamList = new MutableLiveData<>();

    public void loadData() {
        boolean succeeded = true;
        int userId = UserManager.getInstance().getCurrentUserId();
        List<TeamCustomizeModel> customizeList = SetupDataBase.getInstance().setupDao().queryAllTeamCustomize();
        List<TeamGroupCollectionModel> collectionlist = SetupDataBase.getInstance().setupDao().queryAllTeamGroupCollection(userId);
        List<TeamModel> teamModelList = SourceDataBase.getInstance().sourceDao().queryAllTeam(ServerManager.getInstance().getLang());
        if (customizeList == null || customizeList.isEmpty()) {
            succeeded = false;
        }
        if (collectionlist == null || collectionlist.isEmpty()) {
            succeeded = false;
        }
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
