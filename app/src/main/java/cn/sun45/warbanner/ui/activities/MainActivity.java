package cn.sun45.warbanner.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.datamanager.clanwar.ClanWarManager;
import cn.sun45.warbanner.datamanager.nickname.NickNameManager;
import cn.sun45.warbanner.datamanager.source.RawUnitBasic;
import cn.sun45.warbanner.datamanager.source.SourceDataProcessHelper;
import cn.sun45.warbanner.datamanager.source.SourceManager;
import cn.sun45.warbanner.datamanager.update.UpdateManager;
import cn.sun45.warbanner.document.db.clanwar.TeamModel;
import cn.sun45.warbanner.document.db.source.CharacterModel;
import cn.sun45.warbanner.document.preference.AppPreference;
import cn.sun45.warbanner.framework.document.db.DbHelper;
import cn.sun45.warbanner.framework.permission.PermissionRequestListener;
import cn.sun45.warbanner.framework.permission.PermissionRequester;
import cn.sun45.warbanner.framework.ui.BaseActivity;
import cn.sun45.warbanner.ui.shared.SharedViewModelCharacterModelList;
import cn.sun45.warbanner.ui.shared.SharedViewModelSetup;
import cn.sun45.warbanner.ui.shared.SharedViewModelTeamList;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2021/5/19
 * 首页界面
 */
public class MainActivity extends BaseActivity implements PermissionRequestListener, SharedViewModelCharacterModelList.MasterCharaCallBack, UpdateManager.IActivityCallBack, SourceManager.IActivityCallBack, ClanWarManager.IActivityCallBack {
    private PermissionRequester permissionRequester;

    private SharedViewModelCharacterModelList sharedCharacterModelList;
    private SharedViewModelTeamList sharedTeamModelList;
    private SharedViewModelSetup sharedSetup;

    private SourceManager sourceManager;
    private ClanWarManager clanWarManager;
    private UpdateManager updateManager;

    private View mRoot;

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
        sharedCharacterModelList = new ViewModelProvider(this).get(SharedViewModelCharacterModelList.class);
        sharedCharacterModelList.characterlist.observe(this, new Observer<List<CharacterModel>>() {
            @Override
            public void onChanged(List<CharacterModel> characterModels) {
//                for (CharacterModel character : characterModels) {
//                    logD(character.getName());
//                }
            }
        });
        sharedCharacterModelList.setCallBack(this);
        sharedTeamModelList = new ViewModelProvider(this).get(SharedViewModelTeamList.class);
        sharedSetup = new ViewModelProvider(this).get(SharedViewModelSetup.class);
        SourceManager.init(this);
        sourceManager = SourceManager.getInstance();
        sourceManager.setiActivityCallBack(this);
        ClanWarManager.init(this);
        clanWarManager = ClanWarManager.getInstance();
        clanWarManager.setiActivityCallBack(this);
        UpdateManager.init(this);
        updateManager = UpdateManager.getInstance();
        updateManager.setiActivityCallBack(this);

//        NickNameManager.init(this);
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
        sharedSetup.loadData();
        sourceLoad();
    }

    private void sourceLoad() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (sourceManager.checkDbFile()) {
                    sharedCharacterModelList.loadData();
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
                    sharedTeamModelList.loadData();
                    checkAppVersion();
                }
            }
        });
    }

    private void checkAppVersion() {
        updateManager.checkAppVersion(true);
    }

    @Override
    public void charaLoadFinished(boolean succeeded) {
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
            sharedCharacterModelList.clearData();
            List<RawUnitBasic> list = SourceDataProcessHelper.getInstance().getCharaBase();
            for (RawUnitBasic rawUnitBasic : list) {
                CharacterModel character = new CharacterModel();
                rawUnitBasic.set(character);
                DbHelper.insert(this, character);
            }
            sharedCharacterModelList.loadData();
        }
        if (autocheck) {
            clanWarLoad();
        }
    }

    @Override
    public void ClanWarReady(boolean autocheck) {
        sharedTeamModelList.loadData();
        if (autocheck) {
            checkAppVersion();
        }
    }

    @Override
    public void updateInterrupt(boolean autocheck) {
    }
}