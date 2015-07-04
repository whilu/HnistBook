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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.hnist.lib.android.hnistbook.GlApplication;
import cn.hnist.lib.android.hnistbook.R;
import cn.hnist.lib.android.hnistbook.anim.SwipViewAnimation;
import cn.hnist.lib.android.hnistbook.api.Api;
import cn.hnist.lib.android.hnistbook.bean.Annotation;
import cn.hnist.lib.android.hnistbook.bean.Book;
import cn.hnist.lib.android.hnistbook.bean.Constant;
import cn.hnist.lib.android.hnistbook.bean.JsonData;
import cn.hnist.lib.android.hnistbook.ui.adapter.AnnotationAdapter;
import cn.hnist.lib.android.hnistbook.ui.adapter.BookAdapter;
import cn.hnist.lib.android.hnistbook.ui.adapter.ViewPagerAdapter;
import cn.hnist.lib.android.hnistbook.util.BlurUtils;
import cn.hnist.lib.android.hnistbook.util.IntentUtils;
import cn.hnist.lib.android.hnistbook.util.TokenUtils;

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
    private View ivPage2BookBlur;
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
    private int start = 0;

    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constant.MSG_REQUEST_FAILED:

                    break;

                case Constant.MSG_REQUEST_SUCCESS:
                    onSetBookData(msg.obj.toString());
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
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        views = new ArrayList<View>();
        mAnns = new ArrayList<Annotation>();
        mAdapter = new AnnotationAdapter(mAnns);
        mPageChangeListener = new PageChangedListener();
        mTokenUtils = new TokenUtils(getActivity(), mHandler);
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

        mAnnRecycleView = (RecyclerView) views.get(1).findViewById(R.id.rv_annlist);

        mAnnRecycleView.setLayoutManager(mLayoutManager);
        mAnnRecycleView.setHasFixedSize(true);// 若每个item的高度固定，设置此项可以提高性能
        mAnnRecycleView.setItemAnimator(new DefaultItemAnimator());// item 动画效果
        mAdapter.setOnItemClickListener(new AnnotationAdapter.ViewHolder.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        mAnnRecycleView.setAdapter(mAdapter);
        mAnnRecycleView.setOnScrollListener(
                new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);

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
                if (mRefreshLayout.isRefreshing()){
                    mTokenUtils.getData(new HashMap<String, String>(), Api.GET_TODAY_BOOK_URL);
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

    /**
     * set book data
     * @param data
     */
    private void onSetBookData(String data){
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
                                    BlurUtils.blur(loadedImage, ivPage2BookBlur, 1.5f,1.1f);
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
                onUpdateAnnotation(id);
            }
        }else {
            Toast.makeText(GlApplication.getContext(), jsonData.getInfo(), Toast.LENGTH_SHORT).show();
        }
        //TODO test , need to delete
        onUpdateAnnotation("4238362");
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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Api.DOUBAN_HOST + id + "/annotations",
                null,
                new Response.Listener<JSONObject>(){

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        onSetAnnData(jsonObject, false);
                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast .makeText(GlApplication.getContext(), volleyError.getMessage(),
                                Toast.LENGTH_SHORT).show();
                            /*Toast .makeText(GlApplication.getContext(),,
                                    getResources().getString(R.string.msg_find_error),
                                    Toast.LENGTH_SHORT).show();*/
                    }
                });
        GlApplication.getRequestQueue().add(jsonObjectRequest);
    }

    /**
     * set Annotation list data
     * @param jsonObject
     * @param isUpdate
     */
    private void onSetAnnData(JSONObject jsonObject, boolean isUpdate){
        if (jsonObject != null){
            if (isUpdate){// update
                mAnns.clear();
            }
            String json_arr = "";
            try{
                json_arr = jsonObject.getJSONArray("annotations").toString();
                start += (Integer) jsonObject.get("count");
            } catch (JSONException e){
                e.printStackTrace();
            }

            List<Annotation> anns = JSON.parseArray(json_arr, Annotation.class);
            if (anns.size() <= 0){
                Toast .makeText(getActivity(), getResources().getString(R.string.msg_no_find),
                        Toast.LENGTH_SHORT).show();
                mRefreshLayout.setRefreshing(false);
                return;
            }
            mAnns.addAll(anns);
            Log.d("debugss", mAnns.get(0).getAuthor_user().getAvatar() + "");
            mAdapter.notifyDataSetChanged();
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
}
