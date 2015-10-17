package co.lujun.shuzhi.ui;

import android.os.Bundle;

import com.umeng.analytics.MobclickAgent;

import co.lujun.shuzhi.ui.widget.SlidingActivity;

/**
 * Created by lujun on 2015/10/17.
 */
public class BaseActivity extends SlidingActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
