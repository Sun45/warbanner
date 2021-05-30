package cn.sun45.warbanner.ui.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.datamanager.clanwar.ClanWarManager;
import cn.sun45.warbanner.datamanager.source.SourceManager;
import cn.sun45.warbanner.datamanager.update.UpdateManager;
import cn.sun45.warbanner.document.preference.ClanwarPreference;
import cn.sun45.warbanner.document.preference.SetupPreference;
import cn.sun45.warbanner.document.preference.SourcePreference;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2021/5/29
 * 菜单PreferenceFragment
 */
public class MenuPreferecefragment extends PreferenceFragmentCompat {
    private static final String TAG = "MenuPreferecefragment";

    private Preference clanware;
    private Preference db;
    private Preference app;

    private Preference characterScreen;
    private SwitchPreferenceCompat characterScreenEnable;

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
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                preference.setEnabled(true);
                            }
                        });
                    }
                }.start();
                return true;
            }
        });

        db = findPreference("db_version");
        db.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SourceManager.getInstance().checkDatabaseVersion(false);
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
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                preference.setEnabled(true);
                            }
                        });
                    }
                }.start();
                return true;
            }
        });

        app = findPreference("app_version");
        app.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                UpdateManager.getInstance().checkAppVersion(false);
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
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                preference.setEnabled(true);
                            }
                        });
                    }
                }.start();
                return true;
            }
        });

        characterScreen = findPreference("character_screen");
        characterScreen.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                NavController controller = Navigation.findNavController(getView());
                controller.setGraph(R.navigation.app_navigation);
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

        new ClanwarPreference().registListener(listener);
        new SourcePreference().registListener(listener);
    }

    @Override
    public void onResume() {
        Utils.logD(TAG, "onResume");
        super.onResume();
        clanware.setSummary(ClanWarManager.getInstance().getUpdateInfo());
        db.setSummary(SourceManager.getInstance().getDbVersion() + "");
        app.setSummary(Utils.getVersionName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        new ClanwarPreference().unregistListener(listener);
        new SourcePreference().unregistListener(listener);
    }
}
