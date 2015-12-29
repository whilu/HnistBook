package co.lujun.shuzhi.api;

import co.lujun.shuzhi.bean.Book;
import co.lujun.shuzhi.bean.ListData;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Author: lujun(http://blog.lujun.co)
 * Date: 15-12-28 22:42
 */
public interface DbApiService {

    @GET("/search") Observable<ListData> getDbBookList(
            @Query("q") String q, @Query("start") int start, @Query("apikey") String apikey);

    @GET("/isbn/{isbn}") Observable<Book> getDbBook(
            @Path("isbn") String isbn, @Query("apikey") String apikey);

}
