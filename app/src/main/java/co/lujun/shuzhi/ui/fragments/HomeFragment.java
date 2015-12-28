package co.lujun.shuzhi.ui.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.tencent.connect.share.QQShare;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import co.lujun.shuzhi.App;
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
import co.lujun.shuzhi.ui.widget.ShareWindow;
import co.lujun.shuzhi.util.CacheFileUtils;
import co.lujun.shuzhi.util.ImageUtils;
import co.lujun.shuzhi.util.NetWorkUtils;
import co.lujun.shuzhi.util.SystemUtil;
import co.lujun.shuzhi.util.TokenUtils;
import co.lujun.tpsharelogin.bean.QQShareContent;
import co.lujun.tpsharelogin.bean.WBShareContent;
import co.lujun.tpsharelogin.bean.WXShareContent;
import co.lujun.tpsharelogin.listener.StateListener;
import co.lujun.tpsharelogin.platform.qq.QQManager;
import co.lujun.tpsharelogin.platform.weibo.WBManager;
import co.lujun.tpsharelogin.platform.weixin.WXManager;
import co.lujun.tpsharelogin.utils.WXUtil;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by lujun on 2015/3/9.
 */
public class HomeFragment extends BaseFragment {

    private View mView, mContainer;
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    private ArrayList<View> views;
    private TextView tvPage2Author, tvPage2PYear, tvPage2Publisher, tvPage2ISBN, tvPage2Which,
            tvPage2Title, tvPage2Sub, tvPage2Day, tvPage2YM, tvPage2Summary;
    private ImageView ivPage2Image, ivPage2BookBlur;
    private ScrollView svPage2Main;
    private RecyclerView mAnnRecycleView;
    private CardView mCardView1, mCardView2, mStartCardView;
    private Button btnFlip;
    private ImageButton fabShare;

    private ShareWindow shareWindow;
    private WXManager wxManager;
    private WBManager wbManager;
    private QQManager qqManager;

    private AnnotationAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private SwipViewAnimation mSwipViewAnimation;
    private TokenUtils mTokenUtils;
    private StateListener mShareStateListener;

    private List<Annotation> mAnns;
    private String id = "";
    private int page = 0;
    private boolean hasMore = true;
    private static final String TAG = "HomeFragment";

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, null);
        initView();
        return mView;
    }

    private void init() {
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        views = new ArrayList<View>();
        mAnns = new ArrayList<Annotation>();
        mAdapter = new AnnotationAdapter(mAnns);
        mTokenUtils = new TokenUtils();
        wxManager = new WXManager(getActivity());
        wbManager = new WBManager(getActivity());
        qqManager = new QQManager(getActivity());
        mShareStateListener = new StateListener() {
            @Override public void onComplete(Object o) {
//                SystemUtil.showToast(R.string.msg_share_succes);
            }

            @Override public void onError(String err) {
                SystemUtil.showToast(R.string.msg_share_failed);
            }

            @Override public void onCancel() {
                SystemUtil.showToast(R.string.msg_share_cancel);
            }
        };
        wxManager.setListener(mShareStateListener);
        wbManager.setListener(mShareStateListener);
        qqManager.setListener(mShareStateListener);
    }

    private void initView() {
        if (mView == null) {
            return;
        }
        mViewPager = (ViewPager) mView.findViewById(R.id.vp_home);
        shareWindow = new ShareWindow(getActivity(), new OnShreBtnClickListener());
        int tmpLayout = SystemUtil.checkIfDeviceHasNavBar(getActivity()) ?
                R.layout.view_home_page2_with_navbar : R.layout.view_home_page2;
        views.add(LayoutInflater.from(getActivity()).inflate(tmpLayout, null));
        views.add(LayoutInflater.from(getActivity()).inflate(R.layout.view_home_page3, null));
        mViewPagerAdapter = new ViewPagerAdapter(views, null);
        mViewPager.setAdapter(mViewPagerAdapter);

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
        ivPage2BookBlur = (ImageView) views.get(0).findViewById(R.id.iv_book_blur_bg);
        tvPage2Summary = (TextView) views.get(0).findViewById(R.id.tv_page2_summary);

        mContainer = views.get(0).findViewById(R.id.fl_container);
        mCardView1 = (CardView) views.get(0).findViewById(R.id.cv_1);
        mCardView2 = (CardView) views.get(0).findViewById(R.id.cv_2);
        fabShare = (ImageButton) views.get(0).findViewById(R.id.fab_share);
        btnFlip = (Button) views.get(0).findViewById(R.id.btn_flip);
        mStartCardView = mCardView1;
        //设置默认显示信息
        tvPage2Author.setText(getString(R.string.tv_book_author));
        tvPage2Publisher.setText(getString(R.string.tv_book_publisher));
        tvPage2PYear.setText(getString(R.string.tv_book_pubdate));
        tvPage2ISBN.setText(getString(R.string.tv_book_isbn));
//        tvPage2Summary.setText("");
        //
        initConfig();

        //set cache
        Daily daily = (Daily) CacheFileUtils.readObject(Config.SZ_CACHE_FILE_PATH);
        if (daily != null) {
            onSetBookData(daily, true);
        }
        ListData tmpAnn = (ListData) CacheFileUtils.readObject(Config.ANN_CACHE_FILE_PATH);
        if (tmpAnn != null) {
            onSetAnnData(tmpAnn, true, true);
        }

        onUpdateTodayDaily();
    }

    /**
     * init config
     */
    private void initConfig(){
        mRefreshLayout = (SwipeRefreshLayout) views.get(0).findViewById(R.id.srl_home2);
        mAnnRecycleView = (RecyclerView) views.get(1).findViewById(R.id.rv_annlist);
        mAnnRecycleView.setLayoutManager(mLayoutManager);
        mAnnRecycleView.setHasFixedSize(false);
        mAnnRecycleView.setItemAnimator(new DefaultItemAnimator());
        mAdapter.setOnItemClickListener(new AnnotationAdapter.ViewHolder.ItemClickListener() {
            @Override public void onItemClick(View view, int position) {
                AnnDetailView.setTitle("《" + mAnns.get(position).getBook().getTitle() + "》"
                        + App.getContext().getResources().getString(R.string.tv_annotation));
                AnnDetailView.setWriteInfo(mAnns.get(position).getAuthor_user().getName()
                        + App.getContext().getResources().getString(R.string.tv_write)
                        + mAnns.get(position).getTime().substring(0, 10));
                AnnDetailView.setContent(mAnns.get(position).getContent());
                AnnDetailView.show(view, Gravity.CENTER, 0, 0);
            }
        });
        mAnnRecycleView.setAdapter(mAdapter);
        mAnnRecycleView.setOnScrollListener(
                new RecyclerView.OnScrollListener() {
                    @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            int lastVisibleItem = mLayoutManager.findLastCompletelyVisibleItemPosition();
                            int totalItemCount = mLayoutManager.getItemCount();
                            if (lastVisibleItem == totalItemCount - 1 && hasMore) {
                                onUpdateAnnotation(id);
                            }
                        }
                    }

                    @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                    }
                }
        );
        //
        svPage2Main.setVerticalScrollBarEnabled(false);//hide scrollbar
        //
        mSwipViewAnimation = null;// make SwipViewAnimation null in a fragment
        btnFlip.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
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
        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                shareWindow.show(mView, Gravity.CENTER, 0, 0);
            }
        });

        //
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                if (NetWorkUtils.getNetWorkType(App.getContext())
                        == NetWorkUtils.NETWORK_TYPE_DISCONNECT) {
                    SystemUtil.showToast(R.string.msg_no_internet);
                    mRefreshLayout.setRefreshing(false);
                    return;
                }
                if (mRefreshLayout.isRefreshing()) {
                    onUpdateTodayDaily();
                    page = 0;
                    hasMore = true;
                }
            }
        });

        //请求TOKEN设置回调监听
        mTokenUtils.setResponseListener(new TokenUtils.OnResponseListener() {
            @Override public void onFailure(String s) {
                SystemUtil.showToast(R.string.msg_request_error);
                mRefreshLayout.setRefreshing(false);
            }

            @Override public void onSuccess(Map<String, String> map) {
                final Map<String, String> tmpMap = map;
                JSONRequest<Daily> jsonRequest = new JSONRequest<Daily>(
                        Request.Method.POST,
                        Api.GET_TODAY_BOOK_URL,
                        Daily.class,
                        new Response.Listener<Daily>() {
                            @Override public void onResponse(Daily daily) {
                                onSetBookData(daily, false);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override public void onErrorResponse(VolleyError volleyError) {
                                SystemUtil.showToast(R.string.msg_request_error);
                                mRefreshLayout.setRefreshing(false);
                            }
                        }
                ) {
                    @Override protected Map<String, String> getParams() throws AuthFailureError {
                        return tmpMap;
                    }
                };
                App.getRequestQueue().add(jsonRequest);
            }
        });
    }

    /**
     * 请求更新今日书志，首先获取TOKEN，成功后回调请求书志信息
     */
    private void onUpdateTodayDaily() {
        mTokenUtils.getRequestParam();
    }

    /**
     * set book data
     * @param daily
     */
    private void onSetBookData(Daily daily, final boolean isCache) {
        if (daily == null) {
            mRefreshLayout.setRefreshing(false);
            return;
        }
        int status = daily.getStatus();
        if (status == 1) {
            final Book book = daily.getBook();
            Daily.Extra extra = daily.getExtra();
            if (book != null) {
                //write cache
                if (!isCache && !CacheFileUtils.saveObject(daily, Config.SZ_CACHE_FILE_PATH)) {
                    SystemUtil.showToast(R.string.msg_cache_error);
                }
                //set data
                String imgUrl = book.getImages().getLarge();
                if (!TextUtils.isEmpty(imgUrl)) {
                    /*Glide.with(App.getContext()).load(book.getImages().getLarge())
                            .into(ivPage2Image);
                    //book background blur
                    new Thread(new Runnable() {
                        @Override public void run() {
                            Bitmap bmp = WXUtil.getBitmapFromUrl(book.getImages().getLarge());
                            if (bmp != null){
                                bmp = SystemUtil.blurImage(getActivity(), bmp, Config.BLUR_RADIUS);
                                final Bitmap bmp2 = bmp;
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override public void run() {
                                        ivPage2BookBlur.setImageBitmap(bmp2);
                                    }
                                });
                            }
                        }
                    }).start();*/
                    Observable.just(imgUrl)
                            .map(new Func1<String, Bitmap[]>() {
                                @Override
                                public Bitmap[] call(String s) {
                                    Bitmap bmp = WXUtil.getBitmapFromUrl(s);
                                    return new Bitmap[]{bmp, SystemUtil.blurImage(
                                            getActivity(), bmp, Config.BLUR_RADIUS)};
                                }
                            })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<Bitmap[]>() {
                                @Override
                                public void call(Bitmap[] bitmaps) {
                                    ivPage2Image.setImageBitmap(bitmaps[0]);
                                    ivPage2BookBlur.setImageBitmap(bitmaps[1]);
                                }
                            });
                }
                if (extra != null) {
                    tvPage2Which.setText(App.getContext().getResources()
                            .getString(R.string.tv_vol) + extra.getVol());
                    tvPage2Sub.setText(extra.getBrief());
                    tvPage2YM.setText(extra.getYMD()[1] + "." + extra.getYMD()[0]);
                    tvPage2Day.setText(extra.getYMD()[2]);
                }
                tvPage2Title.setText(book.getTitle());
                String author = "";
                for (int j = 0; j < book.getAuthor().length; j++) {
                    author += book.getAuthor()[j] + "、";
                }
                if (author.length() > 0) {
                    author = author.substring(0, author.length() - 1);
                }
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
        } else {
            SystemUtil.showToast(R.string.msg_no_find);
        }
        mRefreshLayout.setRefreshing(false);
    }

    /**
     * update book annotation with book id
     */
    private void onUpdateAnnotation(String id) {
        if (TextUtils.isEmpty(id)) {
            SystemUtil.showToast(R.string.msg_book_id_null);
            return;
        }
        JSONRequest<ListData> jsonRequest = new JSONRequest<ListData>(
                Api.DOUBAN_HOST + id + "/annotations" + "?page=" + page + "&" + Api.API_KEY,
                ListData.class,
                new Response.Listener<ListData>() {
                    @Override
                    public void onResponse(ListData listData) {
                        if (page == 0) {
                            onSetAnnData(listData, true, false);
                        } else {
                            onSetAnnData(listData, false, false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        SystemUtil.showToast(R.string.msg_find_error);
                    }
                }
        );
        App.getRequestQueue().add(jsonRequest);
    }

    /**
     * set Annotation list data
     *
     * @param listData
     * @param isUpdate
     * @param isCache
     */
    private void onSetAnnData(ListData listData, boolean isUpdate, boolean isCache) {
        if (listData != null) {
            if (isUpdate) {// update
                if (!isCache && !CacheFileUtils.saveObject(listData, Config.ANN_CACHE_FILE_PATH)) {
                    SystemUtil.showToast(R.string.msg_cache_error);
                }
                mAnns.clear();
            }
            if (listData.getAnnotations() != null) {
                if (listData.getAnnotations().size() <= 0) {
                    if (!isUpdate){
                        hasMore = false;
                    }
                    SystemUtil.showToast(R.string.msg_no_find);
                    return;
                } else {
                    if (!isCache) {
                        page++;
                    }
                }
                mAnns.addAll(listData.getAnnotations());
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * share to platform
     * @param plat
     */
    private void shareTo(final int plat){
        shareWindow.hide();
        new Thread(new Runnable() {
            @Override public void run() {
                mContainer.setDrawingCacheEnabled(true);
                mContainer.destroyDrawingCache();
                // destory cache
//                mContainer.destroyDrawingCache();
//                mContainer.setDrawingCacheEnabled(false);
                Bitmap bmp = mContainer.getDrawingCache();
                if (bmp == null || !ImageUtils.checkSDCardAvailable()){
                    SystemUtil.showToast(R.string.msg_img_not_found);
                    return;
                }
                String path = Environment.getExternalStorageDirectory() + Config.IMG_PATH;
                ImageUtils.savePhotoToSDCard(bmp, path, Config.SHARE_IMG_NAME);
                switch (plat){
                    case R.id.btn_share_wechatf:
                        shareToWX(WXShareContent.WXSession, path, Config.SHARE_IMG_NAME);
                        break;

                    case R.id.btn_share_wechatt:
                        shareToWX(WXShareContent.WXTimeline, path, Config.SHARE_IMG_NAME);
                        break;

                    case R.id.btn_share_weibo:
                        shareToWB(path, Config.SHARE_IMG_NAME);
                        break;

                    case R.id.btn_share_qqf:
                        shareToQQ(QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE,
                                path, Config.SHARE_IMG_NAME);
                        break;

                    case R.id.btn_share_qqt:
                        shareToQQ(QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN,
                                path, Config.SHARE_IMG_NAME);
                        break;

                    default:break;
                }
            }
        }).start();
    }

    /**
     * share to weixin
     * @param scene
     * @param path
     * @param name
     */
    private void shareToWX(int scene, String path, String name){
        WXShareContent content = new WXShareContent();
        content.setScene(scene).setType(WXShareContent.share_type.Image).setImage_url(path + name);
        wxManager.share(content);
    }

    /**
     * share to weibo
     * @param path
     * @param name
     */
    private void shareToWB(String path, String name){
        WBShareContent content = new WBShareContent();
        content.setShare_method(WBShareContent.COMMON_SHARE)
                .setStatus(getString(R.string.share_copy))
                .setImage_path(path + name);
        wbManager.share(content);
    }

    /**
     * share to qq or qzone
     * @param scene
     * @param path
     * @param name
     */
    private void shareToQQ(int scene, String path, String name){
        QQShareContent content = new QQShareContent();
        content.setShareType(QQShare.SHARE_TO_QQ_TYPE_IMAGE)
                .setShareExt(scene)
                .setImage_path(path + name);
        qqManager.share(content);
    }

    class OnShreBtnClickListener implements View.OnClickListener {

        @Override public void onClick(View v) {
            shareTo(v.getId());
        }
    }
}
