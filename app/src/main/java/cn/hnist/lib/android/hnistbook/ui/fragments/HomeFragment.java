package cn.hnist.lib.android.hnistbook.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import cn.hnist.lib.android.hnistbook.R;
import cn.hnist.lib.android.hnistbook.ui.adapter.ViewPagerAdapter;

/**
 * Created by lujun on 2015/3/9.
 */
public class HomeFragment extends Fragment {
    private View mView;
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private PageChangedListener mPageChangeListener;
    private ArrayList<View> views;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_fragment_home, null);
        initView();
        return mView;
    }

    private void init(){
        views = new ArrayList<View>();
        mPageChangeListener = new PageChangedListener();
    }

    private void initView() {
        if (mView == null) {
            return;
        }
        mViewPager = (ViewPager) mView.findViewById(R.id.vp_home);
        views.add(LayoutInflater.from(getActivity()).inflate(R.layout.view_home_page1, null));
        views.add(LayoutInflater.from(getActivity()).inflate(R.layout.view_home_page2, null));
        views.add(LayoutInflater.from(getActivity()).inflate(R.layout.view_home_page3, null));
        mViewPagerAdapter = new ViewPagerAdapter(views, null);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOnPageChangeListener(mPageChangeListener);
        mViewPager.setCurrentItem(1);
    }

    /**
     * ViewPager滑动监听
     */
    private class PageChangedListener implements OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    }
}
