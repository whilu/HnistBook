package cn.hnist.lib.android.hnistbook.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by lujun on 2015/3/18.
 */
public class Book extends DataSupport {

    private String id;//ID

    private String isbn10, isbn13;//ISBN10 and ISBN13

    private String title, origin_title, alt_title, subtitle;//title

    private String url;//douban book  url

    private String alt;

    private String image;



    private String imgUrl;

    private String author;

    private String publisher;

    private String publishDate;

    public Book(){
        super();
    }

    public Book(String id, String isbn10, String isbn13, String title, String origin_title,
                String alt_title, String subtitle, String url, String alt, String image,
                String author, String publisher,
                String publishDate, String imgUrl){
        super();
        this.id = id;
        this.isbn10 = isbn10;
        this.isbn13 = isbn13;
        this.title = title;
        this.origin_title = origin_title;
        this.alt_title = alt_title;
        this.subtitle = subtitle;
        this.url = url;
        this.alt = alt;
        this.image = image;


        this.imgUrl = imgUrl;
        this.author = author;
        this.publisher = publisher;
        this.publishDate = publishDate;
    }

    public String getId(){ return id; }

    public void setId(String id){ this.id = id;}//set方法名与fastJson中key匹配

    public String getTitle(){ return title; }

    public void setTitle(String title){ this.title = title; }

    public String getOrigin_title(){ return origin_title; }

    public void setOrigin_title(String origin_title){ this.origin_title = origin_title; }

    public String getAlt_title(){ return alt_title; }

    public void setAlt_title(String alt_title){ this.alt_title = alt_title; }

    public String getSubtitle(){ return subtitle; }

    public void setSubtitle(String subtitle){ this.subtitle = subtitle; }

    public String getUrl(){ return url; }

    public void setUrl(String url){ this.url = url; }

    public String getAlt(){ return alt; }

    public void setAlt(String alt){ this.alt = alt; }

    public String getImage(){ return  image; }

    public void setImage(String image){ this.image = image; }




    public String getImgUrl(){ return imgUrl; }

    public void setImgUrl(String imgUrl){ this.imgUrl = imgUrl; }

    public String getAuthor(){ return author; }

    public void setAuthor(String author){ this.author = author; }

    public String getPublisher(){ return publisher; }

    public void setPublisher(String publisher){ this.publisher = publisher; }

    public String getPublishDate(){ return publishDate; }

    public void setPublishDate(String publishDate){ this.publishDate = publishDate; }

    public String getIsbn10(){ return isbn10; }

    public void setIsbn10(String isbn10){ this.isbn10 = isbn10; }

    public String getIsbn13(){ return isbn13; }

    public void setIsbn13(String isbn13){ this.isbn13 = isbn13; }
}
