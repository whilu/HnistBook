package co.lujun.shuzhi;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.umeng.analytics.MobclickAgent;

import co.lujun.shuzhi.api.Api;
import co.lujun.shuzhi.api.DbApiService;
import co.lujun.shuzhi.api.SzApiService;
import co.lujun.shuzhi.ui.widget.AnnDetailView;
import co.lujun.tpsharelogin.TPManager;
import retrofit.RestAdapter;

/**
 * Created by lujun on 2015/3/1.
 */
public class App extends Application {

    private static Context sContext;
    private static RequestQueue sRequestQueue;
    private static RestAdapter sDbRestAdapter, sSzRestAdapter;
    private static SzApiService sSzApiService;
    private static DbApiService sDbApiService;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        AnnDetailView.init(this);
        sRequestQueue = Volley.newRequestQueue(this);
        MobclickAgent.openActivityDurationTrack(false);

        TPManager.getInstance().initAppConfig(
                BuildConfig.WB_APPREDIRECT, BuildConfig.WB_APPKEY, BuildConfig.WB_APPSECRET,
                BuildConfig.QQ_APPKEY, BuildConfig.QQ_APPSECRET,
                BuildConfig.WX_APPKEY, BuildConfig.WX_APPSECRET);
    }

    public static Context getContext(){
        return sContext;
    }

    public static RequestQueue getRequestQueue(){ return sRequestQueue; }

    public static RestAdapter getDbRestAdapter(){
        if (sDbRestAdapter == null){
            synchronized (App.class){
                if (sDbRestAdapter == null){
//                    OkHttpClient client = new OkHttpClient();
//                    client.setConnectTimeout(10, TimeUnit.SECONDS);
                    RestAdapter.Builder builder = new RestAdapter.Builder();
//                    builder.setClient(new OkClient(client));
                    sDbRestAdapter = builder.setEndpoint(Api.DOUBAN_HOST).build();
                }
            }
        }
        return sDbRestAdapter;
    }

    public static RestAdapter getSzRestAdapter(){
        if (sSzRestAdapter == null){
            synchronized (App.class){
                if (sSzRestAdapter == null){
//                    OkHttpClient client = new OkHttpClient();
//                    client.setConnectTimeout(10, TimeUnit.SECONDS);
                    RestAdapter.Builder builder = new RestAdapter.Builder();
//                    builder.setClient(new OkClient(client));
                    sSzRestAdapter = builder
                            .setEndpoint(BuildConfig.API_ENDPOINT + BuildConfig.API_VERSION)
                            .build();
                }
            }
        }
        return sSzRestAdapter;
    }

    public static SzApiService getSzApiService(){
        if (sSzApiService == null){
            synchronized (App.class){
                if (sSzApiService == null){
                    sSzApiService = getSzRestAdapter().create(SzApiService.class);
                }
            }
        }
        return sSzApiService;
    }

    public static DbApiService getDbApiService(){
        if (sDbApiService == null){
            synchronized (App.class){
                if (sDbApiService == null){
                    sDbApiService = getDbRestAdapter().create(DbApiService.class);
                }
            }
        }
        return sDbApiService;
    }

}
