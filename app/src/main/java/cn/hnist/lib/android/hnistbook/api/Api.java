package cn.hnist.lib.android.hnistbook.api;

/**
 * Created by lujun on 2015/3/22.
 */
public class Api {

    private final static String DOUBAN_HOST = "https://api.douban.com/v2/book/";
    public final static String BOOK_SEARCH_URL = DOUBAN_HOST + "search";
    public final static String GET_ISBNBOOK_URL = DOUBAN_HOST + "isbn/";

    private final static String SZ_HOST = "http://bookz.sinaapp.com/v1/";
    public final static String GET_TOKEN_URL = SZ_HOST + "Index/getToken";
    public final static String GET_TODAY_BOOK_URL = SZ_HOST + "Book/today";
    public final static String GET_BOOK_ANN_URL = SZ_HOST + "book/annotations";
}
