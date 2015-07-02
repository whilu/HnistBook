package cn.hnist.lib.android.hnistbook.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by lujun on 2015/5/23.
 */
public class Annotation extends DataSupport {

    private String id;

    private String book_id;

    private Book book;

    private String author_id;

    private User author_user;

    private String chapter;

    private int page_no;

    private int privacy;

    private String Abstract;

    private String content;

    private String abstract_photo;

    private Photo photos;

    private int last_photo;

    private int comments_count;

    private boolean hasmath;

    private String time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public User getAuthor_user() {
        return author_user;
    }

    public void setAuthor_user(User author_user) {
        this.author_user = author_user;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public int getPage_no() {
        return page_no;
    }

    public void setPage_no(int page_no) {
        this.page_no = page_no;
    }

    public int getPrivacy() {
        return privacy;
    }

    public void setPrivacy(int privacy) {
        this.privacy = privacy;
    }

    public String getAbstract() {
        return Abstract;
    }

    public void setAbstract(String anAbstract) {
        Abstract = anAbstract;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAbstract_photo() {
        return abstract_photo;
    }

    public void setAbstract_photo(String abstract_photo) {
        this.abstract_photo = abstract_photo;
    }

    public Photo getPhotos() {
        return photos;
    }

    public void setPhotos(Photo photos) {
        this.photos = photos;
    }

    public int getLast_photo() {
        return last_photo;
    }

    public void setLast_photo(int last_photo) {
        this.last_photo = last_photo;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public boolean isHasmath() {
        return hasmath;
    }

    public void setHasmath(boolean hasmath) {
        this.hasmath = hasmath;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
