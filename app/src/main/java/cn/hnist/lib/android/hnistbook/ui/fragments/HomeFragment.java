package cn.hnist.lib.android.hnistbook.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.HashMap;

import cn.hnist.lib.android.hnistbook.R;
import cn.hnist.lib.android.hnistbook.api.Api;
import cn.hnist.lib.android.hnistbook.bean.Constant;
import cn.hnist.lib.android.hnistbook.ui.adapter.ViewPagerAdapter;
import cn.hnist.lib.android.hnistbook.ui.widget.TextViewVertical;
import cn.hnist.lib.android.hnistbook.util.TokenUtils;

/**
 * Created by lujun on 2015/3/9.
 */
public class HomeFragment extends Fragment {

    private View mView;
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private PageChangedListener mPageChangeListener;
    private ArrayList<View> views;
    private TextViewVertical tvPage2Author, tvPage2PYear, tvPage2Publisher, tvPage2ISBN;
    private ScrollView svPage2Main;

    private TokenUtils mTokenUtils;

    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constant.MSG_REQUEST_FAILED:

                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, null);
        initView();
        return mView;
    }

    private void init(){
        views = new ArrayList<View>();
        mPageChangeListener = new PageChangedListener();
        mTokenUtils = new TokenUtils(getActivity());
    }

    private void initView() {
        if (mView == null) {
            return;
        }
        mViewPager = (ViewPager) mView.findViewById(R.id.vp_home);
        views.add(LayoutInflater.from(getActivity()).inflate(R.layout.view_home_page2, null));
        views.add(LayoutInflater.from(getActivity()).inflate(R.layout.view_home_page1, null));
//        views.add(LayoutInflater.from(getActivity()).inflate(R.layout.view_home_page3, null));
        mViewPagerAdapter = new ViewPagerAdapter(views, null);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOnPageChangeListener(mPageChangeListener);
//        mViewPager.setCurrentItem(1);

        //
        svPage2Main = (ScrollView) views.get(0).findViewById(R.id.sv_page2_main);
        tvPage2Author = (TextViewVertical) views.get(0).findViewById(R.id.tv_page2_author);
        tvPage2PYear = (TextViewVertical) views.get(0).findViewById(R.id.tv_page2_pyear);
        tvPage2Publisher = (TextViewVertical) views.get(0).findViewById(R.id.tv_page2_publisher);
        tvPage2ISBN = (TextViewVertical) views.get(0).findViewById(R.id.tv_page2_isbn);
        //
        svPage2Main.setVerticalScrollBarEnabled(false);//hide scrollbar
        //
        mTokenUtils.getData(mHandler, new HashMap<String, String>(), Api.GET_TODAY_BOOK_URL);
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
