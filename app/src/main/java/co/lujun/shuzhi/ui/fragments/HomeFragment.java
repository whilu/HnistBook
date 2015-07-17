package co.lujun.shuzhi.ui.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import co.lujun.shuzhi.GlApplication;
import co.lujun.shuzhi.R;
import co.lujun.shuzhi.anim.SwipViewAnimation;
import co.lujun.shuzhi.api.Api;
import co.lujun.shuzhi.bean.Annotation;
import co.lujun.shuzhi.bean.Book;
import co.lujun.shuzhi.bean.Config;
import co.lujun.shuzhi.bean.Daily;
import co.lujun.shuzhi.bean.JSONRequest;
import co.lujun.shuzhi.bean.ListData;
import co.lujun.shuzhi.ui.adapter.AnnotationAdapter;
import co.lujun.shuzhi.ui.adapter.ViewPagerAdapter;
import co.lujun.shuzhi.ui.widget.AnnDetailView;
import co.lujun.shuzhi.util.BlurUtils;
import co.lujun.shuzhi.util.CacheFileUtils;
import co.lujun.shuzhi.util.NetWorkUtils;
import co.lujun.shuzhi.util.TokenUtils;

/**
 * Created by lujun on 2015/3/9.
 */
public class HomeFragment extends Fragment {

    private View mView;
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private PageChangedListener mPageChangeListener;
    private SwipeRefreshLayout mRefreshLayout;
    private ArrayList<View> views;
    private TextView tvPage2Author, tvPage2PYear, tvPage2Publisher, tvPage2ISBN;
    private TextView tvPage2Which, tvPage2Title, tvPage2Sub, tvPage2Day, tvPage2YM, tvPage2Summary;
    private ImageView ivPage2Image;
    private View vPage2BookBlur;
    private ScrollView svPage2Main;
    private RecyclerView mAnnRecycleView;
    private List<Annotation> mAnns;
    private AnnotationAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private View mContainer;
    private CardView mCardView1, mCardView2, mStartCardView;
    private Button btnFlip;

    private SwipViewAnimation mSwipViewAnimation;

    private TokenUtils mTokenUtils;

    private String id = "";
    private int page = 0;

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
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        views = new ArrayList<View>();
        mAnns = new ArrayList<Annotation>();
        mAdapter = new AnnotationAdapter(mAnns);
        mPageChangeListener = new PageChangedListener();
        mTokenUtils = new TokenUtils();
    }

    private void initView() {
        if (mView == null) {
            return;
        }
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
        vPage2BookBlur = (View) views.get(0).findViewById(R.id.v_book_blur_bg);
        tvPage2Summary = (TextView) views.get(0).findViewById(R.id.tv_page2_summary);

        //
        mContainer  = views.get(0).findViewById(R.id.fl_container);
        mCardView1 = (CardView) views.get(0).findViewById(R.id.cv_1);
        mCardView2 = (CardView) views.get(0).findViewById(R.id.cv_2);
        btnFlip = (Button) views.get(0).findViewById(R.id.btn_flip);
        mStartCardView = mCardView1;
        //

        mRefreshLayout = (SwipeRefreshLayout) views.get(0).findViewById(R.id.srl_home2);

        mAnnRecycleView = (RecyclerView) views.get(1).findViewById(R.id.rv_annlist);

        mAnnRecycleView.setLayoutManager(mLayoutManager);
        mAnnRecycleView.setHasFixedSize(false);// 若每个item的高度固定，设置此项可以提高性能
        mAnnRecycleView.setItemAnimator(new DefaultItemAnimator());// item 动画效果
        mAdapter.setOnItemClickListener(new AnnotationAdapter.ViewHolder.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                AnnDetailView.setTitle("《" + mAnns.get(position).getBook().getTitle() + "》"
                        + getResources().getString(R.string.tv_annotation));
                AnnDetailView.setWriteInfo(mAnns.get(position).getAuthor_user().getName()
                        + getResources().getString(R.string.tv_write)
                        + mAnns.get(position).getTime().substring(0, 10));
                AnnDetailView.setContent(mAnns.get(position).getContent());
                AnnDetailView.show(view, Gravity.CENTER, 0, 0);
            }
        });
        mAnnRecycleView.setAdapter(mAdapter);
        mAnnRecycleView.setOnScrollListener(
                new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            int lastVisibleItem = mLayoutManager.findLastCompletelyVisibleItemPosition();
                            int totalItemCount = mLayoutManager.getItemCount();

                            if (lastVisibleItem == totalItemCount - 1) {
                                onUpdateAnnotation(id);
                            }
                        }
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                    }
                }
        );
        //
        svPage2Main.setVerticalScrollBarEnabled(false);//hide scrollbar
        //
        mSwipViewAnimation = null;// make SwipViewAnimation null in a fragment
        btnFlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSwipViewAnimation == null) {
                    mSwipViewAnimation = new SwipViewAnimation(mContainer, mCardView1, mCardView2);
                }
                if (0 == mSwipViewAnimation.getIndex() % 2) {
                    mSwipViewAnimation.applyRotation(0, 90);
                } else {
                    mSwipViewAnimation.applyRotation(0, -90);
                }
            }
        });

        //
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetWorkUtils.getNetWorkType(GlApplication.getContext())
                        == NetWorkUtils.NETWORK_TYPE_DISCONNECT) {
                    Toast.makeText(GlApplication.getContext(),
                            getResources().getString(R.string.msg_no_internet),
                            Toast.LENGTH_SHORT).show();
                    mRefreshLayout.setRefreshing(false);
                    return;
                }
                if (mRefreshLayout.isRefreshing()) {
                    onUpdateTodayDaily();
                    page = 0;
                }
            }
        });
        //设置默认显示信息
        tvPage2Author.setText(getString(R.string.tv_book_author));
        tvPage2Publisher.setText(getString(R.string.tv_book_publisher));
        tvPage2PYear.setText(getString(R.string.tv_book_pubdate));
        tvPage2ISBN.setText(getString(R.string.tv_book_isbn));
//        tvPage2Summary.setText("");

        //set cache
        Daily daily = (Daily) CacheFileUtils.readObject(Config.SZ_CACHE_FILE_PATH);
        if (daily != null){
            onSetBookData(daily, true);
        }
        ListData tmpAnn = (ListData) CacheFileUtils.readObject(Config.ANN_CACHE_FILE_PATH);
        if (tmpAnn != null){
            onSetAnnData(tmpAnn, true, true);
        }
        //请求TOKEN设置回调监听
        mTokenUtils.setResponseListener(new TokenUtils.OnResponseListener() {
            @Override
            public void onFailure(String s) {
                Toast.makeText(GlApplication.getContext(), s, Toast.LENGTH_SHORT).show();
                mRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onSuccess(Map<String, String> map) {
                final Map<String, String> tmpMap = map;
                JSONRequest<Daily> jsonRequest = new JSONRequest<Daily>(
                        Request.Method.POST,
                        Api.GET_TODAY_BOOK_URL,
                        Daily.class,
                        new Response.Listener<Daily>() {
                            @Override
                            public void onResponse(Daily daily) {
                                onSetBookData(daily, false);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Toast.makeText(GlApplication.getContext(),
                                        volleyError.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                mRefreshLayout.setRefreshing(false);
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        return tmpMap;
                    }
                };
                GlApplication.getRequestQueue().add(jsonRequest);
            }
        });
        onUpdateTodayDaily();
    }

    /**
     * 请求更新今日书志，首先获取OKEN，成功后回调请求书志信息
     */
    private void onUpdateTodayDaily(){
        mTokenUtils.getRequestParam();
    }

    /**
     * set book data
     * @param daily
     */
    private void onSetBookData(Daily daily, boolean isCache){
        if (daily == null){
            return;
        }
        int status = daily.getStatus();
        if (status == 1){
            Book book = daily.getBook();
            Daily.Extra extra = daily.getExtra();
            if (book != null){
                //write cache
                if (!isCache) {
                    if (!CacheFileUtils.saveObject(daily, Config.SZ_CACHE_FILE_PATH)){
                        Toast .makeText(GlApplication.getContext(),
                                getResources().getString(R.string.msg_cache_error),
                                Toast.LENGTH_SHORT).show();
                    }
                }
                //set data
                if (!TextUtils.isEmpty(book.getImages().getSmall())){
                    Glide.with(GlApplication.getContext()).load(book.getImages().getLarge())
                            .into(ivPage2Image);
                    //book background blur
                    ImageLoader.getInstance().loadImage(book.getImages().getLarge(),
                            new SimpleImageLoadingListener() {

                                @Override
                                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                    super.onLoadingComplete(imageUri, view, loadedImage);
                                    final Bitmap bmp = loadedImage;
                                    vPage2BookBlur.getViewTreeObserver().addOnGlobalLayoutListener(
                                            new ViewTreeObserver.OnGlobalLayoutListener() {
                                            @Override
                                            public void onGlobalLayout() {
                                                if (vPage2BookBlur.getBackground() == null) {
                                                    BlurUtils.blur(bmp, vPage2BookBlur);
                                                }
                                            }
                                        }
                                    );
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
                if (!isCache) {
                    onUpdateAnnotation(id);
                }
            }
        }else {
            Toast.makeText(GlApplication.getContext(), daily.getInfo(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * update book annotation with book id
     */
    private void onUpdateAnnotation(String id){
        if (TextUtils.isEmpty(id)){
            Toast.makeText(GlApplication.getContext(),
                    getResources().getString(R.string.msg_book_id_null), Toast.LENGTH_SHORT).show();
            mRefreshLayout.setRefreshing(false);
            return;
        }
        JSONRequest<ListData> jsonRequest = new JSONRequest<ListData>(
                Api.DOUBAN_HOST + id + "/annotations" + "?page=" + page,
                ListData.class,
                new Response.Listener<ListData>() {
                    @Override
                    public void onResponse(ListData listData) {
                        if (page == 0){
                            onSetAnnData(listData, true, false);
                        }else {
                            onSetAnnData(listData, false, false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast .makeText(GlApplication.getContext(), volleyError.getMessage(),
                                Toast.LENGTH_SHORT).show();
                            /*Toast .makeText(GlApplication.getContext(),,
                                    getResources().getString(R.string.msg_find_error),
                                    Toast.LENGTH_SHORT).show();*/
                    }
                }
        );
        GlApplication.getRequestQueue().add(jsonRequest);
    }

    /**
     * set Annotation list data
     * @param listData
     * @param isUpdate
     * @param isCache
     */
    private void onSetAnnData(ListData listData, boolean isUpdate, boolean isCache){
        if (listData != null){
            if (isUpdate){// update
                if (!isCache){
                    if (!CacheFileUtils.saveObject(listData,
                            Config.ANN_CACHE_FILE_PATH)){
                        Toast .makeText(GlApplication.getContext(),
                                getResources().getString(R.string.msg_cache_error),
                                Toast.LENGTH_SHORT).show();
                    }
                }
                mAnns.clear();
            }
            if (listData.getAnnotations() != null){
                if (listData.getAnnotations().size() <= 0){
                    Toast .makeText(GlApplication.getContext(), getResources().getString(R.string.msg_no_find),
                            Toast.LENGTH_SHORT).show();
                    mRefreshLayout.setRefreshing(false);
                    return;
                }else {
                    if(!isCache) {
                        page++;
                    }
                }
                mAnns.addAll(listData.getAnnotations());
                mAdapter.notifyDataSetChanged();
            }
        }
        mRefreshLayout.setRefreshing(false);
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

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(HomeFragment.class.getName());
        MobclickAgent.onResume(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(HomeFragment.class.getName());
        MobclickAgent.onPause(getActivity());
    }
}
