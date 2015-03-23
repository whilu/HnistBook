package cn.hnist.lib.android.hnistbook.bean;

import org.litepal.crud.DataSupport;

import java.util.List;

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

    private Images images;

    private String[] author, translator;

    private String publisher;

    private String pubdate;

    private Rating rating;

    private List<Tag> tags;

    private String binding;

    private String price;

    private Series series;

    private String pages;

    private String author_intro;

    private String summary;

    private String catalog;//序言

    private String ebook_url;

    private String ebook_price;

    public Book(){
        super();
    }

    public Book(String id, String isbn10, String isbn13, String title, String origin_title,
                String alt_title, String subtitle, String url, String alt, String image,
                Images images, String[] author, String[] translator, String publisher,
                String pubdate, Rating rating, List<Tag> tags, String binding,
                String price, Series series, String pages, String author_intro,
                String summary, String catalog, String ebook_url, String ebook_price){
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
        this.images = images;
        this.author = author;
        this.translator = translator;
        this.publisher = publisher;
        this.pubdate = pubdate;
        this.rating = rating;
        this.tags = tags;
        this.binding = binding;
        this.price = price;
        this.series = series;
        this.pages = pages;
        this.author_intro = author_intro;
        this.summary = summary;
        this.catalog = catalog;
        this.ebook_url = ebook_url;
        this.ebook_price = ebook_price;
    }

    public String getId(){ return id; }

    public void setId(String id){ this.id = id;}//set方法名与fastJson中key匹配

    public String getIsbn10(){ return isbn10; }

    public void setIsbn10(String isbn10){ this.isbn10 = isbn10; }

    public String getIsbn13(){ return isbn13; }

    public void setIsbn13(String isbn13){ this.isbn13 = isbn13; }

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

    public Images getImages(){ return  this.images; }

    public void setImages(Images images){ this.images = images; }

    public String[] getAuthor(){ return author; }

    public void setAuthor(String[] author){ this.author = author; }

    public String[] getTranslator(){ return translator; }

    public void setTranslator(String[] translator){ this.translator = translator; }

    public String getPublisher(){ return publisher; }

    public void setPublisher(String publisher){ this.publisher = publisher; }

    public String getPubdate(){ return pubdate; }

    public void setPubdate(String pubdate){ this.pubdate = pubdate; }

    public Rating getRating(){ return rating; }

    public void setRating(Rating rating){ this.rating = rating; }

    public List<Tag> getTags(){ return tags; }

    public void setTags(List<Tag> tags){ this.tags = tags; }

    public String getBinding(){ return binding; }

    public void setBinding(String binding){ this.binding = binding; }

    public String getPrice(){ return price; }

    public void setPrice(String price){ this.price = price; }

    public Series getSeries(){ return series; }

    public void setSeries(Series series){ this.series = series; }

    public String getPages(){ return pages; }

    public void setPages(String pages){ this.pages = pages; }

    public String getAuthor_intro(){ return author_intro; }

    public void setAuthor_intro(String author_intro){ this.author_intro = author_intro; }

    public String getSummary(){ return summary; }

    public void setSummary(String summary){ this.summary = summary; }

    public String getCatalog(){ return catalog; }

    public void setCatalog(String catalog){ this.catalog = catalog; }

    public String getEbook_url(){ return ebook_url; }

    public void setEbook_url(String ebook_price){ this.ebook_price = ebook_price; }

    public String getEbook_price(){ return ebook_price; }

    public void setEbook_price(String ebook_price){ this.ebook_price = ebook_price; }
}
