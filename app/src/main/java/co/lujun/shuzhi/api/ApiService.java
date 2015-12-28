package co.lujun.shuzhi.api;

import co.lujun.shuzhi.bean.Book;
import co.lujun.shuzhi.bean.Daily;
import co.lujun.shuzhi.bean.ListData;
import co.lujun.shuzhi.bean.Token;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Author: lujun(http://blog.lujun.co)
 * Date: 15-12-28 22:42
 */
public interface ApiService {

    @GET("/Index/getToken") Observable<Token> getToken();

    @GET("") Observable<ListData> getDbBookList(
            @Query("q") String q, @Query("start") int start, @Query("apikey") String apikey);

    @GET("/isbn/{isbn}") Observable<Book> getDbBook(
            @Path("isbn") String isbn, @Query("apikey") String apikey);

    @FormUrlEncoded @POST("/Book/today") Observable<Daily> getDaily(
            @Field("timestamp") String timestamp, @Field("sign") String sign);

    @FormUrlEncoded @POST("") Observable<ListData> getSzBookList(
            @Field("timestamp") String timestamp, @Field("sign") String sign);


}
