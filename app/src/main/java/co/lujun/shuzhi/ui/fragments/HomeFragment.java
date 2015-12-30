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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tencent.connect.share.QQShare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.lujun.shuzhi.App;
import co.lujun.shuzhi.BuildConfig;
import co.lujun.shuzhi.R;
import co.lujun.shuzhi.anim.SwipeViewAnimation;
import co.lujun.shuzhi.bean.Annotation;
import co.lujun.shuzhi.bean.Book;
import co.lujun.shuzhi.bean.Config;
import co.lujun.shuzhi.bean.Daily;
import co.lujun.shuzhi.bean.ListData;
import co.lujun.shuzhi.bean.Token;
import co.lujun.shuzhi.ui.adapter.AnnotationAdapter;
import co.lujun.shuzhi.ui.adapter.ViewPagerAdapter;
import co.lujun.shuzhi.ui.listener.ItemClickListener;
import co.lujun.shuzhi.ui.widget.AnnDetailView;
import co.lujun.shuzhi.ui.widget.ShareWindow;
import co.lujun.shuzhi.util.CacheFileUtils;
import co.lujun.shuzhi.util.ImageUtils;
import co.lujun.shuzhi.util.NetWorkUtils;
import co.lujun.shuzhi.util.SignatureUtils;
import co.lujun.shuzhi.util.SystemUtil;
import co.lujun.tpsharelogin.bean.QQShareContent;
import co.lujun.tpsharelogin.bean.WBShareContent;
import co.lujun.tpsharelogin.bean.WXShareContent;
import co.lujun.tpsharelogin.listener.StateListener;
import co.lujun.tpsharelogin.platform.qq.QQManager;
import co.lujun.tpsharelogin.platform.weibo.WBManager;
import co.lujun.tpsharelogin.platform.weixin.WXManager;
import co.lujun.tpsharelogin.utils.WXUtil;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by lujun on 2015/3/9.
 */
public class HomeFragment extends BaseFragment {

    private Button btnFlip;
    private ImageButton fabShare;
    private CardView mCardView1, mCardView2;
    private ImageView ivPage2Image, ivPage2BookBlur;
    private RecyclerView mAnnRecycleView;
    private ScrollView svPage2Main;
    private SwipeRefreshLayout mRefreshLayout;
    private TextView tvPage2Author, tvPage2PYear, tvPage2Publisher, tvPage2ISBN, tvPage2Which,
            tvPage2Title, tvPage2Sub, tvPage2Day, tvPage2YM, tvPage2Summary;
    private View mView, mContainer;
    private ViewPager mViewPager;

    private AnnotationAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private SwipeViewAnimation mSwipeViewAnimation;
    private StateListener mShareStateListener;
    private ViewPagerAdapter mViewPagerAdapter;
    private ShareWindow shareWindow;
    private WXManager wxManager;
    private WBManager wbManager;
    private QQManager qqManager;

    private List<Annotation> mAnns;
    private List<View> views;

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
        shareWindow = new ShareWindow(getActivity(), new OnShareBtnClickListener());
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
        mRefreshLayout = (SwipeRefreshLayout) views.get(0).findViewById(R.id.srl_home2);
        mAnnRecycleView = (RecyclerView) views.get(1).findViewById(R.id.rv_annlist);
        mRefreshLayout = (SwipeRefreshLayout) views.get(0).findViewById(R.id.srl_home2);
        mAnnRecycleView = (RecyclerView) views.get(1).findViewById(R.id.rv_annlist);
        //
        initExtra();
        loadLocalData();
    }

    /**
     * init config
     */
    private void initExtra(){
        mAnnRecycleView.setLayoutManager(mLayoutManager);
        mAnnRecycleView.setHasFixedSize(false);
        mAnnRecycleView.setItemAnimator(new DefaultItemAnimator());
        mAdapter.setOnItemClickListener(new ItemClickListener() {
            @Override public void onItemClick(View view, int position) {
                showAnnotation(position, view);
            }
        });
        mAnnRecycleView.setAdapter(mAdapter);
        mAnnRecycleView.setOnScrollListener(new RecyclerView.OnScrollListener() {
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
        mSwipeViewAnimation = null;// make SwipViewAnimation null in a fragment
        btnFlip.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (mSwipeViewAnimation == null) {
                    mSwipeViewAnimation = new SwipeViewAnimation(mContainer, mCardView1, mCardView2);
                }
                if (0 == mSwipeViewAnimation.getIndex() % 2) {
                    mSwipeViewAnimation.applyRotation(0, 90);
                } else {
                    mSwipeViewAnimation.applyRotation(0, -90);
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
                onRequestData();
            }
        });
    }

    private void loadLocalData(){
        Daily daily = (Daily) CacheFileUtils.readObject(Config.SZ_CACHE_FILE_PATH);
        if (daily != null) {
            onSetBookData(daily, true);
        }
        ListData tmpAnn = (ListData) CacheFileUtils.readObject(Config.ANN_CACHE_FILE_PATH);
        if (tmpAnn != null) {
            onSetAnnData(tmpAnn, true, true);
        }

        onRequestData();
    }

    private void showAnnotation(int position, View parentVIew){
        AnnDetailView.setTitle("《" + mAnns.get(position).getBook().getTitle() + "》"
                + App.getContext().getResources().getString(R.string.tv_annotation));
        AnnDetailView.setWriteInfo(mAnns.get(position).getAuthor_user().getName()
                + App.getContext().getResources().getString(R.string.tv_write)
                + mAnns.get(position).getTime().substring(0, 10));
        AnnDetailView.setContent(mAnns.get(position).getContent());
        AnnDetailView.show(parentVIew, Gravity.CENTER, 0, 0);
    }

    private void onRequestData(){
        if (NetWorkUtils.getNetWorkType(getActivity()) == NetWorkUtils.NETWORK_TYPE_DISCONNECT){
            SystemUtil.showToast(R.string.msg_no_internet);
            mRefreshLayout.setRefreshing(false);
            return;
        }
        page = 0;
        hasMore = true;
        App.getSzApiService()
                .getSzToken()
                .flatMap(new Func1<Token, Observable<Daily>>() {
                    @Override public Observable<Daily> call(Token token) {
                        return getDaily(token);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Daily>() {
                    @Override public void onCompleted() {}

                    @Override public void onError(Throwable e) {
                        SystemUtil.showToast(R.string.msg_request_error);
                        mRefreshLayout.setRefreshing(false);
                    }

                    @Override public void onNext(Daily daily) {
                        onSetBookData(daily, false);
                    }
                });
    }

    private Observable<Daily> getDaily(Token token){
        if (token == null || token.getStatus() != 1){
            return null;
        }
        String timeStamp = String.valueOf(System.currentTimeMillis());
        Map<String, String> map = new HashMap<String, String>();
        map.put("timestamp", timeStamp);
        String sign = SignatureUtils.makeSignature(token.getData(), map);
        return App.getSzApiService().getSzDaily(timeStamp, sign);
    }

    /**
     * set book data
     * @param daily
     */
    private void onSetBookData(Daily daily, final boolean isCache) {
        mRefreshLayout.setRefreshing(false);
        if (daily == null) {
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
                            .subscribe(new Subscriber<Bitmap[]>() {
                                @Override public void onCompleted() {}

                                @Override public void onError(Throwable e) {}

                                @Override public void onNext(Bitmap[] bitmaps) {
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
        }
    }

    /**
     * update book annotation with book id
     */
    private void onUpdateAnnotation(String id) {
        if (TextUtils.isEmpty(id)) {
            return;
        }
        App.getDbApiService()
                .getDbAnnotationList(id, page, BuildConfig.DB_TOKEN)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ListData>() {
                    @Override public void onCompleted() {}

                    @Override public void onError(Throwable e) {}

                    @Override public void onNext(ListData listData) {
                        if (page == 0) {
                            onSetAnnData(listData, true, false);
                        } else {
                            onSetAnnData(listData, false, false);
                        }
                    }
                });
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

    class OnShareBtnClickListener implements View.OnClickListener {

        @Override public void onClick(View v) {
            shareTo(v.getId());
        }
    }
}
