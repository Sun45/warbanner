package cn.sun45.warbanner.datamanager.update;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.FileProvider;

import com.afollestad.materialdialogs.MaterialDialog;
import com.liulishuo.okdownload.DownloadTask;

import java.io.File;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.document.statics.StaticHelper;
import cn.sun45.warbanner.document.preference.SetupPreference;
import cn.sun45.warbanner.framework.MyApplication;
import cn.sun45.warbanner.framework.file.FileRequestListenerWithProgress;
import cn.sun45.warbanner.framework.file.FileRequester;
import cn.sun45.warbanner.framework.logic.RequestListener;
import cn.sun45.warbanner.logic.app.AppLogic;
import cn.sun45.warbanner.logic.app.AppModel;
import cn.sun45.warbanner.util.FileUtil;
import cn.sun45.warbanner.util.GithubUtils;
import cn.sun45.warbanner.util.Utils;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * Created by Sun45 on 2021/5/29
 * 更新管理
 */
public class UpdateManager {
    private static final String TAG = "UpdateManager";

    //单例对象
    private static UpdateManager instance;

    public static UpdateManager getInstance() {
        if (instance == null) {
            synchronized (UpdateManager.class) {
                if (instance == null) {
                    instance = new UpdateManager();
                }
            }
        }
        return instance;
    }

    private Handler handler;

    private static final int DOWNLOAD_PROGRESSCHANGE = 0;
    private static final int DOWNLOAD_COMPLETE = 1;
    private static final int DOWNLOAD_ERROR = 2;

    private static final int UPDATE_REQUESTCODE = 5000;

    private IActivityCallBack iActivityCallBack;

    public void setiActivityCallBack(IActivityCallBack iActivityCallBack) {
        this.iActivityCallBack = iActivityCallBack;
    }

    private MaterialDialog progressDialog;

    private long currentOffset;
    private long totalLength;

    /**
     * 检查app版本
     *
     * @param autocheck 自动检查
     */
    public void checkAppVersion(boolean autocheck) {
        Utils.logD(TAG, "checkAppVersion autocheck:" + autocheck);
        if (autocheck && !new SetupPreference().isAutoupdate()) {
            Utils.logD(TAG, "自动更新未开启");
            return;
        }
        handler = new Handler(MyApplication.getCurrentActivity().getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case DOWNLOAD_PROGRESSCHANGE:
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.message(null, Utils.getStringWithPlaceHolder(R.string.app_update_progress_text, currentOffset, totalLength), null);
                        }
                        break;
                    case DOWNLOAD_COMPLETE:
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.message(R.string.app_update_progress_finished, null, null);
                        }
                        break;
                    case DOWNLOAD_ERROR:
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.cancel();
                            if (iActivityCallBack != null) {
                                iActivityCallBack.showSnackBar(R.string.app_update_progress_error);
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        new AppLogic().checkAppVersion(new RequestListener<AppModel>() {
            @Override
            public void onSuccess(AppModel result) {
                Utils.logD(TAG, result.toString());
                //有版本更新
                if (result.getVersionCode() > Utils.getVersionCode()) {
                    showConfirmDialog(result, autocheck);
                } else {
                    if (iActivityCallBack != null) {
                        if (autocheck) {
                            updateInterrupt(autocheck);
                        } else {
                            iActivityCallBack.showSnackBar(R.string.app_update_check_noneed);
                        }
                    }
                }
            }

            @Override
            public void onFailed(String message) {
                Utils.logD(TAG, "checkAppVersion onFailed message:" + message);
                if (iActivityCallBack != null) {
                    if (!autocheck) {
                        iActivityCallBack.showSnackBar(R.string.app_update_check_fail);
                    }
                    updateInterrupt(autocheck);
                }
            }
        });
    }

    /***
     * 弹出更新确认下载数据对话框
     */
    private void showConfirmDialog(AppModel appModel, boolean autocheck) {
        Utils.logD(TAG, "showConfirmDialog");
        new MaterialDialog(MyApplication.getCurrentActivity(), MaterialDialog.getDEFAULT_BEHAVIOR())
                .title(null, Utils.getString(R.string.app_name) + " " + appModel.getVersionName())
                .message(null, appModel.getContent(), null)
                .cancelOnTouchOutside(false)
                .positiveButton(R.string.app_update_dialog_confirm, null, new Function1<MaterialDialog, Unit>() {
                    @Override
                    public Unit invoke(MaterialDialog materialDialog) {
                        download(autocheck);
                        return null;
                    }
                })
                .negativeButton(R.string.app_update_dialog_cancel, null, new Function1<MaterialDialog, Unit>() {
                    @Override
                    public Unit invoke(MaterialDialog materialDialog) {
                        Log.d(TAG, "Canceled");
                        updateInterrupt(autocheck);
                        return null;
                    }
                })
                .show();
    }

    /**
     * 文件下载
     */
    private void download(boolean autocheck) {
        Utils.logD(TAG, "download");
        progressDialog = new MaterialDialog(MyApplication.getCurrentActivity(), MaterialDialog.getDEFAULT_BEHAVIOR());
        progressDialog.title(R.string.app_update_progress_title, null)
                .message(R.string.app_update_progress_text_prepare, null, null)
                .cancelable(false)
                .show();
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                updateInterrupt(autocheck);
            }
        });
        String path = FileUtil.getExternalFilesDir("update");
        String name = StaticHelper.APK_NAME;
        new File(path + File.separator + name).deleteOnExit();
        String fileUrl = GithubUtils.getFileUrl(GithubUtils.TYPE_RAW, StaticHelper.APK_OWNER, StaticHelper.APK_REPOSITORY, StaticHelper.APK_BRANCH, StaticHelper.APK_PATH);
        FileRequester.request(fileUrl, path, name, true, new FileRequestListenerWithProgress() {
            @Override
            public void progress(long currentOffset, long totalLength, float percent, int progress) {
                UpdateManager.this.currentOffset = currentOffset;
                UpdateManager.this.totalLength = totalLength;
                handler.sendEmptyMessage(DOWNLOAD_PROGRESSCHANGE);
            }

            @Override
            public void start() {
                Utils.logD(TAG, "start");
            }

            @Override
            public void complete(String url, String path, String name) {
                Utils.logD(TAG, "complete");
                handler.sendEmptyMessage(DOWNLOAD_COMPLETE);
                install(path, name);
            }

            @Override
            public void error(String url, String path, String name, String msg) {
                Utils.logD(TAG, "error name:" + name + " msg:" + msg);
                handler.sendEmptyMessage(DOWNLOAD_ERROR);
            }

            @Override
            public void duplicate(String url, String path, String name) {
            }

            @Override
            public void end(DownloadTask task) {
            }
        });
    }

    /**
     * 安装
     */
    private void install(String path, String name) {
        Utils.logD(TAG, "install");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File file = new File(path + File.separator + name);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(MyApplication.application, "cn.sun45.warbanner.fileprovider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.putExtra("return-data", false);
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        MyApplication.getCurrentActivity().startActivityForResult(intent, UPDATE_REQUESTCODE);
    }

    /**
     * 升级中止
     */
    private void updateInterrupt(boolean autocheck) {
        Utils.logD(TAG, "updateInterrupt");
        if (iActivityCallBack != null) {
            iActivityCallBack.updateInterrupt(autocheck);
        }
    }

    public interface IActivityCallBack {
        void showSnackBar(@StringRes int messageRes);

        void updateInterrupt(boolean autocheck);
    }
}
