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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import co.lujun.shuzhi.GlApplication;
import co.lujun.shuzhi.R;
import co.lujun.shuzhi.api.Api;
import co.lujun.shuzhi.bean.Book;
import co.lujun.shuzhi.bean.Config;
import co.lujun.shuzhi.bean.DbBookData;
import co.lujun.shuzhi.bean.JSONRequest;
import co.lujun.shuzhi.ui.BookDetailActivity;
import co.lujun.shuzhi.ui.adapter.BookAdapter;
import co.lujun.shuzhi.util.IntentUtils;
import co.lujun.shuzhi.util.NetWorkUtils;

/**
 * Created by lujun on 2015/3/17.
 */
public class BookListFragment extends Fragment {

    private View mView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecycleView;
    private LinearLayoutManager mLayoutManager;
    private BookAdapter mAdapter;
    private List<Book> mBooks;
    private Intent mBookDetailIntent;
    private Bundle mBundle;
    private String keyword = "";
    private String mUrl = "";
    private int mType = -1;// mType = 1,获取搜索list, mType = 2, 获取七天内的list, mType = 3, 获取一月之内的list
    private int start = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_booklist, null);
        initView();
        return mView;
    }

    private void init(){
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBooks = new ArrayList<Book>();
        mAdapter = new BookAdapter(mBooks);
        mBundle = new Bundle();
        mBookDetailIntent = new Intent(getActivity(), BookDetailActivity.class);
    }

    private void initView(){
        if (mView != null){
            mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.srl_booklist);
            mRecycleView = (RecyclerView) mView.findViewById(R.id.rv_booklist);
            mRecycleView.setLayoutManager(mLayoutManager);
            mRecycleView.setHasFixedSize(true);// 若每个item的高度固定，设置此项可以提高性能
            mRecycleView.setItemAnimator(new DefaultItemAnimator());// item 动画效果
            mAdapter.setOnItemClickListener(new BookAdapter.ViewHolder.ItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    mBundle.clear();
                    mBundle.putString(Config.BOOK.title.toString(),
                            ((Book) view.getTag()).getTitle());
                    mBundle.putString(Config.BOOK.isbn13.toString(),
                            (((Book) view.getTag()).getIsbn13()));
                    mBundle.putString(Config.BOOK.isbn10.toString(),
                            (((Book) view.getTag()).getIsbn10()));
                    mBookDetailIntent.putExtras(mBundle);
                        IntentUtils.startPreviewActivity(getActivity(), mBookDetailIntent);
                }
            });
            mRecycleView.setAdapter(mAdapter);
            mRecycleView.setOnScrollListener(
                    new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                            if (newState == RecyclerView.SCROLL_STATE_IDLE){
                                int lastVisibleItem = mLayoutManager.findLastCompletelyVisibleItemPosition();
                                int totalItemCount = mLayoutManager.getItemCount();

                                if (lastVisibleItem == totalItemCount - 1){
                                    onLoadMore();
                                }
                            }
                        }

                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                        }
                    }
            );
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    onUpdate(keyword);
                }
            });
            if (getActivity().getIntent() != null){
                Bundle bundle = getActivity().getIntent().getExtras();
                if (bundle != null){
                    mType = bundle.getInt(Config.BOOK_LST_SEARCH_TYPE);
                    keyword = bundle.getString(Config.BOOK_LST_SEARCH_KEY);
                    if (mType == -1){
                        /*Toast.makeText(GlApplication.getContext(),
                                getResources().getString(R.string.msg_intent_extras_null),
                                Toast.LENGTH_SHORT).show();*/
                        Toast.makeText(GlApplication.getContext(),
                                getResources().getString(R.string.msg_param_null),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }else if (mType == 1){// 搜索list
                        mUrl = Api.BOOK_SEARCH_URL;
                    }else if (mType == 2){// 七天list
                        mUrl = Api.BOOK_SEARCH_URL;
                    }else if (mType == 3){// 一月list
                        mUrl = Api.BOOK_SEARCH_URL;
                    }
                    if (!TextUtils.isEmpty(keyword)){
                        try{
                            keyword = URLEncoder.encode(keyword, "UTF-8");// 若关键字是中文，编码
                            mSwipeRefreshLayout.post(new Runnable() {
                                @Override
                                public void run() {
                                    mSwipeRefreshLayout.setRefreshing(true);
                                    onUpdate(keyword);
                                }
                            });
                        }catch (UnsupportedEncodingException e){
                            e.printStackTrace();
                        }
                    }else {
                        Toast.makeText(getActivity(),
                                getResources().getString(R.string.msg_key_word_null),
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
    }

    private void onUpdate(String keyword){
        if (TextUtils.isEmpty(keyword)){
            Toast.makeText(getActivity(),
                    getResources().getString(R.string.msg_key_word_null),
                    Toast.LENGTH_SHORT).show();
            onUpdateComplete();
            return;
        }
        if (NetWorkUtils.getNetWorkType(getActivity()) == NetWorkUtils.NETWORK_TYPE_DISCONNECT){
            Toast .makeText(getActivity(), getResources().getString(R.string.msg_no_internet),
                    Toast.LENGTH_SHORT).show();
            onUpdateComplete();
            return;
        }
        if (mSwipeRefreshLayout.isRefreshing()) {//检查是否正在刷新
            JSONRequest<DbBookData> jsonRequest = new JSONRequest<DbBookData>(
                    mUrl + "?q=" + keyword + "&start=0",
                    DbBookData.class,
                    new Response.Listener<DbBookData>() {
                        @Override
                        public void onResponse(DbBookData dbBookData) {
                            setData(dbBookData, true);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            onUpdateComplete();
                            Toast .makeText(GlApplication.getContext(), volleyError.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            /*Toast .makeText(getActivity(),
                                    getResources().getString(R.string.msg_find_error),
                                    Toast.LENGTH_SHORT).show();*/
                        }
                    }
            );
            GlApplication.getRequestQueue().add(jsonRequest);
        }
    }

    private void onLoadMore(){
        JSONRequest<DbBookData> jsonRequest = new JSONRequest<DbBookData>(
                mUrl + "?q=" + keyword + "&start=" + start,
                DbBookData.class,
                new Response.Listener<DbBookData>() {
                    @Override
                    public void onResponse(DbBookData dbBookData) {
                        setData(dbBookData, false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        onUpdateComplete();
                        Toast .makeText(getActivity(), volleyError.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
        );
        GlApplication.getRequestQueue().add(jsonRequest);
    }

    private void setData(DbBookData dbBookData, boolean isUpdate){
        if (dbBookData != null){
            if (isUpdate){// update
                mBooks.clear();
            }
            start += dbBookData.getCount();

            if (dbBookData.getBooks().size() <= 0){
                Toast .makeText(getActivity(), getResources().getString(R.string.msg_no_find),
                        Toast.LENGTH_SHORT).show();
                onUpdateComplete();
                return;
            }
            mBooks.addAll(dbBookData.getBooks());
            mAdapter.notifyDataSetChanged();
        }
        onUpdateComplete();
    }

    private void onUpdateComplete(){
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(BookListFragment.class.getName());
        MobclickAgent.onResume(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(BookListFragment.class.getName());
        MobclickAgent.onPause(getActivity());
    }
}
