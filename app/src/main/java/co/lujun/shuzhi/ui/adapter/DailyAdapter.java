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
import co.lujun.shuzhi.bean.Daily;

/**
 * Created by lujun on 2015/7/17.
 */
public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.ViewHolder> {

    private List<Daily> mDailies;
    private ViewHolder.ItemClickListener mItemClickListener;

    public DailyAdapter(List<Daily> dailies) {
        this.mDailies = dailies;
    }

    // 创建View，被LayoutManager调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater
                .from(parent.getContext()).inflate(R.layout.daily_list_item, parent, false);
        view.setTag(mDailies.get(i));
        return new ViewHolder(view, mItemClickListener);
    }

    // 将数据与界面进行绑定
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i){
        Daily daily = mDailies.get(i);
        Book book = daily.getBook();
        if (!TextUtils.isEmpty(book.getImages().getSmall())){
            Glide.with(GlApplication.getContext()).load(book.getImages().getSmall())
                    .into(viewHolder.ivBookImg);
        }
        viewHolder.tvBookTitle.setText(
                GlApplication.getContext().getResources().getString(R.string.tv_title)
                        + book.getTitle());
        String author = "";
        for (int j = 0; j < book.getAuthor().length; j++){
            author += book.getAuthor()[j] + "、";
        }
        if (author.length() > 0){ author = author.substring(0, author.length() - 1); }
        viewHolder.tvBookAuthor.setText(
                GlApplication.getContext().getResources().getString(R.string.tv_author) + author);
        viewHolder.tvBookVol.setText(
                GlApplication.getContext().getResources().getString(R.string.tv_vol)
                        + daily.getExtra().getVol());
        viewHolder.tvBookSZDate.setText(daily.getExtra().getDate());
        viewHolder.tvBookSub.setText(
                GlApplication.getContext().getResources().getString(R.string.tv_daily)
                        + daily.getExtra().getBrief());
        viewHolder.itemView.setTag(mDailies.get(i));
    }

    @Override
    public int getItemCount(){
        return mDailies == null ? 0 : mDailies.size();
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
        final TextView tvBookSZDate;
        private ItemClickListener mItemClickListener;

        public ViewHolder(View view, ItemClickListener listener){
            super(view);
            ivBookImg = (ImageView) view.findViewById(R.id.iv_dli_book_img);
            tvBookTitle = (TextView) view.findViewById(R.id.tv_dli_book_title);
            tvBookAuthor = (TextView) view.findViewById(R.id.tv_dli_book_author);
            tvBookSub = (TextView) view.findViewById(R.id.tv_dli_book_sub);
            tvBookVol = (TextView) view.findViewById(R.id.tv_dli_book_vol);
            tvBookSZDate = (TextView) view.findViewById(R.id.tv_dli_book_date);
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
