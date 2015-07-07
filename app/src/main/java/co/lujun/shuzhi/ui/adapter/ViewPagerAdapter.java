package co.lujun.shuzhi.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by lujun on 2015/3/9.
 */
public class ViewPagerAdapter extends PagerAdapter {
    private ArrayList<View> mViews;
    private ArrayList<String> mTitles;

    public ViewPagerAdapter(ArrayList<View> views, ArrayList<String> titles) {
        // TODO Auto-generated constructor stub
        this.mViews = views;
        this.mTitles = titles;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(this.mViews.get(position), 0);
        return this.mViews.get(position);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return this.mViews.size();
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView(this.mViews.get(position));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return this.mTitles.get(position);
    }

    @Override
    public Object instantiateItem(View container, int position) {
        ((ViewPager) container).addView(mViews.get(position));
        return mViews.get(position);
    }
}
