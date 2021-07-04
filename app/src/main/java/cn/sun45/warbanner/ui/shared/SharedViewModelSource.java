package cn.sun45.warbanner.ui.shared;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.document.db.source.CharacterModel;
import cn.sun45.warbanner.document.db.source.ClanWarModel;
import cn.sun45.warbanner.framework.MyApplication;
import cn.sun45.warbanner.framework.document.db.DbHelper;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2021/5/23
 * 数据源数据
 */
public class SharedViewModelSource extends ViewModel {
    private static final String TAG = "SharedViewModelSource";

    public MutableLiveData<List<CharacterModel>> characterlist = new MutableLiveData<>();
    public MutableLiveData<List<ClanWarModel>> clanWarlist = new MutableLiveData<>();

    private CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void loadData() {
        boolean succeeded = true;
        List<CharacterModel> characterModelList = DbHelper.query(MyApplication.application, CharacterModel.class);
        if (characterModelList == null || characterModelList.isEmpty()) {
            succeeded = false;
        }
        List<ClanWarModel> clanWarModelList = DbHelper.query(MyApplication.application, ClanWarModel.class);
        if (clanWarModelList == null || clanWarModelList.isEmpty()) {
            succeeded = false;
        }

        if (succeeded) {
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
            characterlist.postValue(characterModelList);
//            for (CharacterModel characterModel : characterModelList) {
//                Utils.logD(TAG, characterModel.toString());
//            }

            //导入会战排期
            clanWarlist.postValue(clanWarModelList);
//            for (ClanWarModel clanWarModel : clanWarModelList) {
//                Utils.logD(TAG, clanWarModel.toString());
//            }
        }

        if (callBack != null) {
            callBack.sourceLoadFinished(succeeded);
        }
    }

    public void clearData() {
        characterlist.postValue(new ArrayList<>());
        clanWarlist.postValue(new ArrayList<>());
    }

    public interface CallBack {
        void sourceLoadFinished(boolean succeeded);
    }
}
