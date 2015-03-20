package cn.hnist.lib.android.hnistbook.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;

import cn.hnist.lib.android.hnistbook.R;
import cn.hnist.lib.android.hnistbook.bean.Config;
import cn.hnist.lib.android.hnistbook.bean.SlidingActivity;
import cn.hnist.lib.android.hnistbook.ui.fragments.BookListFragment;

/**
 * Created by lujun on 2015/3/2.
 */
public class SearchResultActivity extends SlidingActivity {

    private Toolbar mToolBar;
    private FragmentManager fragmentManager;
    private Fragment mFragment;
    private String searchKeyWords = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        mToolBar = (Toolbar) findViewById(R.id.search_result_toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fragmentManager = getFragmentManager();
        if (getIntent().getStringExtra(Config.SEARCH_KEY) != null
                && !TextUtils.isEmpty(getIntent().getStringExtra(Config.SEARCH_KEY))){
            searchKeyWords = getIntent().getStringExtra(Config.SEARCH_KEY);
            setTitle(searchKeyWords);
            mFragment = new BookListFragment();
            if (mFragment != null){
                replaceFragment(mFragment);
            }
        }
    }

    private void replaceFragment(Fragment fragment){
        fragmentManager.beginTransaction().replace(R.id.search_res_content_frame, fragment).commit();
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
