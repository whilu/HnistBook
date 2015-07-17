package co.lujun.shuzhi.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lujun on 2015/7/14.
 */
public class ListData extends DataSupport implements Serializable {

    private static final long serialVersionUID = 1L; // 序列化ID

    private int count;

    private int start;

    private long total;

    private List<Annotation> annotations;

    private List<Book> books;

    private List<Daily> dailies;

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

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public List<Daily> getDailies() {
        return dailies;
    }

    public void setDailies(List<Daily> dailies) {
        this.dailies = dailies;
    }

}
