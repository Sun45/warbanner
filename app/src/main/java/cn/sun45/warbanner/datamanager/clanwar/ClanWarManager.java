package cn.sun45.warbanner.datamanager.clanwar;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.StringRes;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.clanwar.ClanwarHelper;
import cn.sun45.warbanner.document.db.clanwar.TeamModel;
import cn.sun45.warbanner.document.preference.ClanwarPreference;
import cn.sun45.warbanner.framework.MyApplication;
import cn.sun45.warbanner.framework.document.db.DbHelper;
import cn.sun45.warbanner.framework.logic.RequestListener;
import cn.sun45.warbanner.logic.clanwar.ClanwarLogic;
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

    //单例对象
    private static ClanWarManager instance;

    public static ClanWarManager getInstance() {
        if (instance == null) {
            synchronized (ClanWarManager.class) {
                if (instance == null) {
                    instance = new ClanWarManager();
                }
            }
        }
        return instance;
    }

    private IActivityCallBack iActivityCallBack;

    public void setiActivityCallBack(IActivityCallBack iActivityCallBack) {
        this.iActivityCallBack = iActivityCallBack;
    }

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

//    /***
//     * 弹出更新确认下载数据对话框
//     *
//     * @param autocheck 自动检查
//     */
//    public void showConfirmDialog(boolean autocheck) {
//        Utils.logD(TAG, "showConfirmDialog");
//        new MaterialDialog(MyApplication.getCurrentActivity(), MaterialDialog.getDEFAULT_BEHAVIOR())
//                .title(R.string.clanwar_update_dialog_title, null)
//                .message(R.string.clanwar_update_dialog_text, null, null)
//                .cancelOnTouchOutside(false)
//                .positiveButton(R.string.clanwar_update_dialog_confirm, null, new Function1<MaterialDialog, Unit>() {
//                    @Override
//                    public Unit invoke(MaterialDialog materialDialog) {
//                        downloadData(autocheck);
//                        return null;
//                    }
//                })
//                .negativeButton(R.string.clanwar_update_dialog_cancel, null, new Function1<MaterialDialog, Unit>() {
//                    @Override
//                    public Unit invoke(MaterialDialog materialDialog) {
//                        Log.d(TAG, "Canceled");
//                        if (iActivityCallBack != null) {
//                            iActivityCallBack.ClanWarReady(autocheck);
//                        }
//                        return null;
//                    }
//                })
//                .show();
//    }
//
//    /**
//     * 下载作业数据
//     *
//     * @param autocheck 自动检查
//     */
//    private void downloadData(boolean autocheck) {
//        Utils.logD(TAG, "downloadData");
//        String date = ClanwarHelper.getCurrentClanWarDate();
//        if (TextUtils.isEmpty(date)) {
//            if (iActivityCallBack != null) {
//                iActivityCallBack.showSnackBar(R.string.clanwar_update_clanwar_empty);
//                iActivityCallBack.ClanWarReady(autocheck);
//            }
//        } else {
//            MaterialDialog progressDialog = new MaterialDialog(MyApplication.getCurrentActivity(), MaterialDialog.getDEFAULT_BEHAVIOR());
//            progressDialog.title(R.string.clanwar_update_progress_title, null)
//                    .message(R.string.clanwar_update_progress_text_one, null, null)
//                    .cancelable(false)
//                    .show();
//            new ClanwarPreference().setLastupdate(System.currentTimeMillis());
//            loadDataAndSave(date, 1, () -> {
//                progressDialog.message(R.string.clanwar_update_progress_text_two, null, null);
//                loadDataAndSave(date, 2, () -> {
//                    progressDialog.message(R.string.clanwar_update_progress_text_three, null, null);
//                    loadDataAndSave(date, 3, () -> {
//                        progressDialog.message(R.string.clanwar_update_progress_text_four, null, null);
//                        loadDataAndSave(date, 4, () -> {
//                            if (iActivityCallBack != null) {
//                                iActivityCallBack.showSnackBar(R.string.clanwar_update_finished_text);
//                                iActivityCallBack.ClanWarReady(autocheck);
//                            }
//                            if (progressDialog != null) {
//                                progressDialog.cancel();
//                            }
//                        }, 0);
//                    }, 0);
//                }, 0);
//            }, 0);
//        }
//    }
//
//    /**
//     * 读取数据并存库
//     *
//     * @param date     会战日期 202107
//     * @param stage    阶段 1,2,3,4
//     * @param listener 数据请求监听
//     * @param retry    重试次数
//     */
//    private void loadDataAndSave(String date, int stage, ClanWarRequestListener listener, int retry) {
//        Utils.logD(TAG, "loadDataAndSave date:" + date + " stage:" + stage + " retry:" + retry);
//        new ClanwarLogic().getTeamModelList(date, stage, new RequestListener<List<TeamModel>>() {
//            @Override
//            public void onSuccess(List<TeamModel> result) {
//                if (result != null && !result.isEmpty()) {
//                    DbHelper.delete(MyApplication.application, TeamModel.class, new String[]{"date", "stage"}, new String[]{date, stage + ""});
//                    for (TeamModel teamModel : result) {
//                        DbHelper.insert(MyApplication.application, teamModel);
//                    }
//                    listener.loadFinish();
//                } else {
//                    onFailed("");
//                }
//            }
//
//            @Override
//            public void onFailed(String message) {
//                if (retry < 2) {
//                    loadDataAndSave(date, stage, listener, retry + 1);
//                } else {
//                    listener.loadFinish();
//                }
//            }
//        });
//    }

    /***
     * 弹出更新确认下载数据对话框
     *
     * @param autocheck 自动检查
     */
    public void showConfirmDialogCaimogu(boolean autocheck) {
        Utils.logD(TAG, "showConfirmDialogCaimogu");
        new MaterialDialog(MyApplication.getCurrentActivity(), MaterialDialog.getDEFAULT_BEHAVIOR())
                .title(R.string.clanwar_update_dialog_title, null)
                .message(R.string.clanwar_update_dialog_text, null, null)
                .cancelOnTouchOutside(false)
                .positiveButton(R.string.clanwar_update_dialog_confirm, null, new Function1<MaterialDialog, Unit>() {
                    @Override
                    public Unit invoke(MaterialDialog materialDialog) {
                        downloadDataCaimogu(autocheck);
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
     * 下载作业数据
     *
     * @param autocheck 自动检查
     */
    private void downloadDataCaimogu(boolean autocheck) {
        Utils.logD(TAG, "downloadDataCaimogu");
        String date = ClanwarHelper.getCurrentClanWarDate();
        if (TextUtils.isEmpty(date)) {
            if (iActivityCallBack != null) {
                iActivityCallBack.showSnackBar(R.string.clanwar_update_clanwar_empty);
                iActivityCallBack.ClanWarReady(autocheck);
            }
        } else {
            MaterialDialog progressDialog = new MaterialDialog(MyApplication.getCurrentActivity(), MaterialDialog.getDEFAULT_BEHAVIOR());
            progressDialog.title(R.string.clanwar_update_progress_title, null)
                    .message(R.string.clanwar_update_progress_text, null, null)
                    .cancelable(false)
                    .show();
            new ClanwarPreference().setLastupdate(System.currentTimeMillis());
            loadDataAndSaveCaimogu(date, new ClanWarRequestListener() {
                @Override
                public void loadFinish(String repeatId) {
                    if (iActivityCallBack != null) {
                        if (TextUtils.isEmpty(repeatId)) {
                            iActivityCallBack.showSnackBar(R.string.clanwar_update_finished_text);
                        } else {
                            iActivityCallBack.showSnackBar(Utils.getStringWithPlaceHolder(R.string.clanwar_update_finished_winth_duplicate_id, repeatId));
                        }
                        iActivityCallBack.ClanWarReady(autocheck);
                    }
                    if (progressDialog != null) {
                        progressDialog.cancel();
                    }
                }

                @Override
                public void loadFail() {
                    if (iActivityCallBack != null) {
                        iActivityCallBack.showSnackBar(R.string.clanwar_update_fail);
                        iActivityCallBack.ClanWarReady(autocheck);
                    }
                    if (progressDialog != null) {
                        progressDialog.cancel();
                    }
                }
            }, 0);
        }
    }

    /**
     * 读取数据并存库
     *
     * @param date     会战日期 202107
     * @param listener 数据请求监听
     * @param retry    重试次数
     */
    private void loadDataAndSaveCaimogu(String date, ClanWarRequestListener listener, int retry) {
        Utils.logD(TAG, "loadDataAndSaveCaimogu retry:" + retry);
        new ClanwarLogic().getCaimoguData(new RequestListener<List<TeamModel>>() {
            @Override
            public void onSuccess(List<TeamModel> result) {
                if (result != null && !result.isEmpty()) {
                    DbHelper.delete(MyApplication.application, TeamModel.class, new String[]{"date"}, new String[]{date});
                    List<String> idList = new ArrayList<>();
                    List<String> repeatIdList = new ArrayList<>();
                    for (TeamModel teamModel : result) {
                        String id = teamModel.getNumber();
                        if (idList.contains(id)) {
                            if (!repeatIdList.contains(id)) {
                                repeatIdList.add(id);
                            }
                        } else {
                            idList.add(id);
                        }
                        teamModel.setDate(date);
                        DbHelper.insert(MyApplication.application, teamModel);
                    }
                    String repeatId = null;
                    if (!repeatIdList.isEmpty()) {
                        StringBuilder sb = new StringBuilder();
                        for (String id : repeatIdList) {
                            sb.append(id + " ");
                        }
                        repeatId = sb.toString();
                    }
                    listener.loadFinish(repeatId);
                } else {
                    onFailed("");
                }
            }

            @Override
            public void onFailed(String message) {
                if (retry < 2) {
                    loadDataAndSaveCaimogu(date, listener, retry + 1);
                } else {
                    listener.loadFail();
                }
            }
        });
    }


    public interface ClanWarRequestListener {
        void loadFinish(String repeatId);

        void loadFail();
    }

    public interface IActivityCallBack {
        void showSnackBar(@StringRes int messageRes);

        void showSnackBar(String message);

        void ClanWarReady(boolean autocheck);
    }
}
