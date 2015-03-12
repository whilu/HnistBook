package cn.hnist.lib.android.hnistbook;

import android.content.Context;

import org.litepal.LitePalApplication;

/**
 * Created by lujun on 2015/3/1.
 */
public class GlApplication extends LitePalApplication {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }

    public static Context getContext(){
        return sContext;
    }
}
