package cn.sun45.warbanner.ui.fragments.menu;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.list.DialogSingleChoiceExtKt;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.datamanager.data.DataManager;
import cn.sun45.warbanner.datamanager.update.UpdateManager;
import cn.sun45.warbanner.document.preference.DataPreference;
import cn.sun45.warbanner.document.preference.SetupPreference;
import cn.sun45.warbanner.document.preference.UserPreference;
import cn.sun45.warbanner.document.statics.StaticHelper;
import cn.sun45.warbanner.server.ServerManager;
import cn.sun45.warbanner.user.UserManager;
import cn.sun45.warbanner.util.Utils;
import kotlin.Unit;
import kotlin.jvm.functions.Function3;

/**
 * Created by Sun45 on 2021/5/29
 * 菜单PreferenceFragment
 */
public class MenuPreferecefragment extends PreferenceFragmentCompat {
    private static final String TAG = "MenuPreferecefragment";

    //数据
    private Preference update;

    private Preference boss;

    //设置
    private Preference user;
    private Preference characterScreen;
    private SwitchPreferenceCompat characterScreenEnable;
    private Preference link;

    //系统
    private Preference server;
    private Preference app;
    private SwitchPreferenceCompat autoUpdate;

    //其它
    private Preference record;
    private Preference about;

    private SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals("lastupdate")) {
                update.setSummary(DataManager.getInstance().getUpdateInfo());
            }
            if (key.equals("userid")) {
                user.setSummary(UserManager.getInstance().getCurrentUserName());
            }
            if (key.equals("server")) {
                server.setSummary(ServerManager.getInstance().getServerName());
            }
        }
    };

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        Utils.logD(TAG, "onCreatePreferences");
        setPreferencesFromResource(R.xml.preference, rootKey);

        update = findPreference("data_update");
        update.setOnPreferenceClickListener(preference -> {
            DataManager.getInstance().showConfirmDialogCaimogu();
            btnRestore(preference);
            return true;
        });

        boss = findPreference("data_boss");
        boss.setOnPreferenceClickListener(preference -> {
            NavController controller = Navigation.findNavController(getView());
            controller.navigate(R.id.action_nav_main_to_nav_bossdata);
            return true;
        });

        user = findPreference("user");
        user.setOnPreferenceClickListener(preference -> {
            NavController controller = Navigation.findNavController(getView());
            controller.navigate(R.id.action_nav_main_to_nav_usermanager);
            return true;
        });

        characterScreen = findPreference("character_screen");
        characterScreen.setOnPreferenceClickListener(preference -> {
            NavController controller = Navigation.findNavController(getView());
            controller.navigate(R.id.action_nav_main_to_nav_characterscreen);
            return true;
        });

        characterScreenEnable = findPreference("character_screen_enable");
        characterScreenEnable.setChecked(new SetupPreference().isCharacterscreenenable());
        characterScreenEnable.setOnPreferenceChangeListener((preference, newValue) -> {
            new SetupPreference().setCharacterscreenenable((boolean) newValue);
            return true;
        });

        link = findPreference("link_open_type");
        link.setOnPreferenceClickListener(preference -> {
            MaterialDialog dialog = new MaterialDialog(getContext(), MaterialDialog.getDEFAULT_BEHAVIOR());
            dialog.title(R.string.menu_link_open_type, null);
            DialogSingleChoiceExtKt.listItemsSingleChoice(dialog, R.array.menu_link_open_type_dialog_options, null, null, new SetupPreference().getLinkopentype(), true, 0, 0, new Function3<MaterialDialog, Integer, CharSequence, Unit>() {
                @Override
                public Unit invoke(MaterialDialog materialDialog, Integer integer, CharSequence charSequence) {
                    new SetupPreference().setLinkopentype(integer);
                    return null;
                }
            });
            dialog.cancelOnTouchOutside(false);
            dialog.positiveButton(R.string.menu_link_open_type_dialog_confirm, null, null);
            dialog.show();
            return false;
        });

        server = findPreference("server");
        server.setOnPreferenceClickListener(preference -> {
            MaterialDialog dialog = new MaterialDialog(getContext(), MaterialDialog.getDEFAULT_BEHAVIOR());
            dialog.title(R.string.menu_server, null);
            List<String> items = new ArrayList<>();
            items.add(StaticHelper.ZH_CN_SERVERNAME);
            items.add(StaticHelper.ZH_TW_SERVERNAME);
            DialogSingleChoiceExtKt.listItemsSingleChoice(dialog, 0, items, null, ServerManager.getInstance().getCurrentServer(), true, 0, 0, new Function3<MaterialDialog, Integer, CharSequence, Unit>() {
                @Override
                public Unit invoke(MaterialDialog materialDialog, Integer integer, CharSequence charSequence) {
                    ServerManager.getInstance().setCurrentServer(integer);
                    return null;
                }
            });
            dialog.cancelOnTouchOutside(false);
            dialog.positiveButton(R.string.menu_link_open_type_dialog_confirm, null, null);
            dialog.show();
            return false;
        });

        app = findPreference("app_version");
        app.setOnPreferenceClickListener(preference -> {
            UpdateManager.getInstance().checkAppVersion(false);
            btnRestore(preference);
            return true;
        });

        autoUpdate = findPreference("auto_update");
        autoUpdate.setChecked(new SetupPreference().isAutoupdate());
        autoUpdate.setOnPreferenceChangeListener((preference, newValue) -> {
            new SetupPreference().setAutoupdate((boolean) newValue);
            return true;
        });

        record = findPreference("record");
        record.setOnPreferenceClickListener(preference -> {
            NavController controller = Navigation.findNavController(getView());
            controller.navigate(R.id.action_nav_main_to_nav_record);
            return true;
        });

        about = findPreference("about");
        about.setOnPreferenceClickListener(preference -> {
            NavController controller = Navigation.findNavController(getView());
            controller.navigate(R.id.action_nav_main_to_nav_about);
            return true;
        });

        new DataPreference().registListener(listener);
        new UserPreference().registListener(listener);
        new SetupPreference().registListener(listener);
    }

    private void btnRestore(Preference preference) {
        preference.setEnabled(false);
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Activity activity = getActivity();
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            preference.setEnabled(true);
                        }
                    });
                }
            }
        }.start();
    }

    @Override
    public void onResume() {
        Utils.logD(TAG, "onResume");
        super.onResume();
        update.setSummary(DataManager.getInstance().getUpdateInfo());
        user.setSummary(UserManager.getInstance().getCurrentUserName());
        server.setSummary(ServerManager.getInstance().getServerName());
        app.setSummary(Utils.getVersionName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        new DataPreference().unregistListener(listener);
        new UserPreference().unregistListener(listener);
        new SetupPreference().unregistListener(listener);
    }
}
