package cn.sun45.warbanner.ui.shared;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import cn.sun45.warbanner.document.db.clanwar.TeamModel;

/**
 * Created by Sun45 on 2021/7/4
 * 分刀筛选阵容数据
 */
public class SharedViewModelTeamScreenTeam extends ViewModel {
    public MutableLiveData<TeamModel> teamOne = new MutableLiveData<>();
    public MutableLiveData<TeamModel> teamTwo = new MutableLiveData<>();
    public MutableLiveData<TeamModel> teamThree = new MutableLiveData<>();
}
