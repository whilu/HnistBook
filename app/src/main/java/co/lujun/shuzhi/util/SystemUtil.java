package co.lujun.shuzhi.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import co.lujun.shuzhi.GlApplication;

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

    /**
     * blur bitmap
     * @param context
     * @param input
     * @param radius
     * @return
     */
    public static Bitmap blurImage(Context context, Bitmap input, float radius) {
        RenderScript rsScript = RenderScript.create(context);
        Allocation alloc = Allocation.createFromBitmap(rsScript, input);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rsScript, alloc.getElement());
        blur.setRadius(radius);
        blur.setInput(alloc);

        Bitmap result = Bitmap.createBitmap(input.getWidth(), input.getHeight(), input.getConfig());
        Allocation outAlloc = Allocation.createFromBitmap(rsScript, result);
        blur.forEach(outAlloc);
        outAlloc.copyTo(result);

        rsScript.destroy();
        return result;
    }

    /**
     * check permission when system version >= 6.0 & targetSDKVersion >= 23
     * @param activity
     * @param permissions
     * @param permissionCode
     */
    public static void checkPermission(Activity activity, String[] permissions, int permissionCode){
        if (ContextCompat.checkSelfPermission(activity, permissions[0])
                != PackageManager.PERMISSION_GRANTED){
//            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[0])){
                // add your own explations to user
//            }else {
                ActivityCompat.requestPermissions(activity, permissions, permissionCode);
//            }
        }
    }

}
