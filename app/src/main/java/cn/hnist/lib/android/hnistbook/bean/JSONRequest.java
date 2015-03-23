package cn.hnist.lib.android.hnistbook.bean;

import com.alibaba.fastjson.JSON;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;

/**
 * Created by lujun on 2015/3/23.
 */
public class JSONRequest<T> extends Request<T> {

    private final Response.Listener<T> mListener;
    private Class<T> mClass;
    private JSON mJson;

    public JSONRequest(int method, String url, Class<T> clazz, Response.Listener<T> listener,
                       Response.ErrorListener errorListener){
        super(method, url, errorListener);
        mClass = clazz;
        mListener = listener;
    }

    public JSONRequest(String url, Class<T> clazz, Response.Listener<T> listener,
                       Response.ErrorListener errorListener){
        this(Method.GET, url, clazz, listener, errorListener);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String jsonString = new String(networkResponse.data,
                    HttpHeaderParser.parseCharset(networkResponse.headers));
            return Response.success(JSON.parseObject(jsonString, mClass),
                    HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (UnsupportedEncodingException e){
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T t) {
        mListener.onResponse(t);
    }
}
