package co.lujun.shuzhi.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.lujun.shuzhi.App;
import co.lujun.shuzhi.R;
import co.lujun.shuzhi.bean.Config;
import co.lujun.shuzhi.bean.Daily;
import co.lujun.shuzhi.bean.ListData;
import co.lujun.shuzhi.bean.Token;
import co.lujun.shuzhi.ui.BookDetailActivity;
import co.lujun.shuzhi.ui.adapter.DailyAdapter;
import co.lujun.shuzhi.util.IntentUtils;
import co.lujun.shuzhi.util.NetWorkUtils;
import co.lujun.shuzhi.util.SignatureUtils;
import co.lujun.shuzhi.util.SystemUtil;
import co.lujun.shuzhi.util.TokenUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by lujun on 2015/7/17.
 */
public class DailyListFragment extends BaseFragment {

    private RecyclerView mRecycleView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View mView;
    private LinearLayoutManager mLayoutManager;

    private Bundle mBundle;
    private DailyAdapter mAdapter;
    private Intent mDailyDetailIntent;
    private List<Daily> mDailies;
    private TokenUtils mTokenUtils;

    private String mTime;

    private static final String TAG = "DailyListFragment";

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_dailylist, null);
        initView();
        return mView;
    }

    private void init(){
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mDailies = new ArrayList<Daily>();
        mAdapter = new DailyAdapter(mDailies);
        mBundle = new Bundle();
        mTokenUtils = new TokenUtils();
        mDailyDetailIntent = new Intent(getActivity(), BookDetailActivity.class);
    }

    private void initView(){
        if (mView != null){
            mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.srl_dailylist);
            mRecycleView = (RecyclerView) mView.findViewById(R.id.rv_dailylist);
            mRecycleView.setLayoutManager(mLayoutManager);
            mRecycleView.setHasFixedSize(true);
            mRecycleView.setItemAnimator(new DefaultItemAnimator());
            mAdapter.setOnItemClickListener(new DailyAdapter.ViewHolder.ItemClickListener() {
                @Override public void onItemClick(View view, int position) {
                    mBundle.clear();
                    mBundle.putString(Config.BOOK.title.toString(),
                            ((Daily) view.getTag()).getBook().getTitle());
                    mBundle.putString(Config.BOOK.isbn13.toString(),
                            (((Daily) view.getTag()).getBook().getIsbn13()));
                    mBundle.putString(Config.BOOK.isbn10.toString(),
                            (((Daily) view.getTag()).getBook().getIsbn10()));
                    mDailyDetailIntent.putExtras(mBundle);
                    IntentUtils.startPreviewActivity(getActivity(), mDailyDetailIntent);
                }
            });
            mRecycleView.setAdapter(mAdapter);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override public void onRefresh() {
                    OnRequestData();
                }
            });
            
            Intent intent = getActivity().getIntent();
            Bundle bundle;
            if (intent == null || (bundle = intent.getExtras()) == null 
                    || TextUtils.isEmpty((mTime = bundle.getString(Config.DAILY_CHANNEL)))){
                return;
            }
            mSwipeRefreshLayout.post(new Runnable() {
                @Override public void run() {
                    mSwipeRefreshLayout.setRefreshing(true);
                    OnRequestData();
                }
            });
        }
    }

    private void OnRequestData(){
        if (NetWorkUtils.getNetWorkType(getActivity()) == NetWorkUtils.NETWORK_TYPE_DISCONNECT){
            SystemUtil.showToast(R.string.msg_no_internet);
            mSwipeRefreshLayout.setRefreshing(false);
            return;
        }
        App.getSzApiService()
                .getSzToken()
                .flatMap(new Func1<Token, Observable<ListData>>() {
                    @Override public Observable<ListData> call(Token token) {
                        return getDailyData(token);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ListData>() {
                    @Override public void onCompleted() {}

                    @Override public void onError(Throwable e) {
                        SystemUtil.showToast(R.string.msg_request_error);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override public void onNext(ListData listData) {
                        setData(listData);
                    }
                });
    }

    // IO thread
    private Observable<ListData> getDailyData(Token token){
        if (token == null || token.getStatus() != 1){
            return null;
        }
        String timeStamp = String.valueOf(System.currentTimeMillis());
        Map<String, String> map = new HashMap<String, String>();
        map.put("sign", timeStamp);
        String sign = SignatureUtils.makeSignature(token.getData(), map);
        Log.d(TAG, timeStamp + ", " + sign + ", " + token.getData());
        return App.getSzApiService().getSzBookList(mTime, timeStamp, sign);
    }

    private void setData(ListData listData){
        mSwipeRefreshLayout.setRefreshing(false);
        if (listData == null || listData.getDailies() == null || listData.getDailies().size() <= 0){
            SystemUtil.showToast(R.string.msg_no_find);
            mSwipeRefreshLayout.setRefreshing(false);
            return;
        }
        mDailies.clear();
        mDailies.addAll(listData.getDailies());
        mAdapter.notifyDataSetChanged();
    }
}