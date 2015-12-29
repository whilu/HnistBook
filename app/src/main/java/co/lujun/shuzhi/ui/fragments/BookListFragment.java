package co.lujun.shuzhi.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import co.lujun.shuzhi.App;
import co.lujun.shuzhi.BuildConfig;
import co.lujun.shuzhi.R;
import co.lujun.shuzhi.bean.Book;
import co.lujun.shuzhi.bean.Config;
import co.lujun.shuzhi.bean.ListData;
import co.lujun.shuzhi.ui.BookDetailActivity;
import co.lujun.shuzhi.ui.adapter.BookAdapter;
import co.lujun.shuzhi.util.IntentUtils;
import co.lujun.shuzhi.util.NetWorkUtils;
import co.lujun.shuzhi.util.SystemUtil;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lujun on 2015/3/17.
 */
public class BookListFragment extends BaseFragment {

    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecycleView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View mView;

    private BookAdapter mAdapter;
    private Bundle mBundle;
    private Intent mBookDetailIntent;
    private List<Book> mBooks;

    private static final String TAG = "BookListFragment";
    private String keyword = "";
    private int start = 0;
    private boolean hasMore = true;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
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
            mRecycleView.setHasFixedSize(true);
            mRecycleView.setItemAnimator(new DefaultItemAnimator());
            mAdapter.setOnItemClickListener(new BookAdapter.ViewHolder.ItemClickListener() {
                @Override public void onItemClick(View view, int position) {
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
            mRecycleView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        int lastVisibleItem = mLayoutManager.findLastCompletelyVisibleItemPosition();
                        int totalItemCount = mLayoutManager.getItemCount();
                        if (lastVisibleItem == totalItemCount - 1 && hasMore) {
                            onRequestData(false);
                        }
                    }
                }

                @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }
            });
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override public void onRefresh() {
                    hasMore = true;
                    onRequestData(true);
                }
            });

            Intent intent =  getActivity().getIntent();
            Bundle bundle;
            if (intent == null || (bundle = intent.getExtras()) == null
                    || TextUtils.isEmpty(keyword = bundle.getString(Config.BOOK_LST_SEARCH_KEY))){
                return;
            }
            mSwipeRefreshLayout.post(new Runnable() {
                @Override public void run() {
                    mSwipeRefreshLayout.setRefreshing(true);
                    onRequestData(true);
                }
            });
        }
    }

    private void onRequestData(final boolean isUpdate){
        if (NetWorkUtils.getNetWorkType(getActivity()) == NetWorkUtils.NETWORK_TYPE_DISCONNECT){
            SystemUtil.showToast(R.string.msg_no_internet);
            mSwipeRefreshLayout.setRefreshing(false);
            return;
        }
        if (isUpdate) start = 0;
        App.getDbApiService()
                .getDbBookList(keyword, start, BuildConfig.DB_TOKEN)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ListData>() {
                    @Override public void onCompleted() {}

                    @Override public void onError(Throwable e) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        SystemUtil.showToast(R.string.msg_find_error);
                    }

                    @Override public void onNext(ListData listData) {
                        setData(listData, isUpdate);
                    }
                });
    }

    private void setData(ListData listData, boolean isUpdate){
        mSwipeRefreshLayout.setRefreshing(false);
        if (listData == null || listData.getBooks() == null) {
            SystemUtil.showToast(R.string.msg_no_find);
            return;
        }
        if (isUpdate) mBooks.clear();
        start += listData.getCount();

        if (listData.getBooks().size() <= 0){
            if (!isUpdate){
                hasMore = false;
            }
            SystemUtil.showToast(R.string.msg_no_find);
            return;
        }
        mBooks.addAll(listData.getBooks());
        mAdapter.notifyDataSetChanged();
    }
}