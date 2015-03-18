package cn.hnist.lib.android.hnistbook.model;

import org.litepal.crud.DataSupport;

/**
 * Created by lujun on 2015/3/18.
 */
public class Book extends DataSupport {

    private String imgUrl;

    private String title;

    private String author;

    private String publisher;

    private String publishDate;

    private String isbn;

    public String getImgUrl(){ return imgUrl; }

    public void setImgUrl(String imgUrl){ this.imgUrl = imgUrl; }

    public String getTitle(){ return title; }

    public void setTitle(String title){ this.title = title; }

    public String getAuthor(){ return author; }

    public void setAuthor(String author){ this.author = author; }

    public String getPublisher(){ return publisher; }

    public void setPublisher(String publisher){ this.publisher = publisher; }

    public String getPublishDate(){ return publishDate; }

    public void setPublishDate(String publishDate){ this.publishDate = publishDate; }

    public String getIsbn(){ return isbn; }

    public void setIsbn(String isbn){ this.isbn = isbn; }
}
