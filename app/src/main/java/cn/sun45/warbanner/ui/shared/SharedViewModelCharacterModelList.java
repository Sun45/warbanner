package cn.sun45.warbanner.ui.shared;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.document.db.source.CharacterModel;
import cn.sun45.warbanner.framework.MyApplication;
import cn.sun45.warbanner.framework.document.db.DbHelper;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2021/5/23
 * 角色列表数据
 */
public class SharedViewModelCharacterModelList extends ViewModel {
    public MutableLiveData<List<CharacterModel>> characterlist = new MutableLiveData<>();

    private MasterCharaCallBack callBack;

    public void setCallBack(MasterCharaCallBack callBack) {
        this.callBack = callBack;
    }

    public void loadData() {
        boolean succeeded;
        List<CharacterModel> characterModelList = DbHelper.query(MyApplication.application, CharacterModel.class);
        if (characterModelList == null || characterModelList.isEmpty()) {
            succeeded = false;
        } else {
            //导入昵称数据
            try {
                JSONObject nicknamesjson = new JSONObject(Utils.getJson(MyApplication.application, "nicknames.json"));
                for (CharacterModel characterModel : characterModelList) {
                    JSONArray nicknamearray = nicknamesjson.optJSONArray(characterModel.getId() + "");
                    List<String> nicknames = new ArrayList<>();
                    if (nicknamearray != null) {
                        for (int i = 0; i < nicknamearray.length(); i++) {
                            nicknames.add(nicknamearray.optString(i));
                        }
                        characterModel.setNicknames(nicknames);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            succeeded = true;
            characterlist.postValue(characterModelList);
        }
        if (callBack != null) {
            callBack.charaLoadFinished(succeeded);
        }
    }

    public void clearData() {
        characterlist.postValue(new ArrayList<>());
    }

    public interface MasterCharaCallBack {
        void charaLoadFinished(boolean succeeded);
    }
}
