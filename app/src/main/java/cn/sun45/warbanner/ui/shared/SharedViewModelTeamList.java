package cn.sun45.warbanner.ui.shared;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.document.db.clanwar.TeamModel;
import cn.sun45.warbanner.framework.MyApplication;
import cn.sun45.warbanner.framework.document.db.DbHelper;


/**
 * Created by Sun45 on 2021/5/23
 * 阵容列表数据
 */
public class SharedViewModelTeamList extends ViewModel {
    public MutableLiveData<List<TeamModel>> teamList = new MutableLiveData<>();

    public void loadData() {
        boolean succeeded;
        List<TeamModel> teamModelList = DbHelper.query(MyApplication.application, TeamModel.class);
        if (teamModelList == null || teamModelList.isEmpty()) {
            succeeded = false;
        } else {
            succeeded = true;
            teamList.postValue(teamModelList);
        }
    }

    public void clearData() {
        teamList.postValue(new ArrayList<>());
    }
}
