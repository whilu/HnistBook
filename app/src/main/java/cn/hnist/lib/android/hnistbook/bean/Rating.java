package cn.hnist.lib.android.hnistbook.bean;

/**
 * Created by lujun on 2015/3/23.
 */
public class Rating {

    private int max, min;

    private long numRaters;

    private String average;

    public int getMax(){ return max; }

    public void setMax(int max){ this.max = max; }

    public int getMin(){ return min; }

    public void setMin(int min){ this.min = min; };

    public long getNumRaters(){ return  numRaters; }

    public void setNumRaters(long numRaters){ this.numRaters = numRaters; }

    public String getAverage(){ return average; }

    public void setAverage(String average){ this.average = average; }
}
