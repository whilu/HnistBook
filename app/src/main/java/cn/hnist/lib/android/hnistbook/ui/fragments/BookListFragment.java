package cn.hnist.lib.android.hnistbook.ui.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.hnist.lib.android.hnistbook.R;
import cn.hnist.lib.android.hnistbook.bean.Book;
import cn.hnist.lib.android.hnistbook.bean.Constant;
import cn.hnist.lib.android.hnistbook.ui.BookDetailActivity;
import cn.hnist.lib.android.hnistbook.ui.adapter.BookAdapter;
import cn.hnist.lib.android.hnistbook.util.IntentUtils;

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
    private Bundle mBundle;

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
        if (getActivity().getIntent() != null){
            mBundle = getActivity().getIntent().getExtras();
            Toast.makeText(getActivity(), mBundle.getString(Constant.BOOK_LST_TEST_KEY), Toast.LENGTH_SHORT).show();
        }
    }

    private void initView(){
        if (mView != null){
            mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.srl_booklist);
            mRecycleView = (RecyclerView) mView.findViewById(R.id.rv_booklist);
            mRecycleView.setLayoutManager(mLayoutManager);
            mRecycleView.setHasFixedSize(true);// 若每个item的高度固定，设置此项可以提高性能
            mRecycleView.setItemAnimator(new DefaultItemAnimator());// item 动画效果
            for (int i = 0; i < 2; i++){
                Book book = new Book();
                book.setImgUrl("");
                book.setTitle("平凡的世界" + i);
                book.setAuthor("路遥" + i);
                book.setPublisher("北京十月文艺出版社" + i);
                book.setPublishDate("2001-01-01" + i);
                book.setIsbn("98768654322" + ":" + i);
                mBooks.add(book);
            }
            mAdapter = new BookAdapter(mBooks);
            mAdapter.setOnItemClickListener(new BookAdapter.ViewHolder.ItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    IntentUtils.startPreviewActivity(getActivity(), new Intent(getActivity(), BookDetailActivity.class));
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
                    onUpdate();
                }
            });
        }
    }

    private void onUpdate(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mSwipeRefreshLayout.isRefreshing()) {//检查是否正在刷新
                    Book book = new Book();
                    book.setImgUrl("");
                    book.setTitle("平凡的世界");
                    book.setAuthor("路遥");
                    book.setPublisher("北京十月文艺出版社");
                    book.setPublishDate("2001-01-01");
                    book.setIsbn("98768654322");
                    mBooks.add(book);
                    mAdapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        }, 2000);
    }

    private void onLoadMore(){
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Book book = new Book();
                book.setImgUrl("");
                book.setTitle("平凡的世界xxx");
                book.setAuthor("路遥xxx");
                book.setPublisher("北京十月文艺出版社xxx");
                book.setPublishDate("2001-01-0xxx1");
                book.setIsbn("98768654322xx");
                mBooks.add(book);
                mAdapter.notifyDataSetChanged();
            }
        }, 2000);
    }
}
