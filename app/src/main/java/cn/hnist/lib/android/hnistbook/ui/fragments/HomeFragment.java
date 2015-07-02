package cn.hnist.lib.android.hnistbook.ui.fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import cn.hnist.lib.android.hnistbook.GlApplication;
import cn.hnist.lib.android.hnistbook.R;
import cn.hnist.lib.android.hnistbook.anim.Rotate3dAnimation;
import cn.hnist.lib.android.hnistbook.api.Api;
import cn.hnist.lib.android.hnistbook.bean.Book;
import cn.hnist.lib.android.hnistbook.bean.Constant;
import cn.hnist.lib.android.hnistbook.bean.JsonData;
import cn.hnist.lib.android.hnistbook.ui.adapter.ViewPagerAdapter;
import cn.hnist.lib.android.hnistbook.util.BlurUtils;
import cn.hnist.lib.android.hnistbook.util.TokenUtils;

/**
 * Created by lujun on 2015/3/9.
 */
public class HomeFragment extends Fragment {

    private View mView;
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private PageChangedListener mPageChangeListener;
    private SwipeRefreshLayout mRefreshLayout, mRefreshLayout3;
    private ArrayList<View> views;
    private TextView tvPage2Author, tvPage2PYear, tvPage2Publisher, tvPage2ISBN;
    private TextView tvPage2Which, tvPage2Title, tvPage2Sub, tvPage2Day, tvPage2YM, tvPage2Summary;
    private ImageView ivPage2Image;
    private View ivPage2BookBlur;
    private ScrollView svPage2Main;
    private RecyclerView mAnnRecycleView;

    private View mContainer;
    private CardView mCardView1, mCardView2, mStartCardView;
    private Button btnFlip;
    private int mIndex;
    private int mDuration;
    private float mCenterX;
    private float mCenterY;
    float mDepthZ  = 500.0f;


    private TokenUtils mTokenUtils;

    private String id = "";

    private int countIndex;

    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constant.MSG_REQUEST_FAILED:
                    countIndex = 0;
                    break;

                case Constant.MSG_REQUEST_SUCCESS:
                    if (countIndex == 0) {
                        onUpdateData(msg.obj.toString());
                    }else {
                        Log.d("debug", msg.obj.toString());
                    }
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
        mIndex = 0;
        mDuration = 300;
        mCenterX = 0.0f;
        mCenterY = 0.0f;

        views = new ArrayList<View>();
        mPageChangeListener = new PageChangedListener();
        mTokenUtils = new TokenUtils(getActivity(), mHandler);
    }

    private void initView() {
        if (mView == null) {
            return;
        }
        countIndex = 0;
        mViewPager = (ViewPager) mView.findViewById(R.id.vp_home);
        views.add(LayoutInflater.from(getActivity()).inflate(R.layout.view_home_page2, null));
        views.add(LayoutInflater.from(getActivity()).inflate(R.layout.view_home_page3, null));
        mViewPagerAdapter = new ViewPagerAdapter(views, null);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOnPageChangeListener(mPageChangeListener);
//        mViewPager.setCurrentItem(1);

        //
        svPage2Main = (ScrollView) views.get(0).findViewById(R.id.sv_page2_main);
        tvPage2Author = (TextView) views.get(0).findViewById(R.id.tv_page2_author);
        tvPage2PYear = (TextView) views.get(0).findViewById(R.id.tv_page2_pyear);
        tvPage2Publisher = (TextView) views.get(0).findViewById(R.id.tv_page2_publisher);
        tvPage2ISBN = (TextView) views.get(0).findViewById(R.id.tv_page2_isbn);

        tvPage2Which = (TextView) views.get(0).findViewById(R.id.tv_page2_which);
        tvPage2Title = (TextView) views.get(0).findViewById(R.id.tv_page2_title);
        tvPage2Sub = (TextView) views.get(0).findViewById(R.id.tv_page2_sub);
        tvPage2Day = (TextView) views.get(0).findViewById(R.id.tv_page2_day);
        tvPage2YM = (TextView) views.get(0).findViewById(R.id.tv_page2_ym);
        ivPage2Image = (ImageView) views.get(0).findViewById(R.id.iv_page2_image);
        ivPage2BookBlur = (View) views.get(0).findViewById(R.id.iv_book_blur_bg);
        tvPage2Summary = (TextView) views.get(0).findViewById(R.id.tv_page2_summary);

        //
        mContainer  = views.get(0).findViewById(R.id.fl_container);
        mCardView1 = (CardView) views.get(0).findViewById(R.id.cv_1);
        mCardView2 = (CardView) views.get(0).findViewById(R.id.cv_2);
        btnFlip = (Button) views.get(0).findViewById(R.id.btn_flip);
        mStartCardView = mCardView1;
        //

        mRefreshLayout = (SwipeRefreshLayout) views.get(0).findViewById(R.id.srl_home2);
        //
        mRefreshLayout3 = (SwipeRefreshLayout) views.get(1).findViewById(R.id.srl_home3);

        mAnnRecycleView = (RecyclerView) views.get(1).findViewById(R.id.rv_annlist);
        //
        svPage2Main.setVerticalScrollBarEnabled(false);//hide scrollbar

        //
        btnFlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCenterX = mContainer.getWidth() / 2;
                mCenterY = mContainer.getHeight() / 2;

                if (0 == mIndex % 2) {
                    applyRotation(mStartCardView, 0, 90);
                } else {
                    applyRotation(mStartCardView, 0, -90);
                }
            }
        });

        //
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mRefreshLayout.isRefreshing()){
                    mTokenUtils.getData(new HashMap<String, String>(), Api.GET_TODAY_BOOK_URL);
                }
            }
        });
        mRefreshLayout3.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mRefreshLayout3.isRefreshing()) {
                    onUpdateAnnotation(id);
                }
            }
        });
        //
        mTokenUtils.getData(new HashMap<String, String>(), Api.GET_TODAY_BOOK_URL);

        //设置默认显示信息
        tvPage2Author.setText(getString(R.string.tv_book_author));
        tvPage2Publisher.setText(getString(R.string.tv_book_publisher));
        tvPage2PYear.setText(getString(R.string.tv_book_pubdate));
        tvPage2ISBN.setText(getString(R.string.tv_book_isbn));
        tvPage2Summary.setText(getString(R.string.test_intro));
    }

    private void onUpdateData(String data){
        JsonData jsonData = JSON.parseObject(data, JsonData.class);
        if (jsonData == null){
            return;
        }
        int status = jsonData.getStatus();
        if (status == 1){
            Book book = JSON.parseObject(jsonData.getData(), Book.class);
            JsonData.Extra extra = jsonData.getExtra();
            if (book != null){
                if (!TextUtils.isEmpty(book.getImages().getSmall())){
                    Glide.with(GlApplication.getContext()).load(book.getImages().getLarge())
                            .into(ivPage2Image);

                    //book blur
                    /*ivPage2BookBlur.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            ivPage2BookBlur.getViewTreeObserver().removeOnPreDrawListener(this);
                            ivPage2BookBlur.buildDrawingCache();

                            Bitmap bitmap = ivPage2BookBlur.getDrawingCache();
                            BlurUtils.blur(bitmap, ivPage2BookBlurView);
                            return true;
                        }
                    });*/
                    ImageLoader.getInstance().loadImage(book.getImages().getLarge(),
                            new SimpleImageLoadingListener() {

                                @Override
                                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                    super.onLoadingComplete(imageUri, view, loadedImage);
                                    BlurUtils.blur(loadedImage, ivPage2BookBlur);
                                }
                            });
                }
                if (extra != null){
                    tvPage2Which.setText("VOL." + extra.getVol());
                    tvPage2Sub.setText(extra.getBrief());
                    tvPage2YM.setText(extra.getYMD()[1] + "." + extra.getYMD()[0]);
                    tvPage2Day.setText(extra.getYMD()[2]);
                }
                tvPage2Title.setText(book.getTitle());
                String author = "";
                for (int j = 0; j < book.getAuthor().length; j++){
                    author += book.getAuthor()[j] + "、";
                }
                if (author.length() > 0){ author = author.substring(0, author.length() - 1); }
                tvPage2Author.setText(author);
                tvPage2Publisher.setText(book.getPublisher());
                tvPage2PYear.setText(book.getPubdate());
                tvPage2ISBN.setText(TextUtils.isEmpty(book.getIsbn13()) ? book.getIsbn10() : book.getIsbn13());
                tvPage2Summary.setText(book.getSummary());
                id = book.getId();
                countIndex ++;
                onUpdateAnnotation(id);
            }
        }else {
            Toast.makeText(GlApplication.getContext(), jsonData.getInfo(), Toast.LENGTH_SHORT).show();
        }
        mRefreshLayout.setRefreshing(false);
    }

    /**
     * update book annotation with book id
     */
    private void onUpdateAnnotation(String id){
        if (TextUtils.isEmpty(id)){
            Toast.makeText(GlApplication.getContext(),
                    getResources().getString(R.string.msg_book_id_null), Toast.LENGTH_SHORT).show();
            return;
        }
        //TODO update annotation
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("p", "1");
        mTokenUtils.getData(map, Api.GET_BOOK_ANN_URL);
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

    private void applyRotation(View animView, float startAngle, float toAngle) {
        float centerX = mCenterX;
        float centerY = mCenterY;
        Rotate3dAnimation rotation = new Rotate3dAnimation(
                startAngle, toAngle, centerX, centerY, mDepthZ, true);
        rotation.setDuration(mDuration);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new DisplayNextView());

        animView.startAnimation(rotation);
    }

    /**
     * This class listens for the end of the first half of the animation.
     * It then posts a new action that effectively swaps the views when the container
     * is rotated 90 degrees and thus invisible.
     */
    private final class DisplayNextView implements Animation.AnimationListener {

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {

            mContainer.post(new SwapViews());
        }

        public void onAnimationRepeat(Animation animation) {
        }
    }

    private final class SwapViews implements Runnable {
        @Override
        public void run()
        {
            Rotate3dAnimation rotation;

            mCardView1.setVisibility(View.GONE);
            mCardView2.setVisibility(View.GONE);

            mIndex++;
            if (0 == mIndex % 2){
                mStartCardView = mCardView1;
                rotation = new Rotate3dAnimation(
                        90,
                        0,
                        mCenterX,
                        mCenterY, mDepthZ, false);
            }else{
                mStartCardView = mCardView2;
                rotation = new Rotate3dAnimation(
                        -90,
                        0,
                        mCenterX,
                        mCenterY, mDepthZ, false);
            }

            mStartCardView.setVisibility(View.VISIBLE);
            mStartCardView.requestFocus();

            rotation.setDuration(mDuration);
            rotation.setFillAfter(true);
            rotation.setInterpolator(new DecelerateInterpolator());
            mStartCardView.startAnimation(rotation);
        }
    }
}
