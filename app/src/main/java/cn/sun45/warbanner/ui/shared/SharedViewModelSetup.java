package cn.sun45.warbanner.ui.shared;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.document.db.setup.ScreenCharacterModel;
import cn.sun45.warbanner.framework.MyApplication;
import cn.sun45.warbanner.framework.document.db.DbHelper;

/**
 * Created by Sun45 on 2021/5/30
 * 设置数据
 */
public class SharedViewModelSetup extends ViewModel {
    public MutableLiveData<List<ScreenCharacterModel>> screencharacterlist = new MutableLiveData<>();

    public void loadData() {
        boolean succeeded;
        List<ScreenCharacterModel> screenCharacterModelList = DbHelper.query(MyApplication.application, ScreenCharacterModel.class);
        if (screenCharacterModelList == null || screenCharacterModelList.isEmpty()) {
            succeeded = false;
        } else {
            succeeded = true;
        }
        screencharacterlist.postValue(screenCharacterModelList);
    }

    public void clearData() {
        screencharacterlist.postValue(new ArrayList<>());
    }
}
