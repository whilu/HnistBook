package cn.hnist.lib.android.hnistbook.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.hnist.lib.android.hnistbook.R;
import cn.hnist.lib.android.hnistbook.api.Api;
import cn.hnist.lib.android.hnistbook.bean.Constant;
import cn.hnist.lib.android.hnistbook.bean.Token;

/**
 * Created by lujun on 2015/4/3.
 */
public class TokenUtils {

    private Context mContext;
    private RequestQueue mQueen;
    private Handler mHandler;

    public TokenUtils(Context context, Handler handler){
        mContext = context;
        mHandler = handler;
        mQueen = Volley.newRequestQueue(context);
    }

    public void getData(final Map<String, String> map, final String url){
        if (NetWorkUtils.getNetWorkType(mContext) == NetWorkUtils.NETWORK_TYPE_DISCONNECT){
            Toast.makeText(mContext, mContext.getResources().getString(R.string.msg_no_internet),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        // 获取TOKEN
        JsonObjectRequest tokenRequest = new JsonObjectRequest(Api.GET_TOKEN_URL, null,
                new Response.Listener<JSONObject>(){

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Token token = JSON.parseObject(jsonObject.toString(), Token.class);
                        if (token.getStatus() == 1 && map != null){
                            map.put("timestamp", System.currentTimeMillis() + "");
                            String signature = makeSignature(token.getData(), map);
                            map.put("sign", signature);
                            getContent(map, url);
                        }else {
                            mHandler.sendEmptyMessage(Constant.MSG_REQUEST_FAILED);
                        }
                    }
                }, new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                        Toast .makeText(mContext, volleyError.getMessage(),
                                Toast.LENGTH_SHORT).show();
            }
        });
        mQueen.add(tokenRequest);
    }

    /** 签名*/
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

    /** 获取URL请求的内容POST*/
    private void getContent(final Map<String, String> map, String url){
        StringRequest contentReqest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String s) {
                        Message msg = mHandler.obtainMessage();
                        msg.obj = s;
                        msg.what = Constant.MSG_REQUEST_SUCCESS;
                        mHandler.sendMessage(msg);
                    }
                }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast .makeText(mContext, volleyError.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
        mQueen.add(contentReqest);
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
}
