package cn.hnist.lib.android.hnistbook.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cn.hnist.lib.android.hnistbook.GlApplication;
import cn.hnist.lib.android.hnistbook.R;

/**
 * Created by lujun on 2015/3/1.
 */
public class BookAdapter extends BaseAdapter {
    private LayoutInflater mInflater;

    public BookAdapter(){
        mInflater = LayoutInflater.from(GlApplication.getContext());
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.book_list_item, null);
            holder.ivBookImg = (ImageView) convertView.findViewById(R.id.iv_bli_book_img);
            holder.tvBookTitle = (TextView) convertView.findViewById(R.id.tv_bli_book_title);
            holder.tvBookAuthor = (TextView) convertView.findViewById(R.id.tv_bli_book_author);
            holder.tvBookPublish = (TextView) convertView.findViewById(R.id.tv_bli_book_publish);
            holder.tvBookIsbn = (TextView) convertView.findViewById(R.id.tv_bli_book_isbn);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        //

        return convertView;
    }

    class ViewHolder{
        ImageView ivBookImg;
        TextView tvBookTitle;
        TextView tvBookAuthor;
        TextView tvBookPublish;
        TextView tvBookIsbn;
    }
}
