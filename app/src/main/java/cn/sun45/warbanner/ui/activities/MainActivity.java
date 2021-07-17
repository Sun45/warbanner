package cn.sun45.warbanner.ui.activities;

import android.Manifest;
import android.content.SharedPreferences;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.datamanager.clanwar.ClanWarManager;
import cn.sun45.warbanner.datamanager.source.SourceManager;
import cn.sun45.warbanner.datamanager.update.UpdateManager;
import cn.sun45.warbanner.document.db.source.CharacterModel;
import cn.sun45.warbanner.document.preference.AppPreference;
import cn.sun45.warbanner.document.preference.UserPreference;
import cn.sun45.warbanner.framework.permission.PermissionRequestListener;
import cn.sun45.warbanner.framework.permission.PermissionRequester;
import cn.sun45.warbanner.framework.ui.BaseActivity;
import cn.sun45.warbanner.ui.shared.SharedViewModelClanwar;
import cn.sun45.warbanner.ui.shared.SharedViewModelSource;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2021/5/19
 * 首页界面
 */
public class MainActivity extends BaseActivity implements PermissionRequestListener, SharedViewModelSource.CallBack, UpdateManager.IActivityCallBack, SourceManager.IActivityCallBack, ClanWarManager.IActivityCallBack {
    private PermissionRequester permissionRequester;

    private SharedViewModelSource sharedSource;
    private SharedViewModelClanwar sharedClanwar;

    private SourceManager sourceManager;
    private ClanWarManager clanWarManager;
    private UpdateManager updateManager;

    private View mRoot;

    private SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals("userid")) {
                sharedClanwar.loadData();
            }
        }
    };

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        permissionRequester = new PermissionRequester(this,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, this);
        sharedSource = new ViewModelProvider(this).get(SharedViewModelSource.class);
        sharedSource.characterlist.observe(this, new Observer<List<CharacterModel>>() {
            @Override
            public void onChanged(List<CharacterModel> characterModels) {
//                for (CharacterModel character : characterModels) {
//                    logD(character.getName());
//                }
            }
        });
        sharedSource.setCallBack(this);
        sharedClanwar = new ViewModelProvider(this).get(SharedViewModelClanwar.class);
        sourceManager = SourceManager.getInstance();
        sourceManager.setiActivityCallBack(this);
        clanWarManager = ClanWarManager.getInstance();
        clanWarManager.setiActivityCallBack(this);
        updateManager = UpdateManager.getInstance();
        updateManager.setiActivityCallBack(this);

//        NickNameManager.init(this);
        new UserPreference().registListener(onSharedPreferenceChangeListener);
    }

    @Override
    protected void initView() {
        mRoot = findViewById(R.id.root);
        if (new AppPreference().isAbnormal_exit()) {
            Utils.tip(mRoot, R.string.abnormal_exit_message);
            new AppPreference().setAbnormal_exit(false);
        }
//        NavController controller = Navigation.findNavController(this, R.id.nav_host_fragment);
//        controller.setGraph(R.navigation.app_navigation);
//        controller.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
//            @Override
//            public void onDestinationChanged(@NonNull @NotNull NavController controller, @NonNull @NotNull NavDestination destination, @Nullable @org.jetbrains.annotations.Nullable Bundle arguments) {
//            }
//        });
    }

    @Override
    protected void initNet() {
        permissionRequester.requestPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionRequester.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void permissionGained() {
        sourceLoad();
    }

    private void sourceLoad() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (sourceManager.checkDbFile()) {
                    sharedSource.loadData();
                    clanWarLoad();
                } else {
                    sourceManager.checkDatabaseVersion(true);
                }
            }
        });
    }

    private void clanWarLoad() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (clanWarManager.needDataDownload()) {
                    clanWarManager.showConfirmDialog(true);
                } else {
                    sharedClanwar.loadData();
                    checkAppVersion();
                }
            }
        });
    }

    private void checkAppVersion() {
        updateManager.checkAppVersion(true);
    }

    @Override
    public void sourceLoadFinished(boolean succeeded) {
        if (!succeeded) {
            Utils.tip(mRoot, R.string.chara_load_failed);
        }
    }

    @Override
    public void showSnackBar(int messageRes) {
        Utils.tip(mRoot, messageRes);
    }

    @Override
    public void sourceUpdateFinished(boolean dataGain, boolean autocheck) {
        if (dataGain) {
            sharedSource.clearData();
            sharedSource.loadData();
        }
        if (autocheck) {
            clanWarLoad();
        } else {
            sharedClanwar.loadData();
        }
    }

    @Override
    public void ClanWarReady(boolean autocheck) {
        sharedClanwar.loadData();
        if (autocheck) {
            checkAppVersion();
        }
    }

    @Override
    public void updateInterrupt(boolean autocheck) {
//        JSONArray array=new JSONArray();
//        List<TeamModel> list = DbHelper.query(this, TeamModel.class);
//        for (TeamModel teamModel:list) {
//            array.put(teamModel.getJson());
//        }
//        String str=array.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new UserPreference().unregistListener(onSharedPreferenceChangeListener);
    }
}