package co.lujun.shuzhi.util;

import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.lujun.shuzhi.GlApplication;
import co.lujun.shuzhi.R;
import co.lujun.shuzhi.api.Api;
import co.lujun.shuzhi.bean.JSONRequest;
import co.lujun.shuzhi.bean.JsonData;
import co.lujun.shuzhi.bean.Token;

/**
 * Created by lujun on 2015/4/3.
 */
public class TokenUtils {
    /**
     * 回调接口
     */
    private OnResponseListener mResponseListener;

    public TokenUtils(){
    }

    /**
     * 获取内容
     * @param map
     * @param url
     */
    public void getData(final Map<String, String> map, final String url){
        if (NetWorkUtils.getNetWorkType(
                GlApplication.getContext()) == NetWorkUtils.NETWORK_TYPE_DISCONNECT){
            Toast.makeText(
                    GlApplication.getContext(),
                    GlApplication.getContext().getResources().getString(R.string.msg_no_internet),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        // 获取TOKEN
        JSONRequest<Token> jsonRequest = new JSONRequest<Token>(
                Api.GET_TOKEN_URL,
                Token.class,
                new Response.Listener<Token>() {
                    @Override
                    public void onResponse(Token token) {
                        if (token.getStatus() == 1 && map != null){
                            map.put("timestamp", System.currentTimeMillis() + "");
                            String signature = makeSignature(token.getData(), map);
                            map.put("sign", signature);
                            getContent(map, url);
                        }else {
                            if (mResponseListener != null){
                                mResponseListener.onFailure(GlApplication.getContext()
                                        .getResources().getString(R.string.msg_get_token_err));
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                        Toast .makeText(GlApplication.getContext(), volleyError.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
        GlApplication.getRequestQueue().add(jsonRequest);
    }

    /**
     * 获取URL请求的内容，POST方式
     * @param map
     * @param url
     */
    private void getContent(final Map<String, String> map, String url){
        JSONRequest<JsonData> jsonRequest = new JSONRequest<JsonData>(
                Request.Method.POST,
                url, JsonData.class,
                new Response.Listener<JsonData>() {
                    @Override
                    public void onResponse(JsonData jsonData) {
                        if (mResponseListener != null){
                            mResponseListener.onSuccess(jsonData);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (mResponseListener != null){
                            mResponseListener.onFailure(volleyError.getMessage());
                        }
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
        GlApplication.getRequestQueue().add(jsonRequest);
    }

    /**
     * 签名
     * @param token
     * @param map
     * @return
     */
    private String makeSignature(String token, Map<String, String> map){
        if (map == null || map.isEmpty()){
            return "";
        }
        Map<String, String> sortedMap = new TreeMap<String, String>(new Comparator<String>(){

            @Override
            public int compare(String lhs, String rhs) {
                int intKey1 = 0, intKey2 = 0;
                try {
                    intKey1 = getInt(lhs);
                    intKey2 = getInt(rhs);
                } catch (Exception e) {
                    intKey1 = 0;
                    intKey2 = 0;
                }
                return intKey1 - intKey2;
            }
        });
        sortedMap.putAll(map);
        StringBuilder builder = new StringBuilder();
        Set<Map.Entry<String, String>> set = sortedMap.entrySet();
        for (Iterator<Map.Entry<String, String>> iterator = set.iterator(); iterator.hasNext();) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
            builder.append(entry.getKey() + entry.getValue());
        }
        builder.append(token);
        try {
            return MD5.getMD5(builder.toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    /** */
    private int getInt(String str){
        int i = 0;
        try {
            Pattern p = Pattern.compile("^\\d+");
            Matcher m = p.matcher(str);
            if (m.find()) {
                i = Integer.valueOf(m.group());
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return i;
    }

    /**
     * set response listener
     * @param listener
     */
    public void setResponseListener(OnResponseListener listener){
        this.mResponseListener = listener;
    }

    /**
     * response listener
     */
    public interface OnResponseListener{
        void onFailure(String s);
        void onSuccess(JsonData jsonData);
    };
}
