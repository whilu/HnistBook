package cn.hnist.lib.android.hnistbook.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

import cn.hnist.lib.android.hnistbook.GlApplication;
import cn.hnist.lib.android.hnistbook.R;
import cn.hnist.lib.android.hnistbook.api.Api;
import cn.hnist.lib.android.hnistbook.bean.Book;
import cn.hnist.lib.android.hnistbook.bean.Constant;
import cn.hnist.lib.android.hnistbook.bean.JsonData;
import cn.hnist.lib.android.hnistbook.ui.MainActivity;
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
    private TextView tvPage2Which, tvPage2Title, tvPage2Sub, tvPage2Day, tvPage2YM, tvPage1Summary;
    private ImageView ivPage2Image;
    private ScrollView svPage2Main;

    private TokenUtils mTokenUtils;

    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constant.MSG_REQUEST_FAILED:

                    break;

                case Constant.MSG_REQUEST_SUCCESS:
                    onUpdateData(msg.obj.toString());
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
        mTokenUtils = new TokenUtils(getActivity(), mHandler);
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

        tvPage2Which = (TextView) views.get(0).findViewById(R.id.tv_page2_which);
        tvPage2Title = (TextView) views.get(0).findViewById(R.id.tv_page2_title);
        tvPage2Sub = (TextView) views.get(0).findViewById(R.id.tv_page2_sub);
        tvPage2Day = (TextView) views.get(0).findViewById(R.id.tv_page2_day);
        tvPage2YM = (TextView) views.get(0).findViewById(R.id.tv_page2_ym);
        ivPage2Image = (ImageView) views.get(0).findViewById(R.id.iv_page2_image);
        //
        tvPage1Summary = (TextView) views.get(1).findViewById(R.id.tv_page1_summary);
        //
        svPage2Main.setVerticalScrollBarEnabled(false);//hide scrollbar
        //
        mTokenUtils.getData(new HashMap<String, String>(), Api.GET_TODAY_BOOK_URL);
    }

    private void onUpdateData(String data){
        JsonData jsonData = JSON.parseObject(data, JsonData.class);
        if (jsonData == null){
            return;
        }
        int status = jsonData.getStatus();
        if (status == 1){
            Book book = JSON.parseObject(jsonData.getInfo(), Book.class);
            if (book != null){
                if (!TextUtils.isEmpty(book.getImages().getSmall())){
                    Glide.with(GlApplication.getContext()).load(book.getImages().getSmall())
                            .into(ivPage2Image);
                }
                tvPage2Which.setText("VOL.1");
                tvPage2Title.setText(book.getTitle());
                tvPage2Sub.setText("xxxxxxxxxxxxxxxx");
                tvPage2YM.setText("Mar.2015");
                tvPage2Day.setText("15");
                String author = "";
                for (int j = 0; j < book.getAuthor().length; j++){
                    author += book.getAuthor()[j] + "、";
                }
                if (author.length() > 0){ author = author.substring(0, author.length() - 1); }
                tvPage2Author.setText(author);
                tvPage2Publisher.setText(book.getPublisher());
                tvPage2PYear.setText(book.getPubdate());
                tvPage2ISBN.setText(book.getIsbn13());
                tvPage1Summary.setText(book.getSummary());
            }
        }else {
            Toast.makeText(GlApplication.getContext(), jsonData.getInfo(), Toast.LENGTH_SHORT).show();
        }
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
