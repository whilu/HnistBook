package co.lujun.shuzhi.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import co.lujun.shuzhi.R;
import co.lujun.shuzhi.api.Api;
import co.lujun.shuzhi.bean.Config;
import co.lujun.shuzhi.ui.adapter.SliderMenuAdapter;
import co.lujun.shuzhi.ui.fragments.BookListFragment;
import co.lujun.shuzhi.ui.fragments.DailyListFragment;
import co.lujun.shuzhi.ui.fragments.HomeFragment;
import co.lujun.shuzhi.util.IntentUtils;
import co.lujun.shuzhi.util.PreferencesUtils;

public class MainActivity extends ActionBarActivity implements SearchView.OnQueryTextListener,
        SearchView.OnCloseListener {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private SearchView mSearchView;
    private ListView mDrawerList;
    private CharSequence mTitle, mDrawerTitle;
    private String[] mPlanetTitles;
    private FragmentManager fragmentManager;
    private Fragment[] fragments;
    private Fragment curFragment;
    private Bundle mBundle;
    private String[] menuStrs;
    private static int[] mPlanetIcons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init(savedInstanceState);
    }

    /**
     * init ui & members
     */
    private void init(Bundle savedInstanceState){
        //init umeng update
        if (PreferencesUtils.getBoolean(this, Config.CONFIG_AUTO_UPDATE_KEY, true)){
            // 开启自动更新
            UmengUpdateAgent.update(this);
            PreferencesUtils.putBoolean(this, Config.CONFIG_AUTO_UPDATE_KEY, true);
        }
        //
        mDrawerLayout = (DrawerLayout) this.findViewById(R.id.drawer);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mPlanetTitles = getResources().getStringArray(R.array.planets_array);
        mPlanetIcons = new int[]{
                R.drawable.ic_today_grey600_48dp,
                R.drawable.ic_view_week_grey600_48dp,
                R.drawable.ic_view_day_grey600_48dp
        };
        fragmentManager = getSupportFragmentManager();
        //
        mBundle = new Bundle();
        menuStrs = new String[]{"", Api.GET_7DAY_BOOK_URL, Api.GET_30DAY_BOOK_URL};
        //

        fragments = new Fragment[]{
                new HomeFragment(),
                new DailyListFragment(),
                new DailyListFragment()
        };

        mDrawerList.setAdapter(new SliderMenuAdapter(mPlanetTitles, mPlanetIcons));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//打开up botton
        mTitle = mDrawerTitle = getSupportActionBar().getTitle();
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.drawer_open, R.string.drawer_close){

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };//实例化DrawerToggle
        mDrawerToggle.syncState();//把DrawerToggle放入toolbar
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null){
            selectItem(0);
            fragmentManager.beginTransaction().add(R.id.content_frame, fragments[0]).commit();
            curFragment = fragments[0];
        }
    }

    private void replaceFragment(Fragment from, Fragment to){
        if (from == null || to == null){
            return;
        }
        if (curFragment != to) {
            if (!to.isAdded()) {
                fragmentManager.beginTransaction().hide(from).add(R.id.content_frame, to).commit();
            } else {
                fragmentManager.beginTransaction().hide(from).show(to).commit();
            }
            curFragment = to;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        if (searchItem != null){
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            mSearchView = (SearchView) searchItem.getActionView();
            if(mSearchView != null){
                mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
                mSearchView.setIconified(true);
                mSearchView.setQueryHint(getResources().getString(R.string.search_hint));
                mSearchView.setOnQueryTextListener(this);
                mSearchView.setOnCloseListener(this);
            }
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (!TextUtils.isEmpty(query)){
            Intent searchIntent = new Intent(this, SearchResultActivity.class);
            searchIntent.putExtra(Config.SEARCH_KEY, query);
            IntentUtils.startPreviewActivity(this, searchIntent);
            mSearchView.onActionViewCollapsed();
        }
        return false;
    }

    @Override
    public boolean onClose() {
        return false;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
//        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            IntentUtils.startPreviewActivity(this, new Intent(this, SettingsActivity.class));
        }else if (id == R.id.action_scan){
            IntentUtils.startPreviewActivity(this, new Intent(this, CaptureActivity.class));
        }else if (id == R.id.action_search){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * DrawerItemClickListener class
     */
    private class DrawerItemClickListener implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
            view.setPressed(true);

            Intent intent = new Intent();
            mBundle.clear();
            mBundle.putString(Config.DAILY_LST_TYPE, menuStrs[position]);
            intent.putExtras(mBundle);
            setIntent(intent);
            replaceFragment(curFragment, fragments[position]);
        }
    }

    private void selectItem(int position){
        // update the main content by replacing fragments
        /*Fragment fragment = new PlanetFragment();
        Bundle args = new Bundle();
        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
        fragment.setArguments(args);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();*/
        // update selected item and title, then close the drawer
        for (int i = 0; i < mDrawerList.getChildCount(); i++){
            mDrawerList.getChildAt(i).setBackgroundColor(
                i == position ?
                getResources().getColor(R.color.gray) : getResources().getColor(R.color.white));
        }
        mDrawerList.setItemChecked(position, true);
        setTitle(position == 0 ? mDrawerTitle : mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
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
