package co.lujun.shuzhi;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.umeng.analytics.MobclickAgent;

import co.lujun.shuzhi.ui.widget.AnnDetailView;
import co.lujun.tpsharelogin.TPManager;

/**
 * Created by lujun on 2015/3/1.
 */
public class GlApplication extends Application {

    private static Context sContext;
    private static RequestQueue mRequestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        AnnDetailView.init(this);
        mRequestQueue = Volley.newRequestQueue(this);
        MobclickAgent.openActivityDurationTrack(false);

        TPManager.getInstance().initAppConfig(
                BuildConfig.WB_APPREDIRECT, BuildConfig.WB_APPKEY, BuildConfig.WB_APPSECRET,
                BuildConfig.QQ_APPKEY, BuildConfig.QQ_APPSECRET,
                BuildConfig.WX_APPKEY, BuildConfig.WX_APPSECRET);
    }

    public static Context getContext(){
        return sContext;
    }

    public static RequestQueue getRequestQueue(){ return mRequestQueue; }
}
