package co.lujun.shuzhi.bean;

/**
 * Created by lujun on 2015/3/17.
 */
public class Config {

    /**
     * serialVersionUID
     *
     * DbBookData.class 1L
     * Annotation.class 2L
     * Book.class 3L
     * Images.class 4L
     * Rating.class 5L
     * Series.class 6L
     * Tag.class 7L
     * User.class 8L
     * Photo.class 9L
     * JsonData.class 10L
     * Extra.class 11L
     */

    //key
    public static final String BOOK_LST_SEARCH_KEY = "BookListFragment_search_key";
    public static final String DAILY_LST_TYPE = "DailyListFragment_url";
    public static final String SEARCH_KEY = "search_key_words";
    public static final String CONFIG_PUSH_MSG_KEY = "config_push_message_key";
    public static final String CONFIG_AUTO_UPDATE_KEY = "config_auto_update_key";

    //book attribute
    public enum BOOK{id, isbn10, isbn13, title};

    //cache file config
    public static final String SZ_CACHE_FILE_PATH = "_sz_cache";
    public static final String ANN_CACHE_FILE_PATH = "_ann_cache";
    public static final String IMG_PATH = "/shuzhi/image/";
    public static final String SHARE_IMG_NAME = "shuzhishareimg.png";

    //app config
    public static final long APP_SPLASH_TIME = 1000;

    //third platform config
    public static final String WB_REDIRECT_URL = "http://app.lujun.co/shuzhi";
    public static final String WB_APP_KEY = "2165713124";
    public static final String WB_APP_SECRET = "4837b12b453c76218d45541f846decef";
    public static final String WX_APP_ID = "wxfe5a81b8f8721c6f";
    public static final String WX_APP_SECRET = "c51762ec0e179bc78bcaf41e452d4037";
    public static final String QQ_APP_ID = "1104730321";
    public static final String QQ_APP_KEY = "9QoiADlAfE5TlMZQ";
}
