package cn.sun45.warbanner.ui.fragments.menu;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.list.DialogMultiChoiceExtKt;
import com.afollestad.materialdialogs.list.DialogSingleChoiceExtKt;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.datamanager.clanwar.ClanWarManager;
import cn.sun45.warbanner.datamanager.source.SourceManager;
import cn.sun45.warbanner.datamanager.update.UpdateManager;
import cn.sun45.warbanner.document.preference.ClanwarPreference;
import cn.sun45.warbanner.document.preference.SetupPreference;
import cn.sun45.warbanner.document.preference.SourcePreference;
import cn.sun45.warbanner.user.UserManager;
import cn.sun45.warbanner.util.Utils;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function3;

/**
 * Created by Sun45 on 2021/5/29
 * 菜单PreferenceFragment
 */
public class MenuPreferecefragment extends PreferenceFragmentCompat {
    private static final String TAG = "MenuPreferecefragment";

    private Preference clanware;
    private Preference db;
    private Preference app;
    private Preference user;

    private Preference characterScreen;
    private SwitchPreferenceCompat characterScreenEnable;

    private Preference link;
    private Preference record;

    private SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals("lastupdate")) {
                clanware.setSummary(ClanWarManager.getInstance().getUpdateInfo());
            }
            if (key.equals("dbVersion")) {
                db.setSummary(SourceManager.getInstance().getDbVersion() + "");
            }
        }
    };

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        Utils.logD(TAG, "onCreatePreferences");
        setPreferencesFromResource(R.xml.preference, rootKey);

        clanware = findPreference("clanwar_update");
        clanware.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ClanWarManager.getInstance().showConfirmDialog(false);
                btnRestore(preference);
                return true;
            }
        });

        db = findPreference("db_version");
        db.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SourceManager.getInstance().checkDatabaseVersion(false);
                btnRestore(preference);
                return true;
            }
        });

        app = findPreference("app_version");
        app.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                UpdateManager.getInstance().checkAppVersion(false);
                btnRestore(preference);
                return true;
            }
        });

        user = findPreference("user");
        user.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                NavController controller = Navigation.findNavController(getView());
                controller.navigate(R.id.action_nav_main_to_nav_usermanager);
                return true;
            }
        });

        characterScreen = findPreference("character_screen");
        characterScreen.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                NavController controller = Navigation.findNavController(getView());
                controller.navigate(R.id.action_nav_main_to_nav_characterscreen);
                return true;
            }
        });

        characterScreenEnable = findPreference("character_screen_enable");
        characterScreenEnable.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                new SetupPreference().setCharacterscreenenable((boolean) newValue);
                return true;
            }
        });

        link = findPreference("link_open_type");
        link.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
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
            }
        });

        record = findPreference("record");
        record.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                NavController controller = Navigation.findNavController(getView());
                controller.navigate(R.id.action_nav_main_to_nav_record);
                return true;
            }
        });

        new ClanwarPreference().registListener(listener);
        new SourcePreference().registListener(listener);
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
        clanware.setSummary(ClanWarManager.getInstance().getUpdateInfo());
        db.setSummary(SourceManager.getInstance().getDbVersion() + "");
        app.setSummary(Utils.getVersionName());
        user.setSummary(UserManager.getInstance().getCurrentUserName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        new ClanwarPreference().unregistListener(listener);
        new SourcePreference().unregistListener(listener);
    }
}