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
    public static final String DAILY_CHANNEL = "daily_channel";
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
    public static final int BLUR_RADIUS = 7;

    // Douban host
    public final static String DOUBAN_HOST = "https://api.douban.com/v2/book";
}
