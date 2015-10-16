package co.lujun.shuzhi.util;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import co.lujun.shuzhi.GlApplication;
import co.lujun.shuzhi.R;

/**
 * Created by lujun on 2015/9/15.
 */
public class SystemUtil {

    /**
     * 自动显示或隐藏软键盘静态方法
     * @param context
     */
    public static void showOrHideInputMethodManager(Context context){
        InputMethodManager mInputMethodManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 强制隐藏软键盘方法
     * @param activity 当前软键盘所在的activity
     */
    public static void hideInputMethodManager(Activity activity){
        InputMethodManager mInputMethodManager =
                (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    /**
     * show toast
     * @param strId
     */
    public static void showToast(int strId){
        Toast.makeText(GlApplication.getContext(),
                GlApplication.getContext().getString(strId), Toast.LENGTH_SHORT).show();
    }

    /**
     * show toast
     * @param string
     */
    public static void showToast(String string){
        Toast.makeText(GlApplication.getContext(), string, Toast.LENGTH_SHORT).show();
    }
}
