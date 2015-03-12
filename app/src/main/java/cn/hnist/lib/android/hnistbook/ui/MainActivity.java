package cn.hnist.lib.android.hnistbook.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import cn.hnist.lib.android.hnistbook.R;
import cn.hnist.lib.android.hnistbook.ui.adapter.SliderMenuAdapter;
import cn.hnist.lib.android.hnistbook.ui.fragments.HomeFragment;
import cn.hnist.lib.android.hnistbook.util.IntentUtils;

public class MainActivity extends ActionBarActivity {
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private int iii;
    private ListView mDrawerList;
    private CharSequence mTitle, mDrawerTitle;
    private String[] mPlanetTitles;
    private FragmentManager fragmentManager;
    private HomeFragment homeFragment;
    private int[] mPlanetIcons;

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
        mDrawerLayout = (DrawerLayout) this.findViewById(R.id.drawer);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mPlanetTitles = getResources().getStringArray(R.array.planets_array);
        mPlanetIcons = new int[]{
                R.drawable.ic_account_circle_grey600_36dp,
                R.drawable.ic_account_circle_grey600_36dp,
                R.drawable.ic_account_circle_grey600_36dp,
                R.drawable.ic_account_circle_grey600_36dp
        };
        fragmentManager = getFragmentManager();
        homeFragment = new HomeFragment();

        mDrawerList.setAdapter(new SliderMenuAdapter(mPlanetTitles, mPlanetIcons));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//打开up botton
        mTitle = mDrawerTitle = getSupportActionBar().getTitle();
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close){

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
            replaceFragment(homeFragment);
        }
    }

    private void replaceFragment(Fragment fragment){
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void setupSearchView(Menu menu){
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        if (searchView == null){
            Log.i("SearchView", "SearchView null");
            return;
        }
        searchView.setIconifiedByDefault(true);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        ComponentName componentName = new ComponentName(this, SearchResultActivity.class);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(componentName);
        if (searchableInfo == null){
            Log.i("SearchableInfo", "Failed to find search info");
            return;
        }
        searchView.setSearchableInfo(searchableInfo);
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
//            startActivity(new Intent(this, SettingsActivity.class));
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
}
