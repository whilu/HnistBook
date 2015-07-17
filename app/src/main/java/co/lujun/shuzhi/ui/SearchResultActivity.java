package co.lujun.shuzhi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;

import com.umeng.analytics.MobclickAgent;

import co.lujun.shuzhi.R;
import co.lujun.shuzhi.bean.Config;
import co.lujun.shuzhi.ui.fragments.BookListFragment;

/**
 * Created by lujun on 2015/3/2.
 */
public class SearchResultActivity extends co.lujun.shuzhi.ui.widget.SlidingActivity {

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
        fragmentManager = getSupportFragmentManager();
        if (getIntent().getStringExtra(Config.SEARCH_KEY) != null
                && !TextUtils.isEmpty(getIntent().getStringExtra(Config.SEARCH_KEY))){
            searchKeyWords = getIntent().getStringExtra(Config.SEARCH_KEY);
            setTitle(searchKeyWords);
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString(Config.BOOK_LST_SEARCH_KEY, searchKeyWords);
            intent.putExtras(bundle);
            setIntent(intent);
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

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
