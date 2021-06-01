package cn.sun45.warbanner.datamanager.clanwar;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.document.StaticHelper;
import cn.sun45.warbanner.document.db.clanwar.TeamModel;
import cn.sun45.warbanner.document.preference.ClanwarPreference;
import cn.sun45.warbanner.framework.document.db.DbHelper;
import cn.sun45.warbanner.logic.clanwar.HtmlDocCellModel;
import cn.sun45.warbanner.util.Utils;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;


/**
 * Created by Sun45 on 2021/5/23
 * 会战数据获取管理
 */
public class ClanWarManager {
    private static final String TAG = "ClanWarManager";

    private static final String bossregex = "[1-5]";

    private Activity activity;

    private Handler handler;

    //单例对象
    private static ClanWarManager instance;

    public static void init(Activity activity) {
        if (instance == null) {
            synchronized (ClanWarManager.class) {
                if (instance == null) {
                    instance = new ClanWarManager(activity);
                }
            }
        }
    }

    private static final int DOWNLOAD_STAGETWO = 0;
    private static final int DOWNLOAD_STAGETHREE = 1;


    public static ClanWarManager getInstance() {
        return instance;
    }

    public ClanWarManager(Activity activity) {
        this.activity = activity;
        handler = new Handler(activity.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case DOWNLOAD_STAGETWO:
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.message(R.string.clanwar_update_progress_text_two, null, null);
                        }
                        break;
                    case DOWNLOAD_STAGETHREE:
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.message(R.string.clanwar_update_progress_text_three, null, null);
                        }
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private IActivityCallBack iActivityCallBack;

    public void setiActivityCallBack(IActivityCallBack iActivityCallBack) {
        this.iActivityCallBack = iActivityCallBack;
    }

    private MaterialDialog progressDialog;

    /**
     * 获取更新信息
     */
    public String getUpdateInfo() {
        long lastupdate = new ClanwarPreference().getLastupdate();
        String updateInfo;
        if (lastupdate == 0) {
            updateInfo = Utils.getString(R.string.clanwar_update_last_empty);
        } else {
            long timed = System.currentTimeMillis() - lastupdate;
            String timestr = "";
            timed /= 1000;
            if (timed < 60) {
                timestr = timed + "s";
            } else {
                timed /= 60;
                if (timed < 60) {
                    timestr = timed + "m";
                } else {
                    timed /= 60;
                    if (timed < 24) {
                        timestr = timed + "h";
                    } else {
                        timed /= 24;
                        timestr = timed + "d";
                    }
                }
            }
            updateInfo = Utils.getStringWithPlaceHolder(R.string.clanwar_update_last, timestr);
        }
        return updateInfo;
    }

    /**
     * 监测是否需要下载数据
     */
    public boolean needDataDownload() {
        boolean needDataLoad = false;
        if (new ClanwarPreference().getLastupdate() == 0) {
            needDataLoad = true;
        }
        return needDataLoad;
//        return true;
    }

    /***
     * 弹出更新确认下载数据对话框
     *
     * @param autocheck 自动检查
     */
    public void showConfirmDialog(boolean autocheck) {
        Utils.logD(TAG, "showConfirmDialog");
        new MaterialDialog(activity, MaterialDialog.getDEFAULT_BEHAVIOR())
                .title(R.string.clanwar_update_dialog_title, null)
                .message(R.string.clanwar_update_dialog_text, null, null)
                .cancelOnTouchOutside(false)
                .positiveButton(R.string.clanwar_update_dialog_confirm, null, new Function1<MaterialDialog, Unit>() {
                    @Override
                    public Unit invoke(MaterialDialog materialDialog) {
                        downloadData(autocheck);
                        return null;
                    }
                })
                .negativeButton(R.string.clanwar_update_dialog_cancel, null, new Function1<MaterialDialog, Unit>() {
                    @Override
                    public Unit invoke(MaterialDialog materialDialog) {
                        Log.d(TAG, "Canceled");
                        if (iActivityCallBack != null) {
                            iActivityCallBack.ClanWarReady(autocheck);
                        }
                        return null;
                    }
                })
                .show();
    }

    /**
     * 加载数据
     */
    private void downloadData(boolean autocheck) {
        Utils.logD(TAG, "downloadData");
        progressDialog = new MaterialDialog(activity, MaterialDialog.getDEFAULT_BEHAVIOR());
        progressDialog
                .title(R.string.clanwar_update_progress_title, null)
                .message(R.string.clanwar_update_progress_text_one, null, null)
                .cancelable(false)
                .show();
        DbHelper.delete(activity, TeamModel.class);
        new ClanwarPreference().setLastupdate(System.currentTimeMillis());
        loadDataAndSave(1, new ClanWarManagerListener() {
            @Override
            public void loadFinish() {
                handler.sendEmptyMessage(DOWNLOAD_STAGETWO);
                loadDataAndSave(2, new ClanWarManagerListener() {
                    @Override
                    public void loadFinish() {
                        handler.sendEmptyMessage(DOWNLOAD_STAGETHREE);
                        loadDataAndSave(3, new ClanWarManagerListener() {
                            @Override
                            public void loadFinish() {
                                if (iActivityCallBack != null) {
                                    iActivityCallBack.showSnackBar(R.string.clanwar_update_finished_text);
                                    iActivityCallBack.ClanWarReady(autocheck);
                                }
                                if (progressDialog != null) {
                                    progressDialog.cancel();
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * 读取数据并存库
     *
     * @param stage 阶段 1,2,3
     */
    private void loadDataAndSave(int stage, ClanWarManagerListener clanWarManagerListener) {
        Utils.logD(TAG, "loadDataAndSave stage:" + stage);
        int start = 0;
        int end = 250;
        String url = null;
        switch (stage) {
            case 1:
                url = StaticHelper.CLANWAR_ONE_URL;
                break;
            case 2:
                url = StaticHelper.CLANWAR_TWO_URL;
                break;
            case 3:
                url = StaticHelper.CLANWAR_THREE_URL;
                break;
            default:
                break;
        }
        HtmlDocRequestHelper.request(activity, url, start, end, new HtmlDocRequestHelper.HtmlDocManagerListener() {
            @Override
            public void get(List<HtmlDocCellModel> list) {
                Utils.logD(TAG, "get");
                for (int i = list.size() - 1; i >= 0; i--) {
                    HtmlDocCellModel htmlDocCellModel = list.get(i);
                    if (TextUtils.isEmpty(htmlDocCellModel.getContent()) && TextUtils.isEmpty(htmlDocCellModel.getLink())) {
                        list.remove(i);
                    }
                }
                String stageregex = null;
                switch (stage) {
                    case 1:
                        stageregex = "(a|A)";
                        break;
                    case 2:
                        stageregex = "(b|B)";
                        break;
                    case 3:
                        stageregex = "(c|C)";
                        break;
                    default:
                        break;
                }
                try {
                    List<TeamModel> teamModels = new ArrayList<>();
                    TeamModel teamModel = null;
                    JSONArray remarks = null;
                    for (int i = 0; i < list.size(); i++) {
                        boolean matchteamstart = false;
                        if ((i + 7) < list.size()) {
                            String boss = list.get(i).getContent();
                            String number = list.get(i + 1).getContent();
                            String characterone = list.get(i + 2).getContent();
                            String charactertwo = list.get(i + 3).getContent();
                            String characterthree = list.get(i + 4).getContent();
                            String characterfour = list.get(i + 5).getContent();
                            String characterfive = list.get(i + 6).getContent();
                            String damage = list.get(i + 7).getContent();
                            if (!TextUtils.isEmpty(boss) && Pattern.matches("^" + stageregex + bossregex + "$", boss)
                                    && !TextUtils.isEmpty(number) && Pattern.matches("^" + stageregex + "t?" + bossregex + "[0-9]{2}$", number)
                                    && !TextUtils.isEmpty(characterone)
                                    && !TextUtils.isEmpty(charactertwo)
                                    && !TextUtils.isEmpty(characterthree)
                                    && !TextUtils.isEmpty(characterfour)
                                    && !TextUtils.isEmpty(characterfive)
                                    && !TextUtils.isEmpty(damage) && Pattern.matches("^[0-9]+(w|W)$", damage)) {
                                matchteamstart = true;
                                if (teamModel != null) {
                                    teamModel.setRemarks(remarks.toString());
                                    teamModels.add(teamModel);
                                }
                                teamModel = new TeamModel();
                                remarks = new JSONArray();
                                teamModel.setBoss(boss);
                                teamModel.setNumber(number);
                                teamModel.setStage(stage);
                                teamModel.setCharacterone(characterone);
                                teamModel.setCharactertwo(charactertwo);
                                teamModel.setCharacterthree(characterthree);
                                teamModel.setCharacterfour(characterfour);
                                teamModel.setCharacterfive(characterfive);
                                teamModel.setDamage(damage);
                                damage = damage.replace("w", "");
                                damage = damage.replace("W", "");
                                int damagenumber = Integer.valueOf(damage);
                                teamModel.setDamagenumber(damagenumber);
                                teamModel.setAuto(Pattern.matches("^" + stageregex + "t" + bossregex + "[0-9]{2}$", number));
                                i += 7;
                            }
                        }
                        if (!matchteamstart && teamModel != null) {
                            String content = list.get(i).getContent();
                            String link = list.get(i).getLink();
                            if (!TextUtils.isEmpty(content)) {
                                JSONObject remark = new JSONObject();
                                remark.put("content", content);
                                remark.put("link", link);
                                remarks.put(remark);
                            }
                        }
                    }
                    teamModel.setRemarks(remarks.toString());
                    teamModels.add(teamModel);
                    for (TeamModel model : teamModels) {
                        DbHelper.insert(activity, model);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                clanWarManagerListener.loadFinish();
            }

            @Override
            public void fail(String message) {
                Utils.logD(TAG, "fail message:" + message);
                clanWarManagerListener.loadFinish();
            }
        });
    }

    public interface ClanWarManagerListener {
        void loadFinish();
    }

    public interface IActivityCallBack {
        void showSnackBar(@StringRes int messageRes);

        void ClanWarReady(boolean autocheck);
    }
}
