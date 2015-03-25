package cn.hnist.lib.android.hnistbook.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import cn.hnist.lib.android.hnistbook.R;
import cn.hnist.lib.android.hnistbook.ui.widget.SlidingActivity;

/**
 * Created by lujun on 2015/3/4.
 */
public class SettingsActivity extends SlidingActivity {

    private Toolbar mToolBar;
    private SettingsFragment mSettingsFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mToolBar = (Toolbar) findViewById(R.id.settings_toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.action_settings);
        if (savedInstanceState == null){
            mSettingsFragment = new SettingsFragment();
            replaceFragment(R.id.settings_container, mSettingsFragment);
        }
    }

    /** replace container with fragment*/
    public void replaceFragment(int viewId, Fragment fragment){
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(viewId, fragment).commit();
    }

    /**
     * A placeholder fragment containing a settings view.
     */
    public static class SettingsFragment extends PreferenceFragment
            implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

        private CheckBoxPreference cbpPushService, cbpNightModel, cbpAutoUpdate;
        private Preference pClearCache, pAbout;
        private MaterialDialog aboutDialog;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            cbpPushService = (CheckBoxPreference)
                    findPreference(getString(R.string.pkey_push_service));
            cbpNightModel = (CheckBoxPreference)
                    findPreference(getString(R.string.pkey_night_model));
            cbpAutoUpdate = (CheckBoxPreference)
                    findPreference(getString(R.string.pkey_auto_check_update));
            pClearCache = (Preference) findPreference(getString(R.string.pkey_clear_cache));
            pAbout = (Preference) findPreference(getString(R.string.pkey_about));
            cbpPushService.setOnPreferenceChangeListener(this);
            cbpNightModel.setOnPreferenceChangeListener(this);
            cbpAutoUpdate.setOnPreferenceChangeListener(this);
            pClearCache.setOnPreferenceClickListener(this);
            pAbout.setOnPreferenceClickListener(this);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if (preference.getKey().equals(getString(R.string.pkey_push_service))){
                // TODO 推送开关，是否需要持久化处理
                Toast.makeText(getActivity(), "推送" + (Boolean) newValue, Toast.LENGTH_SHORT).show();
            }else if (preference.getKey().equals(getString(R.string.pkey_night_model))){
                // TODO 夜间模式开关
                Toast.makeText(getActivity(), "夜间" + (Boolean) newValue, Toast.LENGTH_SHORT).show();
            }else if (preference.getKey().equals(getString(R.string.pkey_auto_check_update))){
                // TODO 自动检测更新开关
                Toast.makeText(getActivity(), "自动检测更新" + (Boolean) newValue, Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            if (preference.getKey().equals(getString(R.string.pkey_clear_cache))){
                // TODO 清空缓存
                Toast.makeText(getActivity(), "clear cache", Toast.LENGTH_SHORT).show();
            }else if (preference.getKey().equals(getString(R.string.pkey_about))){
                // TODO 关于
                aboutDialog = new MaterialDialog.Builder(getActivity())
                        .title(getString(R.string.d_about_title))
                        .content(getString(R.string.d_about_msg))
                        .positiveText(getString(R.string.d_about_p_button))
                        .show();
            }
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
