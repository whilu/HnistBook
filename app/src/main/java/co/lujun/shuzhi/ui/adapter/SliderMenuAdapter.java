package co.lujun.shuzhi.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import co.lujun.shuzhi.GlApplication;
import co.lujun.shuzhi.R;

/**
 * Created by lujun on 2015/3/1.
 */
public class SliderMenuAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private static String[] mPlanetTitles;
    private static int[] mPlanetIcons;
    private boolean isFirstDrawer;

    public SliderMenuAdapter(String[] planetTitles, int[] planetIcons){
        isFirstDrawer = true;
        mPlanetTitles = planetTitles;
        mPlanetIcons = planetIcons;
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
        return Math.min(mPlanetTitles.length, mPlanetIcons.length);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
            holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
            holder.tvLabel = (TextView) convertView.findViewById(R.id.tv_label);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (isFirstDrawer && position == 0) {
            isFirstDrawer = false;
            convertView.setBackgroundColor(GlApplication.getContext().getResources().getColor(R.color.gray));
        }
        holder.ivIcon.setImageResource(mPlanetIcons[position]);
        holder.tvLabel.setText(mPlanetTitles[position]);
        return convertView;
    }

    private static class ViewHolder{
        ImageView ivIcon;
        TextView tvLabel;
    }
}
