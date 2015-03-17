package cn.hnist.lib.android.hnistbook.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;

import cn.hnist.lib.android.hnistbook.R;
import cn.hnist.lib.android.hnistbook.bean.Config;
import cn.hnist.lib.android.hnistbook.bean.SlidingActivity;
import cn.hnist.lib.android.hnistbook.bean.TextViewVertical;

/**
 * Created by lujun on 2015/3/2.
 */
public class SearchResultActivity extends SlidingActivity {

    private Toolbar mToolBar;
    private String searchKeyWords = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        mToolBar = (Toolbar) findViewById(R.id.search_result_toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getIntent().getStringExtra(Config.SEARCH_KEY) != null
                && !TextUtils.isEmpty(getIntent().getStringExtra(Config.SEARCH_KEY))){
            searchKeyWords = getIntent().getStringExtra(Config.SEARCH_KEY);
            setTitle("《" + searchKeyWords + "》");
            TextViewVertical tv = (TextViewVertical) findViewById(R.id.tv);
            tv.setText(searchKeyWords);
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
