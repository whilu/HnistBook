package co.lujun.shuzhi.ui;

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
import com.afollestad.materialdialogs.Theme;
import com.umeng.message.PushAgent;

import co.lujun.shuzhi.GlApplication;
import co.lujun.shuzhi.R;
import co.lujun.shuzhi.bean.Config;
import co.lujun.shuzhi.util.CacheFileUtils;
import co.lujun.shuzhi.util.PreferencesUtils;

/**
 * Created by lujun on 2015/3/4.
 */
public class SettingsActivity extends BaseActivity {

    private Toolbar mToolBar;
    private SettingsFragment mSettingsFragment;

    @Override public void onCreate(Bundle savedInstanceState) {
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

        @Override public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            cbpPushService = (CheckBoxPreference)
                    findPreference(getString(R.string.pkey_push_service));
//            cbpNightModel = (CheckBoxPreference)
//                    findPreference(getString(R.string.pkey_night_model));
            cbpAutoUpdate = (CheckBoxPreference)
                    findPreference(getString(R.string.pkey_auto_check_update));
            pClearCache = (Preference) findPreference(getString(R.string.pkey_clear_cache));
            pAbout = (Preference) findPreference(getString(R.string.pkey_about));
            cbpPushService.setOnPreferenceChangeListener(this);
//            cbpNightModel.setOnPreferenceChangeListener(this);
            cbpAutoUpdate.setOnPreferenceChangeListener(this);
            pClearCache.setOnPreferenceClickListener(this);
            pAbout.setOnPreferenceClickListener(this);
        }

        @Override public boolean onPreferenceChange(Preference preference, Object newValue) {
            if (preference.getKey().equals(getString(R.string.pkey_push_service))){
                // 推送开关，是否需要持久化处理
                if ((Boolean) newValue){
                    PushAgent.getInstance(GlApplication.getContext()).enable();
                }else {
                    PushAgent.getInstance(GlApplication.getContext()).disable();
                }
            }/*else if (preference.getKey().equals(getString(R.string.pkey_night_model))){
                // TODO 夜间模式开关
                Toast.makeText(getActivity(), "夜间" + (Boolean) newValue, Toast.LENGTH_SHORT).show();
            }*/else if (preference.getKey().equals(getString(R.string.pkey_auto_check_update))){
                // 自动检测更新开关
                if ((Boolean) newValue){
                    PreferencesUtils.putBoolean(GlApplication.getContext(),
                            Config.CONFIG_AUTO_UPDATE_KEY, true);
                }else{
                    PreferencesUtils.putBoolean(GlApplication.getContext(),
                            Config.CONFIG_AUTO_UPDATE_KEY, false);
                }
            }
            return true;
        }

        @Override public boolean onPreferenceClick(Preference preference) {
            if (preference.getKey().equals(getString(R.string.pkey_clear_cache))){
                // 清空缓存
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        CacheFileUtils.deleteFile(GlApplication.getContext().getFilesDir()
                                + "/" + Config.SZ_CACHE_FILE_PATH);
                        CacheFileUtils.deleteFile(GlApplication.getContext().getFilesDir()
                                + "/" + Config.ANN_CACHE_FILE_PATH);
                        CacheFileUtils.deleteFile(GlApplication.getContext().getCacheDir() + "");
                    }
                }).start();
                Toast.makeText(GlApplication.getContext(),
                        getResources().getString(R.string.msg_cache_cleared),
                        Toast.LENGTH_SHORT).show();
            }else if (preference.getKey().equals(getString(R.string.pkey_about))){
                // 关于
                aboutDialog = new MaterialDialog.Builder(getActivity())
                        .title(getString(R.string.d_about_title))
                        .content(getString(R.string.d_about_msg))
                        .positiveText(getString(R.string.d_about_p_button))
                        .theme(Theme.LIGHT)
                        .show();
            }
            return false;
        }
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
