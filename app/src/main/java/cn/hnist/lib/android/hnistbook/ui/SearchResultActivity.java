package cn.hnist.lib.android.hnistbook.ui;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import cn.hnist.lib.android.hnistbook.R;
import cn.hnist.lib.android.hnistbook.bean.SlidingActivity;

/**
 * Created by lujun on 2015/3/2.
 */
public class SearchResultActivity extends SlidingActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        Log.i("SearchResultActivity", "onNewIntent() is called");
        super.onNewIntent(intent);
        doSearchQuery(intent);
    }


    private void doSearchQuery(Intent intent) {
        Log.i("SearchResultActivity", "doSearchQuery() is called");
        if (intent == null)
            return;
        String queryAction = intent.getAction();
        if (Intent.ACTION_SEARCH.equals(queryAction)) {
            String queryString = intent.getStringExtra(SearchManager.QUERY);
            Log.i("SearchResultActivity", "doSearchQuery()" + queryString);
        }
    }
}
