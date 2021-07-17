package cn.sun45.warbanner.ui.shared;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.sun45.warbanner.datamanager.source.RawClanBattlePeriod;
import cn.sun45.warbanner.datamanager.source.RawClanBattlePhase;
import cn.sun45.warbanner.datamanager.source.RawEnemy;
import cn.sun45.warbanner.datamanager.source.RawUnitBasic;
import cn.sun45.warbanner.datamanager.source.RawWaveGroup;
import cn.sun45.warbanner.datamanager.source.SourceDataProcessHelper;
import cn.sun45.warbanner.document.StaticHelper;
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
        List<CharacterModel> characterModelList = DbHelper.query(MyApplication.application, CharacterModel.class);
        if (characterModelList == null || characterModelList.isEmpty()) {
            List<RawUnitBasic> rawUnitBasics = SourceDataProcessHelper.getInstance().getCharaBase();
            if (rawUnitBasics != null && !rawUnitBasics.isEmpty()) {
                for (RawUnitBasic rawUnitBasic : rawUnitBasics) {
                    CharacterModel character = new CharacterModel();
                    rawUnitBasic.set(character);
                    DbHelper.insert(MyApplication.application, character);
                }
            }
        }

        List<ClanWarModel> clanWarModelList = DbHelper.query(MyApplication.application, ClanWarModel.class);
        if (clanWarModelList == null || clanWarModelList.isEmpty()) {
            List<RawClanBattlePeriod> rawClanBattlePeriods = SourceDataProcessHelper.getInstance().getClanBattlePeriod();
            if (rawClanBattlePeriods != null && !rawClanBattlePeriods.isEmpty()) {
                for (RawClanBattlePeriod rawClanBattlePeriod : rawClanBattlePeriods) {
                    ClanWarModel clanWarModel = new ClanWarModel();
                    rawClanBattlePeriod.set(clanWarModel);
                    List<RawClanBattlePhase> rawClanBattlePhases = SourceDataProcessHelper.getInstance().getClanBattlePhase(rawClanBattlePeriod.clan_battle_id);
                    if (rawClanBattlePhases != null && !rawClanBattlePhases.isEmpty()) {
                        RawClanBattlePhase rawClanBattlePhase = rawClanBattlePhases.get(0);
                        List<Integer> waveGroupList = new ArrayList<>();
                        waveGroupList.add(rawClanBattlePhase.wave_group_id_1);
                        waveGroupList.add(rawClanBattlePhase.wave_group_id_2);
                        waveGroupList.add(rawClanBattlePhase.wave_group_id_3);
                        waveGroupList.add(rawClanBattlePhase.wave_group_id_4);
                        waveGroupList.add(rawClanBattlePhase.wave_group_id_5);
                        List<RawWaveGroup> rawWaveGroups = SourceDataProcessHelper.getInstance().getWaveGroupData(waveGroupList);
                        if (rawWaveGroups != null && rawWaveGroups.size() == 5) {
                            List<Integer> enemyIdList = new ArrayList<>();
                            enemyIdList.add(rawWaveGroups.get(0).enemy_id_1);
                            enemyIdList.add(rawWaveGroups.get(1).enemy_id_1);
                            enemyIdList.add(rawWaveGroups.get(2).enemy_id_1);
                            enemyIdList.add(rawWaveGroups.get(3).enemy_id_1);
                            enemyIdList.add(rawWaveGroups.get(4).enemy_id_1);
                            List<RawEnemy> rawEnemies = SourceDataProcessHelper.getInstance().getEnemy(enemyIdList);
                            if (rawEnemies != null && rawEnemies.size() == 5) {
                                clanWarModel.setBossonename(rawEnemies.get(0).name);
                                clanWarModel.setBossoneiconurl(String.format(Locale.US, StaticHelper.ICON_URL, rawEnemies.get(0).prefab_id));
                                clanWarModel.setBosstwoname(rawEnemies.get(1).name);
                                clanWarModel.setBosstwoiconurl(String.format(Locale.US, StaticHelper.ICON_URL, rawEnemies.get(1).prefab_id));
                                clanWarModel.setBossthreename(rawEnemies.get(2).name);
                                clanWarModel.setBossthreeiconurl(String.format(Locale.US, StaticHelper.ICON_URL, rawEnemies.get(2).prefab_id));
                                clanWarModel.setBossfourname(rawEnemies.get(3).name);
                                clanWarModel.setBossfouriconurl(String.format(Locale.US, StaticHelper.ICON_URL, rawEnemies.get(3).prefab_id));
                                clanWarModel.setBossfivename(rawEnemies.get(4).name);
                                clanWarModel.setBossfiveiconurl(String.format(Locale.US, StaticHelper.ICON_URL, rawEnemies.get(4).prefab_id));
                            }
                        }
                    }
                    DbHelper.insert(MyApplication.application, clanWarModel);
                }
            }
        }

        boolean succeeded = true;
        characterModelList = DbHelper.query(MyApplication.application, CharacterModel.class);
        if (characterModelList == null || characterModelList.isEmpty()) {
            succeeded = false;
        }
        clanWarModelList = DbHelper.query(MyApplication.application, ClanWarModel.class);
        if (clanWarModelList == null || clanWarModelList.isEmpty()) {
            succeeded = false;
        }

        if (succeeded) {
//            //导入昵称数据
//            try {
//                JSONObject nicknamesjson = new JSONObject(Utils.getJson(MyApplication.application, "nicknames.json"));
//                for (CharacterModel characterModel : characterModelList) {
//                    JSONArray nicknamearray = nicknamesjson.optJSONArray(characterModel.getId() + "");
//                    List<String> nicknames = new ArrayList<>();
//                    if (nicknamearray != null) {
//                        for (int i = 0; i < nicknamearray.length(); i++) {
//                            nicknames.add(nicknamearray.optString(i));
//                        }
//                        characterModel.setNicknames(nicknames);
//                    }
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
            characterlist.postValue(characterModelList);
            if (MyApplication.testing) {
                for (CharacterModel characterModel : characterModelList) {
                    Utils.logD(TAG, characterModel.toString());
                }
            }

            //导入会战排期
            clanWarlist.postValue(clanWarModelList);
            if (MyApplication.testing) {
                for (ClanWarModel clanWarModel : clanWarModelList) {
                    Utils.logD(TAG, clanWarModel.toString());
                }
            }
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
