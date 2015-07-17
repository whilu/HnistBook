package co.lujun.shuzhi.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import co.lujun.shuzhi.GlApplication;
import co.lujun.shuzhi.R;
import co.lujun.shuzhi.bean.Book;

/**
 * Created by lujun on 2015/7/17.
 */
public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.ViewHolder> {

    private List<Book> mBooks;
    private ViewHolder.ItemClickListener mItemClickListener;

    public DailyAdapter(List<Book> books) {
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
        if (!TextUtils.isEmpty(book.getImages().getSmall())){
            Glide.with(GlApplication.getContext()).load(book.getImages().getSmall())
                    .into(viewHolder.ivBookImg);
        }
        viewHolder.tvBookTitle.setText(book.getTitle());
        String author = "";
        for (int j = 0; j < book.getAuthor().length; j++){
            author += book.getAuthor()[j] + "、";
        }
        if (author.length() > 0){ author = author.substring(0, author.length() - 1); }
        viewHolder.tvBookAuthor.setText(author);
//        viewHolder.tvBookVol.setText(book.getPublisher() + "/" + book.getPubdate());
//        viewHolder.tvBookSub.setText(book.getSummary());
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
        final TextView tvBookSub;
        final TextView tvBookVol;
        private ItemClickListener mItemClickListener;

        public ViewHolder(View view, ItemClickListener listener){
            super(view);
            ivBookImg = (ImageView) view.findViewById(R.id.iv_dli_book_img);
            tvBookTitle = (TextView) view.findViewById(R.id.tv_dli_book_title);
            tvBookAuthor = (TextView) view.findViewById(R.id.tv_dli_book_author);
            tvBookSub = (TextView) view.findViewById(R.id.tv_dli_book_sub);
            tvBookVol = (TextView) view.findViewById(R.id.tv_dli_book_vol);
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
