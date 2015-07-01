package cn.hnist.lib.android.hnistbook.api;

/**
 * Created by lujun on 2015/3/22.
 */
public class Api {

    private final static String DOUBAN_HOST = "https://api.douban.com/v2/book/";
    public final static String BOOK_SEARCH_URL = DOUBAN_HOST + "search";
    public final static String GET_ISBNBOOK_URL = DOUBAN_HOST + "isbn/";

    public final static String GET_TOKEN_URL = "http://bookz.sinaapp.com/v1/Index/getToken";
    public final static String GET_TODAY_BOOK_URL = "http://bookz.sinaapp.com/v1/Book/today";
}
