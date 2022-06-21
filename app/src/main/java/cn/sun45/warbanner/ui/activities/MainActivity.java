package cn.sun45.warbanner.ui.activities;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.datamanager.data.DataManager;
import cn.sun45.warbanner.datamanager.update.UpdateManager;
import cn.sun45.warbanner.document.database.setup.SetupDataBase;
import cn.sun45.warbanner.document.database.setup.models.UserModel;
import cn.sun45.warbanner.document.database.source.models.CharacterModel;
import cn.sun45.warbanner.document.preference.AppPreference;
import cn.sun45.warbanner.document.preference.UserPreference;
import cn.sun45.warbanner.framework.permission.PermissionRequestListener;
import cn.sun45.warbanner.framework.permission.PermissionRequester;
import cn.sun45.warbanner.framework.ui.BaseActivity;
import cn.sun45.warbanner.server.ServerManager;
import cn.sun45.warbanner.ui.shared.SharedViewModelClanwar;
import cn.sun45.warbanner.ui.shared.SharedViewModelSource;
import cn.sun45.warbanner.user.UserManager;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2021/5/19
 * 首页界面
 */
public class MainActivity extends BaseActivity implements PermissionRequestListener, ServerManager.IActivityCallBack, UpdateManager.IActivityCallBack, DataManager.IActivityCallBack {
    private PermissionRequester permissionRequester;

    private SharedViewModelSource sharedSource;
    private SharedViewModelClanwar sharedClanwar;

    private ServerManager serverManager;
    private DataManager dataManager;
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
                }, this);
        sharedSource = new ViewModelProvider(this).get(SharedViewModelSource.class);
        sharedSource.characterList.observe(this, new Observer<List<CharacterModel>>() {
            @Override
            public void onChanged(List<CharacterModel> characterModels) {
//                for (CharacterModel character : characterModels) {
//                    logD(character.getName());
//                }
            }
        });
        sharedClanwar = new ViewModelProvider(this).get(SharedViewModelClanwar.class);
        serverManager = ServerManager.getInstance();
        serverManager.setiActivityCallBack(this);
        dataManager = DataManager.getInstance();
        dataManager.setiActivityCallBack(this);
        updateManager = UpdateManager.getInstance();
        updateManager.setiActivityCallBack(this);

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

    UserModel userModel;

    @Override
    public void permissionGained() {
        dataLoad();
    }

    private void dataLoad() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dataManager.checkEmpty()) {
                    dataManager.update();
                } else {
                    dataUpdateFinished(true);
                }
            }
        });
    }

    private void checkAppVersion() {
        updateManager.checkAppVersion(true);
    }

    @Override
    public void showSnackBar(int messageRes) {
        Utils.tip(mRoot, messageRes);
    }

    @Override
    public void serverUpdate() {
        UserManager.getInstance().resetToDefaultUser();
        dataManager.update();
    }

    @Override
    public void dataUpdateFinished(boolean needload) {
        if (needload) {
            sharedSource.clearData();
            sharedSource.loadData();
            sharedClanwar.loadData();
        }
        checkAppVersion();
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