package cn.hnist.lib.android.hnistbook.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.hnist.lib.android.hnistbook.R;
import cn.hnist.lib.android.hnistbook.model.Book;

/**
 * Created by lujun on 2015/3/1.
 */
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private List<Book> mBooks;
    private ViewHolder.ItemClickListener mItemClickListener;

    public BookAdapter(List<Book> books) {
        this.mBooks = books;
    }

    // 创建View，被LayoutManager调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_list_item, parent, false);
        view.setTag(mBooks.get(i));
        return new ViewHolder(view, mItemClickListener);
    }

    // 将数据与界面进行绑定
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i){
        Book book = mBooks.get(i);
        viewHolder.ivBookImg.setImageResource(R.drawable.book);
        viewHolder.tvBookTitle.setText(book.getTitle());
        viewHolder.tvBookAuthor.setText(book.getAuthor());
        viewHolder.tvBookPublish.setText(book.getPublisher() + "/" + book.getPublishDate());
        viewHolder.tvBookIsbn.setText(book.getIsbn());
        viewHolder.itemView.setTag(mBooks.get(i));
    }

    @Override
    public int getItemCount(){
        return mBooks == null ? 0 : mBooks.size();
    }

    public void setOnItemClickListener(ViewHolder.ItemClickListener listener){
        this.mItemClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        final ImageView ivBookImg;
        final TextView tvBookTitle;
        final TextView tvBookAuthor;
        final TextView tvBookPublish;
        final TextView tvBookIsbn;
        private ItemClickListener mItemClickListener;

        public ViewHolder(View view, ItemClickListener listener){
            super(view);
            ivBookImg = (ImageView) view.findViewById(R.id.iv_bli_book_img);
            tvBookTitle = (TextView) view.findViewById(R.id.tv_bli_book_title);
            tvBookAuthor = (TextView) view.findViewById(R.id.tv_bli_book_author);
            tvBookPublish = (TextView) view.findViewById(R.id.tv_bli_book_publish);
            tvBookIsbn = (TextView) view.findViewById(R.id.tv_bli_book_isbn);
            mItemClickListener = listener;
            view.setOnClickListener(this);
        }

        @Override
         public void onClick(View v) {
            if (mItemClickListener != null){
                mItemClickListener.onItemClick(v, getPosition());
            }
        }

        public interface ItemClickListener{
            void onItemClick(View view, int position);
        }
    }
}
