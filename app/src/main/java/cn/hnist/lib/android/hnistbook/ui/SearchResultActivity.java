package cn.hnist.lib.android.hnistbook.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import cn.hnist.lib.android.hnistbook.R;
import cn.hnist.lib.android.hnistbook.bean.SlidingActivity;
import cn.hnist.lib.android.hnistbook.bean.TextViewVertical;

/**
 * Created by lujun on 2015/3/2.
 */
public class SearchResultActivity extends SlidingActivity {

    private Toolbar mToolBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        mToolBar = (Toolbar) findViewById(R.id.search_result_toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.action_search);
        TextViewVertical tv = (TextViewVertical) findViewById(R.id.tv);
        tv.setText("你好啊");
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
