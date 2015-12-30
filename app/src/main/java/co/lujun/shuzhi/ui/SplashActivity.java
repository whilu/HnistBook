package co.lujun.shuzhi.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import co.lujun.shuzhi.R;
import co.lujun.shuzhi.bean.Config;
import co.lujun.shuzhi.util.PreferencesUtils;

/**
 * Created by Administrator on 2015/7/8.
 */
public class SplashActivity extends Activity {

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //init umeng
        MobclickAgent.openActivityDurationTrack(false);
        PushAgent.getInstance(this).setDebugMode(false);
        if (!PreferencesUtils.getBoolean(this, Config.CONFIG_PUSH_MSG_KEY, false)){
            PushAgent.getInstance(this).enable();
            PreferencesUtils.putBoolean(this, Config.CONFIG_PUSH_MSG_KEY, true);
        }
        //delay some time
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mIntent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(mIntent);
                SplashActivity.this.finish();
            }
        }, Config.APP_SPLASH_TIME);
    }

    @Override protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getSimpleName());
        MobclickAgent.onResume(this);
    }

    @Override protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getSimpleName());
        MobclickAgent.onPause(this);
    }
}
