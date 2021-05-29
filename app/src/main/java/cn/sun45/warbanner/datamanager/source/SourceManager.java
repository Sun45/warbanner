package cn.sun45.warbanner.datamanager.source;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.document.StaticHelper;
import cn.sun45.warbanner.document.db.source.CharacterModel;
import cn.sun45.warbanner.document.preference.SourcePreference;
import cn.sun45.warbanner.framework.document.db.DbHelper;
import cn.sun45.warbanner.framework.logic.RequestListener;
import cn.sun45.warbanner.logic.source.SourceLogic;
import cn.sun45.warbanner.util.BrotliUtils;
import cn.sun45.warbanner.util.FileUtil;
import cn.sun45.warbanner.util.Utils;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;


/**
 * Created by Sun45 on 2021/5/29
 * 数据源数据获取管理
 */
public class SourceManager {
    private static final String TAG = "SourceManager";

    private Activity activity;

    private Handler handler;

    //单例对象
    private static SourceManager instance;

    public static void init(Activity activity) {
        if (instance == null) {
            synchronized (SourceManager.class) {
                if (instance == null) {
                    instance = new SourceManager(activity);
                }
            }
        }
    }

    public static SourceManager getInstance() {
        return instance;
    }

    public SourceManager(Activity activity) {
        this.activity = activity;
        handler = new Handler(activity.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case DOWNLOAD_PROGRESSCHANGE://更新数据库时进度条变化
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.message(null, String.format("%d / %d KB download.", progress / 1024, maxLength / 1024), null);
                        }
                        break;
                    case DOWNLOAD_COMPLETE://数据库下载完成
                        Log.d(TAG, "DB download finished.");
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.message(R.string.db_update_download_finished_text, null, null);
                        }
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private static final int DOWNLOAD_PROGRESSCHANGE = 0;
    private static final int DOWNLOAD_COMPLETE = 1;

    private IActivityCallBack iActivityCallBack;

    public void setiActivityCallBack(IActivityCallBack iActivityCallBack) {
        this.iActivityCallBack = iActivityCallBack;
    }

    private MaterialDialog progressDialog;

    private long serverVersion;
    private int maxLength;
    private int progress;

    /**
     * 检查数据库文件是否存在
     */
    public boolean checkDbFile() {
        return FileUtil.checkFileAndSize(FileUtil.getDbFilePath(StaticHelper.DB_FILE_NAME), 50);
    }

    /**
     * 获取数据库版本
     */
    public long getDbVersion() {
        return new SourcePreference().getDbVersion();
    }

    /**
     * 检查数据库版本
     *
     * @param autocheck
     */
    public void checkDatabaseVersion(boolean autocheck) {
        new SourceLogic().checkDatabaseVersion(new RequestListener<Long>() {
            @Override
            public void onSuccess(Long result) {
                serverVersion = result;
                if (serverVersion != getDbVersion()) {
                    hintUpdate(autocheck);
                } else {
                    if (iActivityCallBack != null) {
                        if (!autocheck) {
                            iActivityCallBack.showSnackBar(R.string.db_update_check_noneed);
                        }
                        iActivityCallBack.sourceUpdateFinished(false, autocheck);
                    }
                }
            }

            @Override
            public void onFailed(String message) {
                if (iActivityCallBack != null) {
                    iActivityCallBack.showSnackBar(R.string.db_update_failed);
                    iActivityCallBack.sourceUpdateFinished(false, autocheck);
                }
            }
        });
    }

    /***
     * 数据库检查更新完成，弹出更新确认对话框
     */
    private void hintUpdate(boolean autocheck) {
        Utils.logD(TAG, "hintUpdate  serverVersion:" + serverVersion);
        new MaterialDialog(activity, MaterialDialog.getDEFAULT_BEHAVIOR())
                .title(R.string.db_update_dialog_title, null)
                .message(R.string.db_update_dialog_text, null, null)
                .cancelOnTouchOutside(false)
                .positiveButton(R.string.db_update_dialog_confirm, null, new Function1<MaterialDialog, Unit>() {
                    @Override
                    public Unit invoke(MaterialDialog materialDialog) {
                        downloadDB(autocheck);
                        return null;
                    }
                })
                .negativeButton(R.string.db_update_dialog_cancel, null, new Function1<MaterialDialog, Unit>() {
                    @Override
                    public Unit invoke(MaterialDialog materialDialog) {
                        Log.d(TAG, "Canceled download db  serverVersion:" + serverVersion);
                        if (iActivityCallBack != null) {
                            iActivityCallBack.sourceUpdateFinished(false, autocheck);
                        }
                        return null;
                    }
                })
                .show();
    }

    /**
     * 开始数据源下载
     */
    private void downloadDB(boolean autocheck) {
        Log.d(TAG, "downloadDB serverVersion:" + serverVersion);
        progressDialog = new MaterialDialog(activity, MaterialDialog.getDEFAULT_BEHAVIOR());
        progressDialog
                .title(R.string.db_update_progress_title, null)
                .message(R.string.db_update_progress_text, null, null)
                .cancelable(false)
                .show();
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    HttpURLConnection conn = (HttpURLConnection) new URL(StaticHelper.DB_FILE_URL).openConnection();
                    maxLength = conn.getContentLength();
                    InputStream inputStream = conn.getInputStream();
                    if (!new File(FileUtil.getDbFilePath(StaticHelper.DB_FILE_NAME_COMPRESSED)).exists()) {
                        if (!new File(FileUtil.getDbFilePath(StaticHelper.DB_FILE_NAME_COMPRESSED)).mkdirs()) {
                            throw new RuntimeException("Cannot create DB path.");
                        }
                    }
                    File compressedFile = new File(FileUtil.getDbFilePath(StaticHelper.DB_FILE_NAME_COMPRESSED));
                    if (compressedFile.exists()) {
                        FileUtil.deleteFile(compressedFile);
                    }
                    FileOutputStream fileOutputStream = new FileOutputStream(compressedFile);
                    int totalDownload = 0;
                    byte[] buf = new byte[1024 * 1024];
                    int numRead;
                    while (true) {
                        numRead = inputStream.read(buf);
                        totalDownload += numRead;
                        progress = totalDownload;
                        handler.sendEmptyMessage(DOWNLOAD_PROGRESSCHANGE);
                        if (numRead <= 0) {
                            handler.sendEmptyMessage(DOWNLOAD_COMPLETE);
                            break;
                        }
                        fileOutputStream.write(buf, 0, numRead);
                    }
                    inputStream.close();
                    fileOutputStream.close();
                    afterDownLoad(autocheck);
                } catch (IOException e) {
                    e.printStackTrace();
                    if (progressDialog != null) {
                        progressDialog.cancel();
                    }
                    if (iActivityCallBack != null) {
                        iActivityCallBack.showSnackBar(R.string.db_update_failed);
                        iActivityCallBack.sourceUpdateFinished(false, autocheck);
                    }
                }
            }
        }.start();
    }

    /**
     * 下载完成后
     */
    private void afterDownLoad(boolean autocheck) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                //先关闭所有连接，释放sqliteHelper类中的所有旧版本数据库缓存
                SourceDataProcessHelper.getInstance().close();
                DbHelper.delete(activity, CharacterModel.class);
                synchronized (SourceDataProcessHelper.class) {
                    doDecompress(autocheck);
                }
            }
        }.start();
    }

    /**
     * 解压数据源数据
     */
    public void doDecompress(boolean autocheck) {
        FileUtil.deleteFile(FileUtil.getDbFilePath(StaticHelper.DB_FILE_NAME));
        Log.d(TAG, "Start decompress DB.");
        try {
            BrotliUtils.deCompress(FileUtil.getDbFilePath(StaticHelper.DB_FILE_NAME_COMPRESSED), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        dbUpdateCompleted(autocheck);
    }

    /***
     * 数据库更新整个流程结束
     */
    private void dbUpdateCompleted(boolean autocheck) {
        Log.d(TAG, "DB update finished.");
        String newFileHash = FileUtil.getFileMD5ToString(FileUtil.getDbFilePath(StaticHelper.DB_FILE_NAME));
        if (new SourcePreference().getDbHash() == newFileHash) {
            Log.d(TAG, "duplicate DB file.");
        }
        new SourcePreference().setDbHash(newFileHash);
        new SourcePreference().setDbVersion(serverVersion);
        if (iActivityCallBack != null) {
            iActivityCallBack.showSnackBar(R.string.db_update_finished_text);
            iActivityCallBack.sourceUpdateFinished(true, autocheck);
        }
        if (progressDialog != null) {
            progressDialog.cancel();
        }
    }

    public interface IActivityCallBack {
        void showSnackBar(@StringRes int messageRes);

        void sourceUpdateFinished(boolean dataGain, boolean autocheck);
    }
}
