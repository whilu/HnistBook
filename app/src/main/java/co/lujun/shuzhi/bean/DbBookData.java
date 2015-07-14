package co.lujun.shuzhi.bean;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by lujun on 2015/7/14.
 */
public class DbBookData<T> extends DataSupport {

    private int count;

    private int start;

    private long total;

    private List<T> mList;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public List<T> getList() {
        return mList;
    }

    public void setList(List<T> mList) {
        this.mList = mList;
    }

}
