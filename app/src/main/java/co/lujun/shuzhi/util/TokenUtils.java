package co.lujun.shuzhi.util;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import co.lujun.shuzhi.App;
import co.lujun.shuzhi.R;
import co.lujun.shuzhi.api.Api;
import co.lujun.shuzhi.bean.JSONRequest;
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
     * 获取POST请求附加信息
     */
    public void getRequestParam(){
        // 网络未连接
        if (NetWorkUtils.getNetWorkType(
                App.getContext()) == NetWorkUtils.NETWORK_TYPE_DISCONNECT){
            if (mResponseListener != null){
                mResponseListener.onFailure(App.getContext()
                        .getResources().getString(R.string.msg_no_internet));
            }
            return;
        }
        // 获取TOKEN
        JSONRequest<Token> jsonRequest = new JSONRequest<Token>(
                Api.GET_TOKEN_URL,
                Token.class,
                new Response.Listener<Token>() {
                    @Override
                    public void onResponse(Token token) {
                        if (token.getStatus() == 1){
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("timestamp", System.currentTimeMillis() + "");
                            String signature = SignatureUtils.makeSignature(token.getData(), map);
                            map.put("sign", signature);
                            if (mResponseListener != null){
                                mResponseListener.onSuccess(map);
                            }
                        }else {
                            if (mResponseListener != null){
                                mResponseListener.onFailure(App.getContext()
                                        .getResources().getString(R.string.msg_get_token_err));
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                        if (mResponseListener != null){
                            //volleyError.getMessage()
                            mResponseListener.onFailure(App.getContext()
                                    .getResources().getString(R.string.msg_request_error));
                        }
                    }
                });
        App.getRequestQueue().add(jsonRequest);
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
        void onSuccess(Map<String, String> map);
    };
}
