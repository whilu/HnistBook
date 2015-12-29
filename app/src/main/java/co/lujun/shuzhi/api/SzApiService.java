package co.lujun.shuzhi.api;

import co.lujun.shuzhi.bean.Daily;
import co.lujun.shuzhi.bean.ListData;
import co.lujun.shuzhi.bean.Token;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import rx.Observable;

/**
 * Author: lujun(http://blog.lujun.co)
 * Date: 15-12-28 22:42
 */
public interface SzApiService {

    @GET("/Index/getToken") Observable<Token> getSzToken();

    @FormUrlEncoded @POST("/Book/today") Observable<Daily> getSzDaily(
            @Field("timestamp") String timestamp, @Field("sign") String sign);

    @FormUrlEncoded @POST("/Book/{t}") Observable<ListData> getSzBookList(
            @Path("t") String t, @Field("timestamp") String timestamp, @Field("sign") String sign);

}
