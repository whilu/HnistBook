package cn.hnist.lib.android.hnistbook.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.hnist.lib.android.hnistbook.GlApplication;
import cn.hnist.lib.android.hnistbook.R;
import cn.hnist.lib.android.hnistbook.bean.Annotation;

/**
 * Created by lujun on 2015/3/1.
 */
public class AnnotationAdapter extends RecyclerView.Adapter<AnnotationAdapter.ViewHolder> {

    private List<Annotation> mAnns;
    private ViewHolder.ItemClickListener mItemClickListener;

    public AnnotationAdapter(List<Annotation> anns) {
        this.mAnns = anns;
    }

    // 创建View，被LayoutManager调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ann_list_item, parent, false);
        view.setTag(mAnns.get(i));
        return new ViewHolder(view, mItemClickListener);
    }

    // 将数据与界面进行绑定
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i){
        Annotation annotation = mAnns.get(i);
        if (!TextUtils.isEmpty(annotation.getAuthor_user().getAvatar())){
            Glide.with(GlApplication.getContext()).load(annotation.getAuthor_user().getAvatar())
                    .into(viewHolder.ivAvatar);
        }
        viewHolder.tvName.setText(annotation.getAuthor_user().getName());
        viewHolder.tvTime.setText(annotation.getTime().substring(0, 10));
        viewHolder.tvAbstract.setText(annotation.getAbstract());
        viewHolder.tvBookTitle.setText("《" +annotation.getBook().getTitle() + "》");
        viewHolder.tvChapter.setText(
                GlApplication.getContext().getResources().getString(R.string.tv_chapter)
                        + annotation.getChapter());
        viewHolder.itemView.setTag(mAnns.get(i));
    }

    @Override
    public int getItemCount(){
        return mAnns == null ? 0 : mAnns.size();
    }

    public void setOnItemClickListener(ViewHolder.ItemClickListener listener){
        this.mItemClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        final ImageView ivAvatar;
        final TextView tvName;
        final TextView tvTime;
        final TextView tvAbstract;
        final TextView tvChapter;
        final TextView tvBookTitle;
        private ItemClickListener mItemClickListener;

        public ViewHolder(View view, ItemClickListener listener){
            super(view);
            ivAvatar = (ImageView) view.findViewById(R.id.iv_ann_avatar);
            tvName = (TextView) view.findViewById(R.id.tv_ann_name);
            tvTime = (TextView) view.findViewById(R.id.tv_ann_time);
            tvAbstract = (TextView) view.findViewById(R.id.tv_ann_abstract);
            tvChapter = (TextView) view.findViewById(R.id.tv_ann_chapter);
            tvBookTitle = (TextView) view.findViewById(R.id.tv_ann_book_title);
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
