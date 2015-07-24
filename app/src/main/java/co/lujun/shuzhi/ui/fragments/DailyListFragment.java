package co.lujun.shuzhi.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import co.lujun.shuzhi.GlApplication;
import co.lujun.shuzhi.R;
import co.lujun.shuzhi.bean.Config;
import co.lujun.shuzhi.bean.Daily;
import co.lujun.shuzhi.bean.JSONRequest;
import co.lujun.shuzhi.bean.ListData;
import co.lujun.shuzhi.ui.BookDetailActivity;
import co.lujun.shuzhi.ui.adapter.DailyAdapter;
import co.lujun.shuzhi.util.IntentUtils;
import co.lujun.shuzhi.util.NetWorkUtils;
import co.lujun.shuzhi.util.TokenUtils;

/**
 * Created by lujun on 2015/7/17.
 */
public class DailyListFragment extends Fragment {

    private View mView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecycleView;
    private LinearLayoutManager mLayoutManager;
    private DailyAdapter mAdapter;
    private List<Daily> mDailies;
    private Intent mDailyDetailIntent;
    private Bundle mBundle;
    private TokenUtils mTokenUtils;

    private String mUrl = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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
            mRecycleView.setHasFixedSize(true);// 若每个item的高度固定，设置此项可以提高性能
            mRecycleView.setItemAnimator(new DefaultItemAnimator());// item 动画效果
            mAdapter.setOnItemClickListener(new DailyAdapter.ViewHolder.ItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
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
                @Override
                public void onRefresh() {
                    onUpdate(mUrl);
                }
            });
            if (getActivity().getIntent() != null){
                Bundle bundle = getActivity().getIntent().getExtras();
                if (bundle != null){
                    mUrl = bundle.getString(Config.DAILY_LST_TYPE);
                    if (!TextUtils.isEmpty(mUrl)){
                        mSwipeRefreshLayout.post(new Runnable() {
                            @Override
                            public void run() {
                                mSwipeRefreshLayout.setRefreshing(true);
                                onUpdate(mUrl);
                            }
                        });
                    }else {
                        Toast.makeText(getActivity(),
                                getResources().getString(R.string.msg_param_null),
                                Toast.LENGTH_SHORT).show();
                    }
                }else {
                    /*Toast.makeText(getActivity(),
                            getResources().getString(R.string.msg_intent_extras_null),
                            Toast.LENGTH_SHORT).show();*/
                    Toast.makeText(GlApplication.getContext(),
                            getResources().getString(R.string.msg_param_null),
                            Toast.LENGTH_SHORT).show();
                }
            }else {
                /*Toast.makeText(getActivity(),
                        getResources().getString(R.string.msg_intent_null),
                        Toast.LENGTH_SHORT).show();*/
                Toast.makeText(GlApplication.getContext(),
                        getResources().getString(R.string.msg_param_null),
                        Toast.LENGTH_SHORT).show();
            }
        }
        //请求TOKEN设置回调监听
        mTokenUtils.setResponseListener(new TokenUtils.OnResponseListener() {
            @Override
            public void onFailure(String s) {
                Toast.makeText(GlApplication.getContext(), s, Toast.LENGTH_SHORT).show();
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onSuccess(Map<String, String> map) {
                final Map<String, String> tmpMap = map;
                JSONRequest<ListData> jsonRequest = new JSONRequest<ListData>(
                        Request.Method.POST,
                        mUrl,
                        ListData.class,
                        new Response.Listener<ListData>() {
                            @Override
                            public void onResponse(ListData listData) {
                                setData(listData);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Toast.makeText(GlApplication.getContext(),
                                        getResources().getString(R.string.msg_request_error),
                                        Toast.LENGTH_SHORT).show();
                                mSwipeRefreshLayout.setRefreshing(false);
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
    }

    /**
     * 更新信息
     * @param url
     */
    private void onUpdate(String url){
        if (TextUtils.isEmpty(url)){
            Toast.makeText(getActivity(),
                    getResources().getString(R.string.msg_param_null),
                    Toast.LENGTH_SHORT).show();
            mSwipeRefreshLayout.setRefreshing(false);
            return;
        }
        if (NetWorkUtils.getNetWorkType(getActivity()) == NetWorkUtils.NETWORK_TYPE_DISCONNECT){
            Toast .makeText(getActivity(), getResources().getString(R.string.msg_no_internet),
                    Toast.LENGTH_SHORT).show();
            mSwipeRefreshLayout.setRefreshing(false);
            return;
        }
        if (mSwipeRefreshLayout.isRefreshing()) {//检查是否正在刷新
            mTokenUtils.getRequestParam();
        }
    }

    private void setData(ListData listData){
        if (listData != null && listData.getDailies() != null){
            mDailies.clear();

            if (listData.getDailies().size() <= 0){
                Toast .makeText(getActivity(), getResources().getString(R.string.msg_no_find),
                        Toast.LENGTH_SHORT).show();
                mSwipeRefreshLayout.setRefreshing(false);
                return;
            }
            mDailies.addAll(listData.getDailies());
            mAdapter.notifyDataSetChanged();
        }else {
            Toast .makeText(getActivity(), getResources().getString(R.string.msg_no_find),
                    Toast.LENGTH_SHORT).show();
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(DailyListFragment.class.getName());
        MobclickAgent.onResume(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(DailyListFragment.class.getName());
        MobclickAgent.onPause(getActivity());
    }
}
