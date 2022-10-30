package cn.sun45.warbanner.datamanager.data;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.document.database.source.SourceDataBase;
import cn.sun45.warbanner.document.database.source.models.BossModel;
import cn.sun45.warbanner.document.database.source.models.CharacterModel;
import cn.sun45.warbanner.document.database.source.models.TeamModel;
import cn.sun45.warbanner.document.preference.DataPreference;
import cn.sun45.warbanner.framework.MyApplication;
import cn.sun45.warbanner.framework.logic.RequestListener;
import cn.sun45.warbanner.framework.record.ErrorRecordManager;
import cn.sun45.warbanner.logic.caimogu.CaimoguBaseData;
import cn.sun45.warbanner.logic.caimogu.CaimoguLogic;
import cn.sun45.warbanner.server.ServerManager;
import cn.sun45.warbanner.util.Utils;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * Created by Sun45 on 2022/6/10
 * 数据获取管理
 */
public class DataManager {
    private static final String TAG = "SourceManager";

    //单例对象
    private static DataManager instance;

    public static DataManager getInstance() {
        if (instance == null) {
            synchronized (DataManager.class) {
                if (instance == null) {
                    instance = new DataManager();
                }
            }
        }
        return instance;
    }

    private Handler handler;

    private static final int DOWNLOAD_UPDATE = 0;

    private IActivityCallBack iActivityCallBack;

    public void setiActivityCallBack(IActivityCallBack iActivityCallBack) {
        this.iActivityCallBack = iActivityCallBack;
    }

    /**
     * 获取更新信息
     */
    public String getUpdateInfo() {
        long lastupdate = new DataPreference().getLastupdate();
        String updateInfo;
        if (lastupdate == 0) {
            updateInfo = Utils.getString(R.string.data_update_last_empty);
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
            updateInfo = Utils.getStringWithPlaceHolder(R.string.data_update_last, timestr);
        }
        return updateInfo;
    }

    /**
     * 检查数据库文件是否存在
     */
    public boolean checkEmpty() {
        return new DataPreference().getLastupdate() == 0;
    }

    /***
     * 弹出更新确认下载数据对话框
     *
     */
    public void showConfirmDialogCaimogu() {
        Utils.logD(TAG, "showConfirmDialogCaimogu");
        new MaterialDialog(MyApplication.getCurrentActivity(), MaterialDialog.getDEFAULT_BEHAVIOR())
                .title(R.string.data_update_dialog_title, null)
                .message(R.string.data_update_dialog_text, null, null)
                .cancelOnTouchOutside(false)
                .positiveButton(R.string.data_update_dialog_confirm, null, new Function1<MaterialDialog, Unit>() {
                    @Override
                    public Unit invoke(MaterialDialog materialDialog) {
                        update();
                        return null;
                    }
                })
                .negativeButton(R.string.data_update_dialog_cancel, null, new Function1<MaterialDialog, Unit>() {
                    @Override
                    public Unit invoke(MaterialDialog materialDialog) {
                        Log.d(TAG, "Canceled");
                        return null;
                    }
                })
                .show();
    }

    /**
     * 更新内容
     */
    public void update() {
        new DataPreference().setLastupdate(System.currentTimeMillis());
        handler = new Handler(MyApplication.getCurrentActivity().getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case DOWNLOAD_UPDATE://更新时内容变化
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.message(null, (String) msg.obj, null);
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        progressDialog = new MaterialDialog(MyApplication.getCurrentActivity(), MaterialDialog.getDEFAULT_BEHAVIOR());
        progressDialog
                .title(R.string.data_update_dialog_title, null)
                .message(R.string.data_update_basedata, null, null)
                .cancelable(false)
                .show();
        new CaimoguLogic().getBaseData(new RequestListener<CaimoguBaseData>() {
            @Override
            public void onSuccess(CaimoguBaseData result) {
                if (result == null || result.isEmpty()) {
                    updateFail("getBaseData result is empty");
                    return;
                }
                List<CharacterModel> characterModels = result.getCharacterModels();
                List<BossModel> bossModels = result.getBossModels();
                SourceDataBase.getInstance().sourceDao().deleteAllCharacter(ServerManager.getInstance().getLang());
                SourceDataBase.getInstance().sourceDao().insertCharacter(characterModels);
                SourceDataBase.getInstance().sourceDao().deleteAllBoss(ServerManager.getInstance().getLang());
                SourceDataBase.getInstance().sourceDao().insertBoss(bossModels);
                Message message = new Message();
                message.what = DOWNLOAD_UPDATE;
                message.obj = Utils.getString(R.string.data_update_teamdata);
                handler.sendMessage(message);
                new CaimoguLogic().getTeamData(new RequestListener<List<TeamModel>>() {
                    @Override
                    public void onSuccess(List<TeamModel> result) {
                        if (result == null || result.isEmpty()) {
                            updateFail("getTeamData result is empty");
                            return;
                        }
                        SourceDataBase.getInstance().sourceDao().deleteAllTeam(ServerManager.getInstance().getLang());
                        SourceDataBase.getInstance().sourceDao().insertTeam(result);
                        if (iActivityCallBack != null) {
                            iActivityCallBack.showSnackBar(R.string.data_update_finish);
                            iActivityCallBack.dataUpdateFinished(true);
                        }
                        if (progressDialog != null) {
                            progressDialog.cancel();
                        }
                    }

                    @Override
                    public void onFailed(String message) {
                        updateFail("getTeamData onFailed: " + message);
                    }
                }, null, ServerManager.getInstance().getLang());
            }

            @Override
            public void onFailed(String message) {
                updateFail("getBaseData onFailed: " + message);
            }
        }, null, ServerManager.getInstance().getLang());
    }

    /**
     * 更新失败
     */
    private void updateFail(String message) {
        if (iActivityCallBack != null) {
            ErrorRecordManager.getInstance().save(message);
            iActivityCallBack.showSnackBar(R.string.data_update_failed);
            iActivityCallBack.dataUpdateFinished(true);
        }
        if (progressDialog != null) {
            progressDialog.cancel();
        }
    }


    private MaterialDialog progressDialog;

    public interface IActivityCallBack {
        void showSnackBar(@StringRes int messageRes);

        void dataUpdateFinished(boolean needload);
    }
}
