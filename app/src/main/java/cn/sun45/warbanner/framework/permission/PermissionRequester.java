package cn.sun45.warbanner.framework.permission;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Random;

/**
 * Created by Sun45 on 2019/10/29.
 * 权限申请类
 */
public class PermissionRequester {
    private Activity activity;
    private String[] permissionList;
    private PermissionRequestListener listener;

    //权限请求requestcode
    private int REQUEST_DIALOG_PERMISSION;
    private int PERMISSIONS_REQUEST;

    /**
     * @param activity
     * @param permissionList 需要申请的权限列表
     * @param listener       监听
     */
    public PermissionRequester(Activity activity, String[] permissionList, PermissionRequestListener listener) {
        this.activity = activity;
        this.permissionList = permissionList;
        this.listener = listener;
        REQUEST_DIALOG_PERMISSION = new Random().nextInt(1000);
        PERMISSIONS_REQUEST = new Random().nextInt(1000);
    }

    public PermissionRequester(Activity activity, String[] permissionList) {
        this(activity, permissionList, null);
    }

    /**
     * 权限申请
     */
    public void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (!Settings.canDrawOverlays(activity)) {
//                int sdkInt = Build.VERSION.SDK_INT;
//                if (sdkInt >= Build.VERSION_CODES.O) {//8.0以上
//                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//                    activity.startActivityForResult(intent, REQUEST_DIALOG_PERMISSION);
//                } else {//6.0-8.0
//                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//                    intent.setData(Uri.parse("package:" + activity.getPackageName()));
//                    activity.startActivityForResult(intent, REQUEST_DIALOG_PERMISSION);
//                }
//            }
            if (permissionList != null && permissionList.length > 0) {
                boolean allGranted = true;
                boolean shouldShowRequestPermissionRationale = false;
                for (String neededPermission : permissionList) {
                    boolean granted = ContextCompat.checkSelfPermission(activity, neededPermission) == PackageManager.PERMISSION_GRANTED;
                    allGranted &= granted;
                    if (!granted) {
                        shouldShowRequestPermissionRationale |= ActivityCompat.shouldShowRequestPermissionRationale(activity, neededPermission);
                    }
                }
                if (allGranted) {
                    if (listener != null) {
                        listener.permissionGained();
                    }
                } else {
                    if (shouldShowRequestPermissionRationale) {
                        showRequestPermissionRationale();
                    } else {
                        ActivityCompat.requestPermissions(activity, permissionList, PERMISSIONS_REQUEST);
                    }
                }
            }
        } else {
            if (listener != null) {
                listener.permissionGained();
            }
        }
    }

    /**
     * 注册权限申请回调
     *
     * @param requestCode  申请码
     * @param permissions  申请的权限
     * @param grantResults 结果
     * @return true代表未截获到返回数据，直接调用父类方法
     */
    public boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST) {
            if (grantResults.length > 0) {
                boolean allGranted = true;
                for (int result : grantResults) {
                    allGranted &= result == PackageManager.PERMISSION_GRANTED;
                }
                if (allGranted) {
                    if (listener != null) {
                        listener.permissionGained();
                    }
                } else {
                    boolean shouldShowRequestPermissionRationale = false;
                    for (int i = 0; i < permissions.length; i++) {
                        String neededPermission = permissions[i];
                        if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            shouldShowRequestPermissionRationale |= ActivityCompat.shouldShowRequestPermissionRationale(activity, neededPermission);
                        }
                    }
                    if (shouldShowRequestPermissionRationale) {
                        showRequestPermissionRationale();
                    } else {
                        showGoSettingPermission();
                    }
                }
            }
            return false;
        }
        return true;
    }

    /**
     * 展示请提供权限提示
     */
    protected void showRequestPermissionRationale() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        builder.setTitle("权限提示");
        builder.setMessage("请提供必要的权限，否则功能不能正常使用");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(activity, permissionList, PERMISSIONS_REQUEST);
            }
        });
        builder.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

    /**
     * 展示需要打开系统设置提示
     */
    protected void showGoSettingPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        builder.setTitle("权限提示");
        builder.setMessage("必要的权限无法申请，请在系统设置界面手动打开");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }
}
