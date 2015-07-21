package co.lujun.shuzhi.api;

/**
 * Created by lujun on 2015/3/22.
 */
public class Api {

    public final static String DOUBAN_HOST = "https://api.douban.com/v2/book/";
    public final static String BOOK_SEARCH_URL = DOUBAN_HOST + "search";
    public final static String GET_ISBNBOOK_URL = DOUBAN_HOST + "isbn/";

    private final static String SZ_HOST = "http://api.lujun.co/bookz/v1/";
    public final static String GET_TOKEN_URL = SZ_HOST + "Index/getToken";
    public final static String GET_TODAY_BOOK_URL = SZ_HOST + "Book/today";
    public final static String GET_7DAY_BOOK_URL = SZ_HOST + "Book/week";
    public final static String GET_30DAY_BOOK_URL = SZ_HOST + "Book/month";
}
