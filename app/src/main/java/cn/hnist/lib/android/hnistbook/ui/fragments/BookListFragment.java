package cn.hnist.lib.android.hnistbook.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.hnist.lib.android.hnistbook.R;
import cn.hnist.lib.android.hnistbook.model.Book;
import cn.hnist.lib.android.hnistbook.ui.adapter.BookAdapter;

/**
 * Created by lujun on 2015/3/17.
 */
public class BookListFragment extends Fragment {

    private View mView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecycleView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private List<Book> mBooks;

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
        mBooks = new ArrayList<Book>();
    }

    private void initView(){
        if (mView != null){
            mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.srl_booklist);
            mRecycleView = (RecyclerView) mView.findViewById(R.id.rv_booklist);
            mRecycleView.setLayoutManager(mLayoutManager);
            mRecycleView.setHasFixedSize(true);// 若每个item的高度固定，设置此项可以提高性能
            for (int i = 0; i < 4; i++){
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
            mRecycleView.setAdapter(mAdapter);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    /*new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Book book = new Book();
                            book.setImgUrl("");
                            book.setTitle("平凡的世界");
                            book.setAuthor("路遥");
                            book.setPublisher("北京十月文艺出版社");
                            book.setPublishDate("2001-01-01");
                            book.setIsbn("98768654322");
                            mBooks.add(book);
                            try{
                                Thread.sleep(2000);
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    }).start();*/
                }
            });
        }
    }
}
