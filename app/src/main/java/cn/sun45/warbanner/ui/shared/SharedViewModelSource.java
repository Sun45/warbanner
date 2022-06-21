package cn.sun45.warbanner.ui.shared;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.document.database.source.SourceDataBase;
import cn.sun45.warbanner.document.database.source.models.BossModel;
import cn.sun45.warbanner.document.database.source.models.CharacterModel;
import cn.sun45.warbanner.framework.MyApplication;
import cn.sun45.warbanner.server.ServerManager;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2021/5/23
 * 数据源数据
 */
public class SharedViewModelSource extends ViewModel {
    private static final String TAG = "SharedViewModelSource";

    public MutableLiveData<List<CharacterModel>> characterList = new MutableLiveData<>();
    public MutableLiveData<List<BossModel>> bossList = new MutableLiveData<>();

    public void loadData() {
        boolean succeeded = true;
        List<CharacterModel> characterModelList = SourceDataBase.getInstance().sourceDao().queryAllCharacter(ServerManager.getInstance().getLang());
        if (characterModelList == null || characterModelList.isEmpty()) {
            succeeded = false;
        }
        List<BossModel> bossModelList = SourceDataBase.getInstance().sourceDao().queryAllBoss(ServerManager.getInstance().getLang());
        if (bossModelList == null || bossModelList.isEmpty()) {
            succeeded = false;
        }

        if (succeeded) {
            characterList.postValue(characterModelList);
            if (MyApplication.testing) {
                for (CharacterModel characterModel : characterModelList) {
                    Utils.logD(TAG, characterModel.toString());
                }
            }

            //导入会战排期
            bossList.postValue(bossModelList);
            if (MyApplication.testing) {
                for (BossModel bossModel : bossModelList) {
                    Utils.logD(TAG, bossModel.toString());
                }
            }
        }
    }

    public void clearData() {
        characterList.postValue(new ArrayList<>());
        bossList.postValue(new ArrayList<>());
    }
}
