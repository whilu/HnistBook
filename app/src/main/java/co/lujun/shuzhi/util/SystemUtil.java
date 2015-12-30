package co.lujun.shuzhi.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.widget.Toast;

import co.lujun.shuzhi.App;

/**
 * Created by lujun on 2015/9/15.
 */
public class SystemUtil {

    /**
     * show toast
     * @param strId
     */
    public static void showToast(int strId){
        Toast.makeText(App.getContext(),
                App.getContext().getString(strId), Toast.LENGTH_SHORT).show();
    }

    /**
     * show toast
     * @param string
     */
    public static void showToast(String string){
        Toast.makeText(App.getContext(), string, Toast.LENGTH_SHORT).show();
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

    /**
     * check device has navagtion bar
     * @param context
     * @return
     */
    public static boolean checkIfDeviceHasNavBar(Context context){
        boolean hasMenuKey = false;
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
        }
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        if (!hasMenuKey && !hasBackKey){
            return true;
        }
        return false;
    }

}
